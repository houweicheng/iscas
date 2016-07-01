package cn.as.iscas.mtlanguage.utils;

import java.text.DecimalFormat;
import java.util.List;

import android.graphics.PointF;

/**
 * ���������ڽ�����ʵ�����
 * @author ��ΰ��
 *
 */
public class DecodeUtils {

	/**
	 * ��Point��List������ַ���
	 * @param points
	 * @return ����ɹ����ַ���
	 */
	static public String convertPoint2String(List<PointF> points) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		DecimalFormat df = new DecimalFormat("0.0");
		for (PointF point : points) {
			sb.append(df.format(point.x));
			sb.append(", ");
			sb.append(df.format(point.x));
			sb.append(", ");
		}
		sb.append("-1.0, -1.0]");
		return sb.toString();
	}

	/**
	 * �����ʻ��洢�Ĵ����켣������ַ���
	 * @param stokes ��ǰ�����켣 
	 * @return ����ɹ����ַ���
	 */
	static public String convertStokes2String(List<List<PointF>> stokes) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		DecimalFormat df = new DecimalFormat("0.0");
		for (List<PointF> stoke : stokes) {
			for (PointF point : stoke) {
				sb.append(df.format(point.x));
				sb.append(", ");
				sb.append(df.format(point.x));
				sb.append(", ");
			}
			sb.append("-1.0, 0.0, ");
		}
		sb.append("-1.0, -1.0]");
		return sb.toString();
	}
}
