# Actions

`Actions` 是一个接口，该接口模拟用户的硬件设备输入。

在之前的元素交互中，学习了 `send_keys()` 用于向元素输入内容，在本节你会学习到一些关于 `selenium` 是如何向浏览器模拟用户使用硬件设备的输入操作。

## 原理

在源码，开发者很难发现 `selenium` 是如何完成这些操作的，在之前讲述过 `selenium` 是如何完成元素交互的，`Actions` 的原理与其差不多，但并非完全一样，**其都是基于 `WebDriver` 和浏览器内核的通信，`Actions` 是通过 W3C 的动作序列接口模拟用户的输入操作来完成的，其向目标元素赋值的过程中是相当于实实在在敲击键盘输入的**。

## 使用示例

### 按下与释放按键（键盘）

```python
ActionChains(driver)\
	.key_down(Keys.SHIFT)\
	.send_keys("abc")\
	.perform()
```

```py
ActionChains(driver)\
	.key_down(Keys.SHIFT)\
	.send_keys("a")\
	.key_up(Keys.SHIFT)\
	.send_keys("b")\
	.perform()
```

### 一次性输入（键盘）

```python
ActionChains(driver)\
	.send_keys("abc")\
	.perform()
```

### 指定元素的一次性输入（键盘）

```python
text_input = driver.find_element(By.ID, "textInput")
ActionChains(driver)\
	.send_keys_to_element(text_input, "abc")\
	.perform()
```

### 点击左键（鼠标）

```python
clickable = driver.find_element(By.ID, "click")
ActionChains(driver) \
	.click(clickable) \  # 点击左键
	.perform()
```

### 按住左键（鼠标）

```python
clickable = driver.find_element(By.ID, "clickable")
ActionChains(driver) \
	.click_and_hold(clickable) \  # 按住左键
	.perform()
```

### 点击右键（鼠标）

```python
clickable = driver.find_element(By.ID, "clickable")
ActionChains(driver) \
	.context_click(clickable) \  # 点击右键
	.perform()
```

### 鼠标侧键（鼠标）

```py
action = ActionBuilder(driver)
# 鼠标侧键的回退键
action.pointer_action.pointer_down(MouseButton.BACK)
action.pointer_action.pointer_up(MouseButton.BACK)

action.perform()



action = ActionBuilder(driver)
# 鼠标侧键的前进键
action.pointer_action.pointer_down(MouseButton.FORWARD)
action.pointer_action.pointer_up(MouseButton.FORWARD)

action.perform()
```

### 双击左键

```python
clickable = driver.find_element(By.ID, "clickable")
ActionChains(driver) \
	.double_click(clickable) \
	.perform()
```

### 移动鼠标至目标元素

```python
hoverable = driver.find_element(By.ID, "hover")
ActionChains(driver) \
    .move_to_element(hoverable) \
    .perform()
```

### 移动鼠标（从当前位置移动）

```python
ActionChains(driver) \
    .move_by_offset(13, 15) \
    .perform()
```

### 移动鼠标（从目标元素中心点移动）

```py
mouse_tracker = driver.find_element(By.ID, "mouse-tracker")
ActionChains(driver) \
    .move_to_element_with_offset(mouse_tracker, 8, 0) \
    .perform()
```

### 拖放元素

```py
draggable = driver.find_element(By.ID, "draggable")
droppable = driver.find_element(By.ID, "droppable")
# 将 "draggable" 元素移动至 "droppable"
ActionChains(driver) \
    .drag_and_drop(draggable, droppable) \
    .perform()
```

### 拖放元素（通过偏移量）

```py
draggable = driver.find_element(By.ID, "draggable")
start = draggable.location
finish = driver.find_element(By.ID, "droppable").location
ActionChains(driver) \
    .drag_and_drop_by_offset(draggable, finish['x'] - start['x'], finish['y'] - start['y']) \
    .perform()
```

### 按目标元素位置窗口滚动

```py
iframe = driver.find_element(By.TAG_NAME, "iframe")

# 窗口滚动至目标元素位置，元素位于窗口最底部
ActionChains(driver)\
    .scroll_to_element(iframe)\ 
    .perform()
```

### 按数值窗口滚动

```py
# 获取目标元素的高度位置
footer = driver.find_element(By.TAG_NAME, "footer")
delta_y = footer.rect['y']

# 执行窗口滚动操作
ActionChains(driver)\
    .scroll_by_amount(0, delta_y)\
    .perform()
```



## 注意事项

1. 在鼠标的操作中，所有通过偏移量进行操作的方法，都是基于开发者实时的可视窗口进行操作的，开发者需要自行滚动窗口（大部分浏览器是支持自动滚动的，但部分场景（如精准坐标操作）可能失败）。建议开发者在对元素进行拖放操作时，一定要保证在实时的可视窗口中。
2. 以上内容对于仅对 windows 系统可用，其余操作系统开发者需要自行根据其操作系统修改参数使用。
3. 以上所有的窗口滚动操作仅对于 Chromium 内核浏览器有效。

