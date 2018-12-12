# MobilePlayer
note:
	1、使用handler实现activity延迟跳转；
	2、重写activity的onTouchEvent方法，实现点击后立即跳转
	3、避免点击时启动多个activity实例，可使用singleTask或使用标志位判断是否已经启动过
	4、RadioButton的使用
	5、提取控件的公共属性，放在style里面，便于更改，并使代码简洁
	6、封装titleBar，shape、style、selector使用
	7、fragment使用
	8、listView使用
	9、VideoView使用，获取视频原始宽高，自定义videoView（重写onMeasure），设置全屏或默认大小（全屏使用DisplayMetrics）
	10、Parcelable使用（intent传递对象）
	11、seekbar使用（关联视屏长度，seekTo方法）
	12、广播的使用，获取电量广播
	13、CP使用（获取本地音乐及视频列表）
	14、GestrueDetector使用（接管onTouchEvent），双击、单击、长按监听
	15、onTouchEvent中滑动改变音量，AudioManager获取/设置当前音量以及最大音量，flags标志位：0表示不改变系统音量，1表示同时改变系统音量
	16、onKeyDown中监听物理键调节音量