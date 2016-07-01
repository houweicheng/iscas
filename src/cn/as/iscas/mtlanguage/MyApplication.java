package cn.as.iscas.mtlanguage;

import org.xutils.x;

import android.app.Application;

/**
 * 为了初始化xUtils3自定义继承自Application的类
 * @author 侯伟成
 *
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 在这里初始化xUtils框架
		x.Ext.init(this);
		//xUtils3框架的Debug模式
		x.Ext.setDebug(BuildConfig.DEBUG);
	}
	
}
