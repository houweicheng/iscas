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
 * @author ��ΰ��
 * 
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends Activity{

	/**
	 * ����
	 */
	private Typeface tfTibetan;
	/**
	 * �ɹ���
	 */
	private Typeface tfMongolian;
	/**
	 * ά�����
	 */
	private Typeface tfUyghur;
	/**
	 * ��ʾ���ַ�
	 */
	private TextView TvChar1;
	/**
	 * ��д������
	 */
	private ImageView IvWrite;
	/**
	 * ������ʾ�ĺ�̨bitmap
	 */
	private Bitmap baseBitmap;
	/**
	 * ����ǰ̨��ʾ�Ļ���
	 */
	private Canvas canvasIv;
	/**
	 * ����
	 */
	private Paint paint;
	/**
	 * ���ڼ�¼�������X
	 */
	private float startX;
	/**
	 * ���ڼ�¼�������Y
	 */
	private float startY;
	/**
	 * �洢���ݵ�ʵ�������
	 */
	private WriteBean dataBean;
	/**
	 * ����ѡ��ؼ�
	 */
	private Spinner SpLanguageType;
	/**
	 * Ӧ��������
	 */
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		mContext = this.getApplicationContext();

		dataBean = WriteBean.getInstance();

		// �ҵ���Ҫʹ�õĿؼ�
		TvChar1 = (TextView) findViewById(R.id.TvChar1);
		IvWrite = (ImageView) findViewById(R.id.IvWrite);
		SpLanguageType = (Spinner) findViewById(R.id.SpLanguageType);

		// ��Assets�ж�ȡ�����ļ�
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

		// ������ѡ���������ѡȡ�ַ�Ĭ��Ϊ����
		int a = 0xF300;
		a += (int) (Math.random() * 219);
		char b = (char) a;
		dataBean.setCh(b);
		TvChar1.setTypeface(tfTibetan);
		TvChar1.setText(Character.toString(dataBean.getCh()));
	}

	/**
	 * �����¼�����Ӧ����iv�ϻ��ƴ����ʼ�
	 * @param v �ÿؼ�
	 * @param event �����¼�
	 * @return �ô����Ƿ������˸��¼�
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
			// ��¼�������
			temp = new PointF(startX, startY);
			dataBean.track.add(temp);
			break;
		case MotionEvent.ACTION_MOVE:
			// ������ɻ������߲���¼����
			float stopX,
			stopY;
			stopX = event.getX();
			stopY = event.getY();
			canvasIv.drawLine(startX, startY, stopX, stopY, paint);
			startX = stopX;
			startY = stopY;
			// ��¼����
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
	 * ѡ��һ�����Ժ���ʾ���������ַ�
	 * 
	 * @param arg0
	 *            �ÿؼ���ʹ������������
	 * @param arg1
	 *            �ÿؼ�
	 * @param pos
	 *            ѡ��item��λ��
	 * @param arg3
	 *            ѡ��item��λ��
	 */
	@Event(value = R.id.SpLanguageType, type = AdapterView.OnItemSelectedListener.class)
	private void onSpLanguageTypeItemSelectedEvent(AdapterView<?> arg0,
			View arg1, int pos, long arg3) {
		getNewChar(pos);
	}

	/**
	 * ��ʾһ���µ��ַ�
	 * 
	 * @param pos
	 *            ���Ե�����
	 */
	private void getNewChar(int pos) {
		switch (pos) {
		case 0:
			// ѡ����ǲ���(����)
			int a = 0xF300;
			a += (int) (Math.random() * 219);
			char b = (char) a;
			dataBean.setCh(b);
			TvChar1.setTypeface(tfTibetan);
			TvChar1.setText(Character.toString(dataBean.getCh()));
			break;
		case 1:
			// ѡ������ɹ���
			int a1 = 0x1810;
			a1 += (int) (Math.random() * 168);
			char b1 = (char) a1;
			dataBean.setCh(b1);
			TvChar1.setTypeface(tfMongolian);
			TvChar1.setText(Character.toString(dataBean.getCh()));
			break;
		case 2:
			// ѡ�����ά�����
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
	 * ���ȡ�����bean��canvas��bitmap
	 * 
	 * @param view
	 *            �ÿؼ�
	 */
	@Event(value = R.id.BtCancel, type = View.OnClickListener.class)
	private void onCancelClick(View view) {
		clearCanvas();
		dataBean.stokes.clear();
	}

	/**
	 * ���ȷ���ϴ���¼������
	 * 
	 * @param view
	 *            �ÿؼ�
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
				showMessage("�������Ӵ���");
			}

			@Override
			public void onFinished() {
			}

			@Override
			public void onSuccess(JSONObject rlt) {
				try {
					if (rlt.getString("result").equals("success")) {
						showMessage("�ɹ��ύ");
					}
				} catch (JSONException e) {
					// ���������ظ�ʽ�޷�����
					showMessage("�������쳣");
				}
				getNewChar(SpLanguageType.getSelectedItemPosition());
			}
		});
	}

	/**
	 * �����һ�ʣ�ɾ�����һ���ʻ����ػ����
	 * 
	 * @param view
	 *            �ÿؼ�
	 */
	@Event(value = R.id.BtDelStroke, type = View.OnClickListener.class)
	private void onDelStrokeClick(View view) {
		if (!dataBean.stokes.isEmpty())
			dataBean.stokes.remove(dataBean.stokes.size() - 1);
		clearCanvas();
		// ��ǰ��ıʻ����»���
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
