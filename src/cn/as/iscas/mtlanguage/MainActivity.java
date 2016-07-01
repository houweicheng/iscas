package cn.as.iscas.mtlanguage;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.as.iscas.mtlanguage.bean.WriteBean;
import cn.as.iscas.mtlanguage.utils.DecodeUtils;

/**
 * 
 * @author 侯伟成
 * 
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends Activity{

	/**
	 * 藏文
	 */
	private Typeface tfTibetan;
	/**
	 * 蒙古文
	 */
	private Typeface tfMongolian;
	/**
	 * 维吾尔文
	 */
	private Typeface tfUyghur;
	/**
	 * 显示的字符
	 */
	private TextView TvChar1;
	/**
	 * 书写的区域
	 */
	private ImageView IvWrite;
	/**
	 * 用于显示的后台bitmap
	 */
	private Bitmap baseBitmap;
	/**
	 * 用于前台显示的画布
	 */
	private Canvas canvasIv;
	/**
	 * 画笔
	 */
	private Paint paint;
	/**
	 * 用于记录点的坐标X
	 */
	private float startX;
	/**
	 * 用于记录点的坐标Y
	 */
	private float startY;
	/**
	 * 存储数据的实体类对象
	 */
	private WriteBean dataBean;
	/**
	 * 语言选择控件
	 */
	private Spinner SpLanguageType;
	/**
	 * 应用上下文
	 */
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		mContext = this.getApplicationContext();

		dataBean = WriteBean.getInstance();

		// 找到需要使用的控件
		TvChar1 = (TextView) findViewById(R.id.TvChar1);
		IvWrite = (ImageView) findViewById(R.id.IvWrite);
		SpLanguageType = (Spinner) findViewById(R.id.SpLanguageType);

		// 从Assets中读取字体文件
		tfTibetan = Typeface.createFromAsset(getAssets(),
				"fonts/Jomolhari-alpha3c-0605331.ttf");
		tfMongolian = Typeface.createFromAsset(getAssets(),
				"fonts/monbaiti.ttf");
		tfUyghur = Typeface.createFromAsset(getAssets(),
				"fonts/ALKATIP Tor.TTF");

		TvChar1.setTypeface(tfTibetan);

		TvChar1.setText(Character.toString(dataBean.getCh()));

		paint = new Paint();
		paint.setStrokeWidth(5);
		paint.setColor(Color.BLACK);

		startX = 0;
		startY = 0;

		// 根据所选的语言随机选取字符默认为藏文
		int a = 0xF300;
		a += (int) (Math.random() * 219);
		char b = (char) a;
		dataBean.setCh(b);
		TvChar1.setTypeface(tfTibetan);
		TvChar1.setText(Character.toString(dataBean.getCh()));
	}

	/**
	 * 触摸事件的响应，在iv上绘制触摸笔记
	 * @param v 该控件
	 * @param event 触摸事件
	 * @return 该处理是否消费了该事件
	 */
	@Event(value = R.id.IvWrite, type = View.OnTouchListener.class)
	private boolean onIvWriteTouchEvent(View v, MotionEvent event) {
		PointF temp;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (baseBitmap == null) {
				clearCanvas();
			}
			startX = event.getX();
			startY = event.getY();
			// 记录触摸起点
			temp = new PointF(startX, startY);
			dataBean.track.add(temp);
			break;
		case MotionEvent.ACTION_MOVE:
			// 这里完成绘制曲线并记录曲线
			float stopX,
			stopY;
			stopX = event.getX();
			stopY = event.getY();
			canvasIv.drawLine(startX, startY, stopX, stopY, paint);
			startX = stopX;
			startY = stopY;
			// 记录曲线
			temp = new PointF(startX, startY);
			dataBean.track.add(temp);
			IvWrite.setImageBitmap(baseBitmap);
			break;
		case MotionEvent.ACTION_UP:
			dataBean.startNewStoke();
			break;
		default:
			break;
		}
		return true;
	}

	private void clearCanvas() {
		if (baseBitmap != null) {
			baseBitmap = null;
		}
		baseBitmap = Bitmap.createBitmap(IvWrite.getWidth(),
				IvWrite.getHeight(), Bitmap.Config.ARGB_8888);
		canvasIv = new Canvas(baseBitmap);
		canvasIv.drawColor(Color.rgb(224, 224, 224));
		IvWrite.setImageBitmap(baseBitmap);
	}

	/**
	 * 选择一种语言后显示该种语言字符
	 * 
	 * @param arg0
	 *            该控件所使用数据适配器
	 * @param arg1
	 *            该控件
	 * @param pos
	 *            选择item的位置
	 * @param arg3
	 *            选择item的位置
	 */
	@Event(value = R.id.SpLanguageType, type = AdapterView.OnItemSelectedListener.class)
	private void onSpLanguageTypeItemSelectedEvent(AdapterView<?> arg0,
			View arg1, int pos, long arg3) {
		getNewChar(pos);
	}

	/**
	 * 显示一个新的字符
	 * 
	 * @param pos
	 *            语言的种类
	 */
	private void getNewChar(int pos) {
		switch (pos) {
		case 0:
			// 选择的是藏文(本地)
			int a = 0xF300;
			a += (int) (Math.random() * 219);
			char b = (char) a;
			dataBean.setCh(b);
			TvChar1.setTypeface(tfTibetan);
			TvChar1.setText(Character.toString(dataBean.getCh()));
			break;
		case 1:
			// 选择的是蒙古文
			int a1 = 0x1810;
			a1 += (int) (Math.random() * 168);
			char b1 = (char) a1;
			dataBean.setCh(b1);
			TvChar1.setTypeface(tfMongolian);
			TvChar1.setText(Character.toString(dataBean.getCh()));
			break;
		case 2:
			// 选择的是维吾尔文
			int a2 = 0xFBD1;
			a2 += (int) (Math.random() * 42);
			char b2 = (char) a2;
			dataBean.setCh(b2);
			TvChar1.setTypeface(tfUyghur);
			TvChar1.setText(Character.toString(dataBean.getCh()));
			break;

		default:
			break;
		}
		clearCanvas();
		dataBean.stokes.clear();
	}

	private void showMessage(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 点击取消清除bean和canvas的bitmap
	 * 
	 * @param view
	 *            该控件
	 */
	@Event(value = R.id.BtCancel, type = View.OnClickListener.class)
	private void onCancelClick(View view) {
		clearCanvas();
		dataBean.stokes.clear();
	}

	/**
	 * 点击确认上传记录的数据
	 * 
	 * @param view
	 *            该控件
	 */
	@Event(value = R.id.BtConfirm, type = View.OnClickListener.class)
	private void onConfirmClick(View view) {
		RequestParams params = new RequestParams(
				this.getString(R.string.server_put_data));
		params.addBodyParameter("code", dataBean.getChUnicode());
		params.addBodyParameter("track",
				DecodeUtils.convertStokes2String(dataBean.stokes));
		x.http().get(params, new CommonCallback<JSONObject>() {

			@Override
			public void onCancelled(CancelledException arg0) {
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				showMessage("网络连接错误");
			}

			@Override
			public void onFinished() {
			}

			@Override
			public void onSuccess(JSONObject rlt) {
				try {
					if (rlt.getString("result").equals("success")) {
						showMessage("成功提交");
					}
				} catch (JSONException e) {
					// 服务器返回格式无法解析
					showMessage("服务器异常");
				}
				getNewChar(SpLanguageType.getSelectedItemPosition());
			}
		});
	}

	/**
	 * 点击上一笔，删除最后一个笔画并重绘界面
	 * 
	 * @param view
	 *            该控件
	 */
	@Event(value = R.id.BtDelStroke, type = View.OnClickListener.class)
	private void onDelStrokeClick(View view) {
		if (!dataBean.stokes.isEmpty())
			dataBean.stokes.remove(dataBean.stokes.size() - 1);
		clearCanvas();
		// 将前面的笔画重新绘制
		float startX = 0, startY = 0;
		float stopX, stopY;
		for (List<PointF> stoke : dataBean.stokes) {
			for (PointF point : stoke) {
				if (startX == 0 && startY == 0) {
					startX = point.x;
					startY = point.y;
				} else {
					stopX = point.x;
					stopY = point.y;
					canvasIv.drawLine(startX, startY, stopX, stopY, paint);
					startX = stopX;
					startY = stopY;
					IvWrite.setImageBitmap(baseBitmap);
				}
			}
			startX = 0;
			startY = 0;
		}
	}
}
