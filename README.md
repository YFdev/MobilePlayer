# MobilePlayer
note:
	1、使用handler实现activity延迟跳转；
	2、重写activity的onTouchEvent方法，实现点击后立即跳转
	3、避免点击时启动多个activity实例，可使用singleTask或使用标志位判断是否已经启动过
	4、RadioButton的使用
	5、提取控件的公共属性，放在style里面，便于更改，并使代码简洁
	6、封装titleBar，shape、style、selector使用
	7、fragment使用