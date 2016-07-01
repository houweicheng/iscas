package cn.as.iscas.mtlanguage.utils;

import java.text.DecimalFormat;
import java.util.List;

import android.graphics.PointF;

/**
 * 工具类用于将各种实体编码
 * @author 侯伟成
 *
 */
public class DecodeUtils {

	/**
	 * 将Point的List编码成字符串
	 * @param points
	 * @return 编码成功的字符串
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
	 * 将按笔画存储的触摸轨迹编码成字符串
	 * @param stokes 当前触摸轨迹 
	 * @return 编码成功的字符串
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
