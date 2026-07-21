# Serviece

`Service` 服务类用于管理驱动程序，用户在通过驱动和浏览器进行通信的这一整个过程可以认为是一次服务，这次服务发生在哪个位置，哪个端口，多长时间，是否持续服务等等，都是由 `Service` 类来完成的。

## 源码

```python
class Service(ABC):
    """
    该类是所有 Service 类的抽象基类，服务通常会在新进程中启动一个子程序作为中间进程，以与浏览器进行通信。

    参数列表：
    		excutable_path：开发者指定驱动的位置
    		port：服务运行的端口，默认值为 0，此时将由操作系统决定该端口
    		log_output：日志级别
    		driver_path_env_key：和 excutable_path 一样都是指定驱动文件路径
    		
    补充：这里补充 excutable_path 和 driver_path_env_key 的区别
		 1.优先级：excutable_path 的优先级高于 driver_path_env_key 
		 2.驱动文件获取方式：excutable_path 的文件获取方式是直接根据开发者所提供的路径获取；
		 driver_path_env_key 的文件获取方式是基于环境变量来获取，这也就意味着开发者需要提前配置操作系统的环境变量
    """

    def __init__(
        self,
        executable_path: Optional[str] = None,
        port: int = 0,
        log_output: Optional[SubprocessStdAlias] = None,
        env: Optional[Mapping[Any, Any]] = None,
        driver_path_env_key: Optional[str] = None,
        **kwargs,
    ) -> None:
        """
        初始化方法
        """
        self.log_output: Optional[Union[int, IOBase]]
        if isinstance(log_output, str):
            self.log_output = cast(IOBase, open(log_output, "a+", encoding="utf-8"))
        elif log_output == subprocess.STDOUT:
            self.log_output = None
        elif log_output is None or log_output == subprocess.DEVNULL:
            self.log_output = subprocess.DEVNULL
        else:
            self.log_output = cast(Union[int, IOBase], log_output)

        self.port = port or utils.free_port()
        # 每个 Python 子进程的默认值：subprocess.Popen (..., creationflags=0)
        self.popen_kw = kwargs.pop("popen_kw", {})
        self.creation_flags = self.popen_kw.pop("creation_flags", 0)
        self.env = env or os.environ
        self.DRIVER_PATH_ENV_KEY = driver_path_env_key
        self._path = self.env_path() or executable_path

    @property
    def service_url(self) -> str:
        """ 获取服务的 IP 地址 """
        return f"http://{utils.join_host_port('localhost', self.port)}"

    @abstractmethod
    def command_line_args(self) -> list[str]:
        """ 程序参数列表（不包括可执行文件） """
        raise NotImplementedError("This method needs to be implemented in a sub class")

    @property
    def path(self) -> str:
        """ 获取浏览器驱动位置 """
        return self._path or ""

    @path.setter
    def path(self, value: str) -> None:
        self._path = str(value)

    def start(self) -> None:
        """
        开始服务
        抛出：当服务无法正常启动或者，找不到服务驱动的路径时会抛出异常 WebDriverException
        """
        if self._path is None:
            raise WebDriverException("Service path cannot be None.")
        self._start_process(self._path)

        count = 0
        while True:
            self.assert_process_still_running()
            if self.is_connectable():
                break
            # 等待链接，等待时间随着尝试链接次数增加而增加
            sleep(min(0.01 + 0.05 * count, 0.5))
            count += 1
            if count == 70:
                raise WebDriverException(f"Can not connect to the Service {self._path}")

    def assert_process_still_running(self) -> None:
        """检查底层服务是否还在运行，如果底层服务进程推出，则抛出异常 WebDriverException 以及详细信息"""
        return_code = self.process.poll()
        if return_code:
            raise WebDriverException(f"Service {self._path} unexpectedly exited. Status code was: {return_code}")

    def is_connectable(self) -> bool:
        """建立套接字连接，以确定在该端口上运行的服务是否可访问"""
        return utils.is_connectable(self.port)

    def send_remote_shutdown_command(self) -> None:
        """向该服务的关闭端点发送一个 HTTP 请求，以尝试停止该服务"""
        try:
            request.urlopen(f"{self.service_url}/shutdown")
        except URLError:
            return

        for _ in range(30):
            if not self.is_connectable():
                break
            sleep(1)

    def stop(self) -> None:
        """停止服务"""

        if self.log_output not in {PIPE, subprocess.DEVNULL}:
            if isinstance(self.log_output, IOBase):
                self.log_output.close()
            elif isinstance(self.log_output, int):
                os.close(self.log_output)

        if self.process is not None and self.process.poll() is None:
            try:
                self.send_remote_shutdown_command()
            except TypeError:
                pass
            finally:
                self._terminate_process()

    def _terminate_process(self) -> None:
        """
        终止子进程。
		在 POSIX 系统上，这会先尝试发送优雅的 SIGTERM 信号，然后发送 SIGKILL 信号；
		在 Windows 操作系统上，kill 是 terminate 的别名。如果出现问题，终止操作本身不会报错，而是（目前）在此处默默地忽略错误。
        """
        try:
            stdin, stdout, stderr = (
                self.process.stdin,
                self.process.stdout,
                self.process.stderr,
            )
            for stream in stdin, stdout, stderr:
                try:
                    stream.close()
                except AttributeError:
                    pass
            self.process.terminate()
            try:
                self.process.wait(60)
            except subprocess.TimeoutExpired:
                logger.error(
                    "Service process refused to terminate gracefully with SIGTERM, escalating to SIGKILL.",
                    exc_info=True,
                )
                self.process.kill()
        except OSError:
            logger.error("Error terminating service process.", exc_info=True)

    def __del__(self) -> None:
        try:
            self.stop()
        except Exception:
            pass

    def _start_process(self, path: str) -> None:
        """
        通过执行所提供的命令来创建子进程。
		参数：
		path：要执行的完整命令
        """
        cmd = [path]
        cmd.extend(self.command_line_args())
        close_file_descriptors = self.popen_kw.pop("close_fds", sys.platform != "win32")
        try:
            start_info = None
            if sys.platform == "win32":
                start_info = subprocess.STARTUPINFO()
                start_info.dwFlags = subprocess.CREATE_NEW_CONSOLE | subprocess.STARTF_USESHOWWINDOW
                start_info.wShowWindow = subprocess.SW_HIDE

            self.process = subprocess.Popen(
                cmd,
                env=self.env,
                close_fds=close_file_descriptors,
                stdout=cast(Optional[Union[int, IO[Any]]], self.log_output),
                stderr=cast(Optional[Union[int, IO[Any]]], self.log_output),
                stdin=PIPE,
                creationflags=self.creation_flags,
                startupinfo=start_info,
                **self.popen_kw,
            )
            logger.debug(
                "Started executable: `%s` in a child process with pid: %s using %s to output %s",
                self._path,
                self.process.pid,
                self.creation_flags,
                self.log_output,
            )
        except TypeError:
            raise
        except OSError as err:
            if err.errno == errno.EACCES:
                if self._path is None:
                    raise WebDriverException("Service path cannot be None.")
                raise WebDriverException(
                    f"'{os.path.basename(self._path)}' executable may have wrong permissions."
                ) from err
            raise

    def env_path(self) -> Optional[str]:
        if self.DRIVER_PATH_ENV_KEY:
            return os.getenv(self.DRIVER_PATH_ENV_KEY, None)
        return None

```

## 使用示例

```python
"""
Service 的学习
"""
from selenium import webdriver

# 创建服务实例
service = webdriver.EdgeService(executable_path="D:/Development/web_Driver/msedgedriver.exe", port=8080)

# path => 驱动文件路径
print(service.path)

# port => 服务运行所在端口
print(service.port)

# 日志输出模式
"""
log_output = -3
-3  丢弃日志
-2  日志输出至浏览器控制台
-1  日志输出到标准错误
非负数 日志输出到指定路径
"""
print(service.log_output)

# env => 系统环境变量
print(service.env)

# env_path => driver_path_env_key 所指定的路径或从环境变量名为 SE_EDGEDRIVER 中取值
print(service.env_path())

# 创建驱动实例
# driver = webdriver.Edge(service=service)

```

这里日志部分并没有讲述完整，不要急后面再各个浏览器部分会补充完整（请观看 *09_浏览器*）。