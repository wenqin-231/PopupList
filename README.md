# PopupList

### Feature

* 完全由**PopupWindow**实现
* 支持弹性打开菜单列表
* 支持点击列表不同位置弹出不同的菜单
* 支持弹出背景变灰色，列表消失后列表背景恢复
* 支持列表圆角（列表子项设置点击效果后圆角不受影响）

### Demo

![](https://github.com/wenqin-231/PopupList/blob/master/art/popup_window.gif?raw=true)

### More

这是为前公司的项目做的列表菜单，参考了即刻、支付宝、Bilibili等客户端的实现效果。

其中技术的麻烦点主要集中在

* 弹出动画效果不佳。与即刻的弹出动画相比，总觉得我的实现方案好像差了点什么，反反复复看了即刻的弹出框后才明白，原来是他在弹出后稍稍放大了一下菜单，再恢复原本大小。这样就可以给人一种“闪亮登场”的感觉2333~ 这些产品的细节就好像乐队的贝斯，他的存在不受关注，但是没有了他音乐就少了很多感觉。

* 背景的灰度恢复得在动画结束后回调，这样`setAnimationStyle`就无法使用了（没有动画结束的监听，如果可以实现的话可以告知一下），最终的实现方案是利用属性动画和事件点击拦截机制去实现。

* 点击效果不理想。如果像以前的方式使用圆角（设置背景），那么点击事件的子View最终会覆盖父布局。我上网看了很多解决方案，比如设置padding，使用.9图片，但是没有一个比较合适。最终我发现了CardView的设置就可以解决这个问题。然而过了几天后，[GcsSloop](https://github.com/GcsSloop)发布的[**rclayout**](https://github.com/GcsSloop/rclayout)就完美解决了我的问题，他是重写了`dispatchDraw`的方法，对布局进行裁剪，为这个实现方案疯狂打Call~ 

如果有更多的想法交流，可以通过我的邮箱与我取得联系——wenqin231@gmail.com。
