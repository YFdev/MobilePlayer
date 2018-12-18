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
	17、设置播放视频时可调起自身App，<intent-filter>
	18、手机连接电脑Tomcat的配置：
		1、在pc上共享WiFi热点
		2、手机连接到pc共享的wifi
		3、开启电脑的tomcat，并且把视频放到Tomcat中
		4、查看IP地址：ipconfig
		5、把视频地址修改成无线IP地址
	19、设置监听网络视频卡顿
		1、前提：播放网络视频的时候，网络比较慢才出现
		2、Android2.3，在mediaPlayer中引入监听卡--》自定义videoview，校验播放进度判断是否监听卡。原理：当前播放进度-上一次播放进度，如果<0，则表示卡顿，否则不卡
		3、Android4.0后将监听卡封装在videoview
	20、vitamio的集成
		1、下载
		2、集成
		3、关联vitamio库:
			a、compile project(':vitamio')
			b、把功能清单文件配置拷贝：permission
			 <uses-permission android:name="android.permission.WAKE_LOCK" />
				<uses-permission android:name="android.permission.INTERNET" />
			<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
			<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />、
			配置
			<!-- Don't forgot InitActivity -->
			<activity
            android:name="io.vov.vitamio.activity.InitActivity"
            android:configChanges="orientation|screenSize|smallestScreenSize|keyboard|keyboardHidden|navigation"
            android:launchMode="singleTop"
            android:theme="@android:style/Theme.NoTitleBar"
            android:windowSoftInputMode="stateAlwaysHidden" />
		4、把系统控件复制一份，
		5、初始化vitamio库，在布局加载之前
		6、当系统播放器出错时，跳转到vitamio播放器（不首选vitamio播放器而使用系统播放器，因为vitamio播放器性能开销较大，且系统播放器播放效果好）
	21、集成jar包和so文件时
		1、可以将jar包放在libs目录下，so文件放在jniLibs目录下-->原生方式
		2、可以将jar包和so文件都放在libs目录下，但是需要在build.gradle中添加sourceSet{}块 main{ jniLibs.srcDirs = ['/../../libs']}
	22、视频播放器的两种做法
		1、直接使用VideoView
		2、在Activity中封装service和surfaceView
	23、EventBus:事件发布/订阅总线，用于替代广播、handler、intent在Fragment/activity/service/线程间传递消息，用于组件之间通信
		