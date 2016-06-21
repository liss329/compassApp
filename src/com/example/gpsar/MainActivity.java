package com.example.gpsar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
	
	static SQLiteDatabase mydb;
	int scanCount = 0;
	
	//トップ画面からの受け渡しデータ
	String sintyo;
	String place; //String型のルート番号
	int placeNo;  //Int型に変換したあとのルート番号（を格納する変数）

	int rssiVal; // 測定点数
    ArrayList<String>[][] point; //ここにデータを仮格納  [測定点数][0->ssid, 1->level];

	final int INTERVAL_PERIOD = 1000;
	Timer timer = new Timer();
	int x = 0;
	Handler handle = new Handler();
	int fpno = 0;

	private SensorManager sensorManager;
	private float[] accelerometerValues = new float[3]; // 陷会ｿｽ鬨ｾ貅ｷ�ｽｺ�ｽｦ郢ｧ�ｽｻ郢晢ｽｳ郢ｧ�ｽｵ
	private float[] magneticValues = new float[3]; // 陜ｨ�ｽｰ騾寂扱�ｽｰ蜉ｱ縺晉ｹ晢ｽｳ郢ｧ�ｽｵ
	List listMag;
	List listAcc;

	private ARView arView;

	private float oldx = 0f;
	private float oldy = 0f;
	private float oldz = 0f;

	private float dx = 0f;
	private float dy = 0f;
	private float dz = 0f;

	boolean counted = false;
	long counter = -1;
	boolean vectorUp = true;
	double oldVectorSize = 0;
	double vectorSize = 0;
	double picupVectorSize = 0;
	long changeTime = 0;
	double threshold = 15;
	double thresholdMin = 1;
	long thresholdTime = 190;
	boolean vecx = true;
	boolean vecy = true;
	boolean vecz = true;
	int vecchangecount = 0;

	float pretheta;
	float direction;
	float step;
	float drDistance = -step;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MySQLiteopenHelper SQLite = new MySQLiteopenHelper(getApplicationContext());
		mydb = SQLite.getWritableDatabase();
		
		Intent intent = getIntent();
        if(intent != null){
            sintyo = intent.getStringExtra("com.example.gpsar.sintyoString");
            place = intent.getStringExtra("com.example.gpsar.placeString");
        }	
        
        
    	step = (float)0.0046 * Float.parseFloat(sintyo); //ユーザの身長データを基に、歩幅決定 [歩幅(m)＝身長(m)*0.46]

    	if(place.equals("323教員室")){
    		placeNo = 323;
    	}else{
            placeNo = Integer.parseInt(place);
    	}
        
        //ルーティング機能は未実装である
        //また、評価実験はあらかじめルートが決められたもので行うため、
        //トップ画面で選ばれたルートによって読み込む測定点ファイルを最小限にする
    	switch(placeNo){
    	  case 1:   //入口 → 231教員室　測定点少
    		  rssiVal = 10;
    		  point = new ArrayList[rssiVal][2];
    		  CSV("entrance.csv", 0);
    		  CSV("libOut.csv", 1);
    		  CSV("libIn.csv", 2);
    		  CSV("stepFrontOut.csv", 3);
    		  CSV("stepFrontIn.csv", 4);
    		  CSV("330front.csv", 5);
    		  CSV("330gfIn.csv", 6);
    		  CSV("230gfOut.csv", 7);
    		  CSV("230gfIn.csv", 8);
    		  CSV("235front.csv", 9);
    		  break;
    		  
    	  case 323:   //入口 → 231教員室　測定点多
    		  rssiVal = 10;
    		  point = new ArrayList[rssiVal][2];
    		  CSV("entrance.csv", 0);
    		  CSV("libOut.csv", 1);
    		  CSV("libIn.csv", 2);
    		  CSV("atmfront.csv", 3);
    		  CSV("atmTostep.csv", 4);
    		  CSV("stepFrontOut.csv", 5);
    		  CSV("stepFrontIn.csv", 6);
    		  CSV("322front.csv", 7);
    		  CSV("326front.csv", 8);
    		  CSV("330front.csv", 9);
    		  //CSV("330gfIn.csv", 10);
    		  //CSV("230gfOut.csv", 11);
    		  //CSV("230gfIn.csv", 12);
    		  //CSV("321front.csv", 13);
    		  //CSV("235front.csv", 14);
    		  break;
    		  
    	  case 3:   //デルタビスタ → 426教室　測定点少
    		  rssiVal = 11;
    		  point = new ArrayList[rssiVal][2];
    		  CSV("deltaCenter.csv", 0);
    		  CSV("deltaOut.csv", 1);
    		  CSV("deltaIn.csv", 2);
    		  CSV("m401front.csv", 3);
    		  CSV("431Out.csv", 4);
    		  CSV("431In.csv", 5);
    		  CSV("s9Out.csv", 6);
    		  CSV("s9In.csv", 7);
    		  CSV("429Out.csv", 8);
    		  CSV("429In.csv", 9);
    		  CSV("426front.csv", 10);
    		  break;
    		  
    	  case 4:   //デルタビスタ → 426教室　測定点多
    		  rssiVal = 11;
    		  point = new ArrayList[rssiVal][2];
    		  CSV("deltaCenter.csv", 0);
    		  CSV("deltaOut.csv", 1);
    		  CSV("deltaIn.csv", 2);
    		  CSV("m401front.csv", 3);
    		  CSV("431Out.csv", 4);
    		  CSV("431In.csv", 5);
    		  CSV("s9Out.csv", 6);
    		  CSV("s9In.csv", 7);
    		  CSV("429Out.csv", 8);
    		  CSV("429In.csv", 9);
    		  CSV("426front.csv", 10);
    		  break;
    		  
    	  case 5:   //入口 → 231教員室　測定点多
    		  rssiVal = 11;
    		  point = new ArrayList[rssiVal][2];
    		  CSV("point1.csv", 0);
    		  CSV("point2.csv", 1);
    		  CSV("point3.csv", 2);
    		  CSV("point4.csv", 3);
    		  CSV("point5.csv", 4);
    		  CSV("point6.csv", 5);
    		  CSV("point7.csv", 6);
    		  CSV("point8.csv", 7);
    		  CSV("point9.csv", 8);
    		  CSV("point10.csv", 9);
    		  CSV("point11.csv", 10);
    		  break;
    	}

		fingerPrint();

		// フルスクリーン指定
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ARViewの取得
		arView = new ARView(this);

		// (1)各種センサーの用意
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		listMag = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
		listAcc = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

		// (2)Viewの重ね合わせ
		setContentView(new CameraView(this));
		addContentView(arView, new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));

	}

	public void OnCre2() {

		fingerPrint();

		// フルスクリーン指定
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ARViewの取得
		arView = new ARView(this);

		// (1)各種センサーの用意
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		listMag = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
		listAcc = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

		// (2)Viewの重ね合わせ
		setContentView(new CameraView(this));
		addContentView(arView, new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));

	}

	public float getDx() {
		return dx;
	}

	public float getDy() {
		return dy;
	}

	public float getDz() {
		return dz;
	}

	public double getVectorSize() {
		return vectorSize;
	}

	public long getCounter() {
		return counter;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// (3)郢ｧ�ｽｻ郢晢ｽｳ郢ｧ�ｽｵ郢晢ｽｼ陷�ｽｦ騾��ｿｽ邵ｺ�ｽｮ騾具ｽｻ鬪ｭ�ｽｲ
		sensorManager.registerListener(this, (Sensor) listMag.get(0),
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, (Sensor) listAcc.get(0),
				SensorManager.SENSOR_DELAY_NORMAL);

	}

	@Override
	public void onStop() {
		super.onStop();

		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			accelerometerValues = event.values.clone();
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			magneticValues = event.values.clone();
			break;
		}

		if (magneticValues != null && accelerometerValues != null) {
			float[] R = new float[16];
			float[] I = new float[16];

			SensorManager.getRotationMatrix(R, I, accelerometerValues,
					magneticValues);

			float[] actual_orientation = new float[3];

			SensorManager.getOrientation(R, actual_orientation);
			// 求まった方位角をラジアンから度に変換する
			float direction = (float) Math.toDegrees(actual_orientation[0]);
			arView.drawScreen(direction, drDistance, fpno, placeNo); //ARViewクラスに値の受け渡し(端末の向いている方向,累計進んだ距離,測定点の推定結果,トップ画面で選んだルート番号)	
		}

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			// 増加量
			dx = event.values[0] - oldx;
			dy = event.values[1] - oldy;
			dz = event.values[2] - oldz;
			// ベクトル量をピタゴラスの定義から求める。
			// が正確な値は必要でなく、消費電力から平方根まで求める必要はない
			// vectorSize = Math.sqrt((double)(dx*dx+dy*dy+dz*dz));
			vectorSize = dx * dx + dy * dy + dz * dz;
			// ベクトル計算を厳密に行うと計算量が上がるため、簡易的な方向を求める。
			// 一定量のベクトル量があり向きの反転があった場合（多分走った場合）
			// vecchangecountはSENSOR_DELAY_NORMALの場合、200ms精度より
			// 加速度変化が検出できないための専用処理。精度を上げると不要
			// さらに精度がわるいことから、連続のベクトル変化は検知しない。
			long dt = new Date().getTime() - changeTime;
			boolean dxx = Math.abs(dx) > thresholdMin && vecx != (dx >= 0);
			boolean dxy = Math.abs(dy) > thresholdMin && vecy != (dy >= 0);
			boolean dxz = Math.abs(dz) > thresholdMin && vecz != (dz >= 0);
			if (vectorSize > threshold && dt > thresholdTime
					&& (dxx || dxy || dxz)) {
				vecchangecount++;
				changeTime = new Date().getTime();

			}
			// ベクトル量がある状態で向きが２回（上下運動とみなす）変わった場合
			// または、ベクトル量が一定値を下回った（静止とみなす）場合、カウント許可
			if (vecchangecount > 1 || vectorSize < 1) {
				counted = false;
				vecchangecount = 0;
			}
			// カウント許可で、閾値を超えるベクトル量がある場合、カウント
			if (!counted && vectorSize > threshold) {

				counted = true;
				vecchangecount = 0;
				counter++;

				float theta;
				drDistance = drDistance + step;

				/*
				 * if(pretheta >= 0 && pretheta < 90){ theta = pretheta;
				 * drDistance = (float)(drDistance + (step * Math.cos(theta)));
				 * } if(pretheta >= 90 && pretheta < 180){ theta = 180 -
				 * pretheta; drDistance = (float)(drDistance - (step *
				 * Math.cos(theta))); } if(pretheta >= 180 && pretheta < 270){
				 * theta = pretheta - 180; drDistance = (float)(drDistance -
				 * (step * Math.cos(theta))); } if(pretheta >= 270 && pretheta
				 * <= 360){ theta = 360 - pretheta; drDistance =
				 * (float)(drDistance + (step * Math.cos(theta))); }
				 */

			}
			// カウント自の加速度の向きを保存
			vecx = dx >= 0;
			vecy = dy >= 0;
			vecz = dz >= 0;
			// 状態更新
			oldVectorSize = vectorSize;
			// 加速度の保存
			oldx = event.values[0];
			oldy = event.values[1];
			oldz = event.values[2];
		}

	}

	// Fingerprint手法の位置推定アルゴリズム
	public void fingerPrint() {

		// タイマークラスによる自動実行
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				handle.post(new Runnable() {
					@Override
					public void run() {

						// int fpno = 0;
						double max1 = 0;
						double max2 = 0;
						double sigma1[] = new double[rssiVal];
						;
						double sigma2 = 0;
						double e[] = new double[rssiVal];
						double w[] = new double[rssiVal];
						double theta = 0; // 現在位置で取得したRSSIを格納する
						double s = 0; // データベースのrssiを格納する
						int count[] = new int[rssiVal]; // 各測定点におけるDBとスキャン結果の一致APを数える

						WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
						manager.startScan();
						List<ScanResult> results = manager.getScanResults();

						for (int j = 0; j < rssiVal; j++) { // 測定点番号ループ　(測定点1->測定点2->・・・)
							for (int k = 0; k < point[j][0].size(); k++) { // 測定点データループ
								for (int i = 0; i < results.size(); i++) { // APスキャン結果ループ
									if (results.get(i).level > -70) {   //明らかに微弱な電波は除外するためにRSSIの下限を定める
										if (results.get(i).BSSID.equals(point[j][0].get(k))) { // APスキャン結果のBSSID＝測定点データのBSSIDならば　アルゴリズム計算
											count[j]++;
											theta = results.get(i).level;
											s = Double.parseDouble(point[j][1].get(k));
											sigma1[j] = sigma1[j] + Math.pow(theta - s, 2);
											break;
										}
									//}
								}
							}
							if (count[j] > max1)
								max1 = count[j];
							Log.d("count", String.valueOf(count[j]));
							// Log.d("theta", String.valueOf(theta));
							// Log.d("s", String.valueOf(s));
							
							e[j] = Math.sqrt(sigma1[j]);

						}
						}

						
						// ---------------------------------------
						// 取得RSSIとDB内RSSIの一致AP数の違いに対応
						// 一致AP数が一番多い測定点にAP数を合わせるために
						// 足りないAP数のぶんだけ
						// 各測定点における取得RSSIとDB内RSSIの誤差の平均を加算していく
						for (int o = 0; o < rssiVal; o++) {
							double difVal = max1 - count[o];
							if (difVal != 0 && count[o] > 5) { // 一致AP数が明らかに少ない測定点を省くためにcount[o] > ○　を入れているが不要？　else if以降も同様
								sigma1[o] = sigma1[o]+ ((difVal * sigma1[o]) / count[o]);
							} else if (difVal != 0 && count[o] <= 5) {
								sigma1[o] = 0;
							}
							e[o] = Math.sqrt(sigma1[o]);
							// Log.d("ruijido"+ o, String.valueOf(e[o]));
						}
						// ---------------------------------------
						 


						// 2本目の式（重み付け）
						for (int l = 0; l < rssiVal; l++) { // 類似度ループ
							if (e[l] != 0)
								sigma2 = sigma2 + (1 / (e[l] * e[l])); // 　0で除算しないためのif
						}

						for (int t = 0; t < rssiVal; t++) { // 類似度ループ
							if (e[t] == 0) { // 0で除算しないためのif
								w[t] = 0;
							} else if (e[t] != 0) {
								w[t] = (1 / (e[t] * e[t])) / sigma2;
							}
							Log.d("weight" + t, String.valueOf(w[t]));
							if (w[t] > max2) {
								max2 = w[t];
								fpno = t;
							}
						}
						
						
						/* 精度評価実験　　測定点記録用ソースコード      */
						/*-----------------以下------------------*/
						
						//if(scanCount < 60){
						//	ContentValues values = new ContentValues();
						//	values.put("point", fpno+1);
						//	mydb.insert("mytable", null, values);				
						//}
						//scanCount++;
					
						/*-----------------以上------------------*/
						
					}
					
				});

			}

		}, 0, INTERVAL_PERIOD);
	}

	// CSVファイルを読み込み、ArrayListに格納する（ 引数はファイル名, 測定点番号）
	public void CSV(String pass, int no) {
		// AssetManagerの呼び出し
		AssetManager assetManager = getResources().getAssets();
		try {
			// 初期化
			point[no][0] = new ArrayList<String>();
			point[no][1] = new ArrayList<String>();

			InputStream fin = null;
			fin = assetManager.open(pass);
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));

			// 最終行まで読み込む
			String line = "";

			while ((line = br.readLine()) != null) {

				// 1行をデータの要素に分割
				StringTokenizer st = new StringTokenizer(line, ",");
				int i = 0;
				while (st.hasMoreTokens()) {
					// 1行の各要素をタブ区切りで表示
					String tmp = st.nextToken();
					tmp = tmp.replaceAll("\"", "");
					point[no][i].add(tmp);
					i++;
				}
			}
			br.close();

		} catch (FileNotFoundException e) {
			// Fileオブジェクト生成時の例外捕捉
			e.printStackTrace();
		} catch (IOException e) {
			// BufferedReaderオブジェクトのクローズ時の例外捕捉
			e.printStackTrace();
		}
	}
	
	

}