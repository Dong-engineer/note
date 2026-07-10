BeautifulSoup库

### 一、初始化与解析

| 方法 / 属性                         | 功能描述                                                     | 示例代码                                                     |
| ----------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `BeautifulSoup(html_doc, 'parser')` | 创建 BeautifulSoup 对象，解析 HTML/XML 文档。 支持的解析器：`'html.parser'`、`'lxml'`、`'html5lib'` | `soup = BeautifulSoup('<html>...</html>', 'html.parser')`    |
| `BeautifulSoup.from_encoding()`     | 指定编码解析文档（处理乱码问题）                             | `soup = BeautifulSoup(html_bytes, 'lxml', from_encoding='utf-8')` |

### 二、元素查找（导航树操作）

| 方法 / 属性        | 功能描述                                     | 示例代码                                                     |
| ------------------ | -------------------------------------------- | ------------------------------------------------------------ |
| **标签名直接访问** | 获取文档中第一个匹配的标签                   | `soup.title` → 返回 `<title>...</title>` 标签对象            |
| **`find()`**       | 查找第一个匹配的标签                         | `soup.find('p', class_='intro')` → 返回第一个 `<p class="intro">` 标签 |
| **`find_all()`**   | 查找所有匹配的标签，返回列表                 | `soup.find_all('a', href=True)` → 返回所有带 `href` 属性的 `<a>` 标签 |
| **`select_one()`** | 通过 CSS 选择器查找第一个匹配的元素          | `soup.select_one('div.content > p')` → 返回第一个 `div.content` 下的 `<p>` |
| **`select()`**     | 通过 CSS 选择器查找所有匹配的元素，返回列表0 | `soup.select('ul li.active')` → 返回所有 `<ul>` 下的 `<li class="active">` |
| **属性查找**       | 通过 `attrs` 参数查找标签                    | `soup.find('div', attrs={'data-id': '123'})` → 返回带 `data-id="123"` 的 `<div>` |

### 三、元素内容与属性获取

| 方法 / 属性                | 功能描述                                            | 示例代码                                                     |
| -------------------------- | --------------------------------------------------- | ------------------------------------------------------------ |
| **`tag.string`**           | 获取标签内的文本内容（仅限单一子节点）              | `<title>Hello</title>` → `soup.title.string` → `'Hello'`     |
| **`tag.get_text()`**       | 获取标签内的所有文本（包含嵌套标签），可指定分隔符  | `<p>Hello <b>World</b></p>` → `soup.p.get_text()` → `'Hello World'` |
| **`tag.attrs`**            | 获取标签的所有属性，返回字典                        | `<a href="/">Home</a>` → `soup.a.attrs` → `{'href': '/'}`    |
| **`tag['attr_name']`**     | 获取标签的单个属性值                                | `<img src="logo.png">` → `soup.img['src']` → `'logo.png'`    |
| **`tag.get('attr_name')`** | 安全获取标签的单个属性值（属性不存在时返回 `None`） | `soup.a.get('href')` → 等价于 `soup.a['href']`，但更安全     |

### 四、元素导航（父子 / 兄弟关系）

| 方法 / 属性        | 功能描述                                                     | 示例代码                                                     |
| ------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **父节点**         | `tag.parent` → 直接父节点 `tag.parents` → 所有祖先节点的生成器 | `soup.p.parent` → 返回 `<p>` 的父标签                        |
| **子节点**         | `tag.contents` → 直接子节点列表 `tag.children` → 直接子节点生成器 | `soup.body.contents` → 返回 `<body>` 内的所有直接子节点      |
| **所有子孙节点**   | `tag.descendants` → 递归获取所有子孙节点的生成器             | `for child in soup.body.descendants: ...` → 遍历 `<body>` 内的所有节点 |
| **兄弟节点**       | `tag.next_sibling` / `tag.previous_sibling` → 下一个 / 上一个兄弟节点 | `soup.li.next_sibling` → 返回 `<li>` 的下一个兄弟标签        |
| **同级节点迭代器** | `tag.next_siblings` / `tag.previous_siblings` → 所有后续 / 前续兄弟节点的生成器 | `for sibling in soup.li.next_siblings: ...` → 遍历后续所有 `<li>` 兄弟节点 |

