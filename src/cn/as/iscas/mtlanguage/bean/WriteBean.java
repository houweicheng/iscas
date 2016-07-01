package cn.as.iscas.mtlanguage.bean;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;

/**
 * ��д��ʵ���࣬�洢�������Լ��켣������ģʽ��֤Ψһ
 * @author ��ΰ��
 *
 */
public class WriteBean {
	/**
	 * ������ʵ��
	 */
	static private WriteBean instance;

	static {
		instance = new WriteBean();
	}

	/**
	 * �ַ�
	 */
	char ch;

	/**
	 * ��ȡ��ǰ�ַ�
	 * @return ��ǰ�ַ�
	 */
	public char getCh() {
		return ch;
	}

	/**
	 * ���õ�ǰ�ַ�
	 * @param ch Ҫ���õ��ַ�
	 */
	public void setCh(char ch) {
		this.ch = ch;
	}

	/**
	 * ��ȡ��ǰ�ַ���Unicode�����ַ���
	 * @return Unicode�����ַ���
	 */
	public String getChUnicode() {
		return Integer.toHexString(ch);
	}

	/**
	 * ��ʼһ���µıʻ�
	 */
	public void startNewStoke() {
		if (!track.isEmpty()) {
			stokes.add(track);
			track = new ArrayList<PointF>();
		}
	}

	/**
	 * ��ǰ�ʻ�
	 */
	public List<PointF> track;

	/**
	 * ���ʻ��洢�Ĵ����켣
	 */
	public List<List<PointF>> stokes;

	/**
	 * ˽�л����캯���γɵ���ģʽ
	 */
	private WriteBean() {
		ch = 'a';
		track = new ArrayList<PointF>();
		stokes = new ArrayList<List<PointF>>();
	}

	/**
	 * ��ȡ��ǰ���ʵ��
	 * @return ���ʵ��
	 */
	public static WriteBean getInstance() {
		return instance;
	}
}
