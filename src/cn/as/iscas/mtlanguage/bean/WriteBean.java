package cn.as.iscas.mtlanguage.bean;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;

/**
 * 书写的实体类，存储了字形以及轨迹，单例模式保证唯一
 * @author 侯伟成
 *
 */
public class WriteBean {
	/**
	 * 单例的实体
	 */
	static private WriteBean instance;

	static {
		instance = new WriteBean();
	}

	/**
	 * 字符
	 */
	char ch;

	/**
	 * 获取当前字符
	 * @return 当前字符
	 */
	public char getCh() {
		return ch;
	}

	/**
	 * 设置当前字符
	 * @param ch 要设置的字符
	 */
	public void setCh(char ch) {
		this.ch = ch;
	}

	/**
	 * 获取当前字符的Unicode编码字符串
	 * @return Unicode编码字符串
	 */
	public String getChUnicode() {
		return Integer.toHexString(ch);
	}

	/**
	 * 开始一个新的笔画
	 */
	public void startNewStoke() {
		if (!track.isEmpty()) {
			stokes.add(track);
			track = new ArrayList<PointF>();
		}
	}

	/**
	 * 当前笔画
	 */
	public List<PointF> track;

	/**
	 * 按笔画存储的触摸轨迹
	 */
	public List<List<PointF>> stokes;

	/**
	 * 私有化构造函数形成单例模式
	 */
	private WriteBean() {
		ch = 'a';
		track = new ArrayList<PointF>();
		stokes = new ArrayList<List<PointF>>();
	}

	/**
	 * 获取当前类的实体
	 * @return 类的实体
	 */
	public static WriteBean getInstance() {
		return instance;
	}
}