### 五、元素修改（文档操作）

| 方法 / 属性      | 功能描述                                                     | 示例代码                                                     |
| ---------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **修改标签内容** | `tag.string = 'new text'` → 替换标签内的文本                 | `soup.title.string = 'New Title'` → 修改 `<title>` 标签的内容 |
| **添加子节点**   | `tag.append(new_tag)` → 在标签末尾添加子节点                 | `new_tag = soup.new_tag('p')` `soup.body.append(new_tag)` → 在 `<body>` 末尾添加 `<p>` |
| **插入子节点**   | `tag.insert(index, new_tag)` → 在指定位置插入子节点          | `soup.head.insert(0, new_tag)` → 在 `<head>` 的第 1 个位置插入新标签 |
| **替换节点**     | `old_tag.replace_with(new_tag)` → 用新节点替换旧节点         | `soup.a.replace_with(soup.new_tag('span'))` → 将 `<a>` 替换为 `<span>` |
| **删除节点**     | `tag.decompose()` → 移除标签及其内容 `tag.extract()` → 移除标签但保留内容 | `soup.script.decompose()` → 删除所有 `<script>` 标签         |

### 六、特殊内容处理

| 方法 / 属性           | 功能描述                                             | 示例代码                                                     |
| --------------------- | ---------------------------------------------------- | ------------------------------------------------------------ |
| **处理注释**          | `Comment` 对象表示 HTML 注释                         | `<p>Text <!-- Comment --></p>` → `soup.p.contents[1]` → 返回注释对象 |
| **处理 XML 命名空间** | 在 XML 文档中通过 `tag['xmlns']` 访问命名空间属性    | `<ns:tag xmlns:ns="http://example.com">` → `soup.tag['xmlns']` → `'http://example.com'` |
| **处理转义字符**      | BeautifulSoup 自动处理 HTML 转义字符（如 `<` → `<`） | 解析后直接使用 `tag.string` 即可获得解码后的文本             |

### 七、编码与输出

| 方法 / 属性           | 功能描述                                          | 示例代码                                                     |
| --------------------- | ------------------------------------------------- | ------------------------------------------------------------ |
| **`soup.prettify()`** | 返回格式化的 HTML/XML 字符串（缩进美观）          | `print(soup.prettify())` → 打印美化后的文档                  |
| **`soup.encode()`**   | 将文档转换为字节串，指定编码（默认 UTF-8）        | `html_bytes = soup.encode('utf-8')` → 返回 UTF-8 编码的字节串 |
| **`soup.decode()`**   | 将文档转换为 Unicode 字符串（等价于 `str(soup)`） | `html_str = soup.decode()` → 返回 Unicode 字符串             |

### 八、其他实用功能

| 方法 / 属性                        | 功能描述                     | 示例代码                                                     |
| ---------------------------------- | ---------------------------- | ------------------------------------------------------------ |
| **`soup.new_tag(name, attrs={})`** | 创建新的标签对象             | `new_tag = soup.new_tag('a', href='#')` → 创建 `<a href="#">` 标签 |
| **`soup.new_string(text)`**        | 创建新的文本节点             | `new_text = soup.new_string('Hello')` → 创建文本节点 `'Hello'` |
| **`soup.find_next(string)`**       | 查找下一个包含指定文本的节点 | `soup.find_next(string='Next Page')` → 查找下一个包含 "Next Page" 的节点 |
| **`soup.find_previous(string)`**   | 查找上一个包含指定文本的节点 | `soup.find_previous(string='Previous')` → 查找上一个包含 "Previous" 的节点 |

### 九、常见场景示例

| 场景                   | 代码示例                                                     |
| ---------------------- | ------------------------------------------------------------ |
| **获取所有链接的 URL** | `urls = [a['href'] for a in soup.find_all('a', href=True)]`  |
| **提取所有图片的 src** | `img_srcs = [img['src'] for img in soup.find_all('img')]`    |
| **获取表格数据**       | `for row in soup.table.find_all('tr'):<br> cols = row.find_all('td')<br> data = [col.get_text(strip=True) for col in cols]<br> print(data)` |
| **处理动态加载的内容** | BeautifulSoup 只能解析静态 HTML，动态内容需配合 Selenium 等工具先渲染页面 |