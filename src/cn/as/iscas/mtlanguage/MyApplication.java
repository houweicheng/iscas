package cn.as.iscas.mtlanguage;

import org.xutils.x;

import android.app.Application;

/**
 * Ϊ�˳�ʼ��xUtils3�Զ���̳���Application����
 * @author ��ΰ��
 *
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// �������ʼ��xUtils���
		x.Ext.init(this);
		//xUtils3��ܵ�Debugģʽ
		x.Ext.setDebug(BuildConfig.DEBUG);
	}
	
}
