package com.example.gpsar;

import java.math.BigDecimal;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

@SuppressLint("Instantiatable")
public class ARView extends View {

	// 逕ｻ蜒上�ｮ隱ｭ縺ｿ霎ｼ縺ｿ
	Resources res = this.getContext().getResources();
	Bitmap yajirusi = BitmapFactory.decodeResource(res, R.drawable.yajirusi);
	Bitmap migiyajirusi = BitmapFactory.decodeResource(res,R.drawable.migiyajirusi);
	Bitmap hidariyajirusi = BitmapFactory.decodeResource(res,R.drawable.hidariyajirusi);


	// 蜷代″繧剃ｿ晄戟縺吶ｋ螟画焚
	float direction;
	float drDistance;
	int fpno;
	boolean flag = false;
	float refDistance;
	float arDistance;
	int placeNo;

	public ARView(Context context) {
		super(context);
	}

	// (1)描画処理
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		// コンパスを描画する
		drawCompass(canvas, paint, paint2);

	}

	// (2)コンパスの描画
	private void drawCompass(Canvas canvas, Paint paint, Paint paint2) {
		
		switch (placeNo) {
		case 1:  //入口 → 231教員室　測定点少
			if (fpno == 1 || fpno == 2) {
				if (direction > 220 && direction < 280)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 70)	canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 280 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 70 && direction <= 220) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}

			if (fpno == 3 || fpno == 4 ||fpno == 7 || fpno == 8 ) {
				if (direction > 170 && direction < 230)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 20) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 230 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 20 && direction <= 170)	canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}
			
			
			if (fpno == 5 || fpno == 6 ) {
				if (direction > 260 && direction < 320)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 110) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 320 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 110 && direction <= 260) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}

			if (fpno == 9 || fpno == 10 ) {
				if (direction > 260 && direction < 320)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 110) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 320 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 110 && direction <= 260) canvas.drawBitmap(migiyajirusi, 0, 0, paint);

				//最後のジオフェンスからは「目的地までの距離」を表示するため
				//最後のジオフェンスに入った時点で、進んだ距離の累計を0に戻す
				//以下は、この操作を1度だけ行うためのflagである
				if (flag == false) {
					flag = true;
					refDistance = drDistance;
				}

				arDistance = drDistance - refDistance;
				BigDecimal bdDistance = new BigDecimal(arDistance); // 進んだ距離をBigDecimal型にする
				BigDecimal dispDist = bdDistance.setScale(1, BigDecimal.ROUND_DOWN); // 小数第2位　以下を切り捨てる
				paint.setColor(Color.WHITE);
				paint.setTextSize(100);
				BigDecimal mokuteki = new BigDecimal("50.0"); // 最後のジオフェンスから目的地までの距離
				BigDecimal nokori = mokuteki.subtract(dispDist); // 目的地点までの残り距離 [最後のジオフェンスから目的地までの距離 - 最後のジオフェンスから進んだ距離]
				canvas.drawText("目的地までの距離\r\n" + nokori.toString() + "m", 150,	1000, paint);

			}

			break;

		case 323:  //入口 → 231教員室　測定点多
			if (fpno == 1 || fpno == 2) {
				if (direction > 220 && direction < 280)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 70)	canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 280 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 70 && direction <= 220) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}

			if (fpno == 3 || fpno == 4 ||fpno == 5 || fpno == 6 ) {
				if (direction > 170 && direction < 230)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 20) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 230 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 20 && direction <= 170)	canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}
			
			
			if (fpno == 7 || fpno == 8 || fpno == 9 || fpno == 10 ) {
				if (direction > 285 && direction < 345)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 135) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 345 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 135 && direction <= 285) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
				
				//最後のジオフェンスからは「目的地までの距離」を表示するため
				//最後のジオフェンスに入った時点で、進んだ距離の累計を0に戻す
				//以下は、この操作を1度だけ行うためのflagである
				if (flag == false) {
					flag = true;
					refDistance = drDistance;
			}

				/*
			if (fpno == 13 || fpno == 14 || fpno == 15) {
				if (direction > 260 && direction < 320)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 110) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 320 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 110 && direction <= 260) canvas.drawBitmap(migiyajirusi, 0, 0, paint);

				//最後のジオフェンスからは「目的地までの距離」を表示するため
				//最後のジオフェンスに入った時点で、進んだ距離の累計を0に戻す
				//以下は、この操作を1度だけ行うためのflagである
				if (flag == false) {
					flag = true;
					refDistance = drDistance;
				}
				*/

				arDistance = drDistance - refDistance;
				BigDecimal bdDistance = new BigDecimal(arDistance); // 進んだ距離をBigDecimal型にする
				BigDecimal dispDist = bdDistance.setScale(1, BigDecimal.ROUND_DOWN); // 小数第2位　以下を切り捨てる
				paint.setColor(Color.WHITE);
				paint.setTextSize(100);
				BigDecimal mokuteki = new BigDecimal("15.0"); // 最後のジオフェンスから目的地までの距離
				BigDecimal nokori = mokuteki.subtract(dispDist); // 目的地点までの残り距離 [最後のジオフェンスから目的地までの距離 - 最後のジオフェンスから進んだ距離]
				if(mokuteki.compareTo(dispDist)== -1){
					canvas.drawText("目的地までの距離\r\n" + 0 + "m", 150,	1000, paint);
				}else{
				canvas.drawText("目的地までの距離\r\n" + nokori.toString() + "m", 150,	1000, paint);
				}

			}

			break;

		case 3:  //デルタビスタ → 426教室　測定点少
			if (fpno == 1 || fpno == 2) {
				if (direction >= 0 && direction < 55) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction > 355 && direction <= 360) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 55 && direction <= 205) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction > 205 && direction <= 355) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}

			if (fpno == 3 || fpno == 4 || fpno == 5) {
				if (direction >= 5 && direction < 65) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 65 && direction <= 215) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction > 215 && direction <= 360) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
				if (direction >= 0 && direction < 5 ) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}
			
			if (fpno == 6 || fpno == 7) {
				if (direction >= 100 && direction < 160) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 160 && direction < 310) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 310 && direction <= 360) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
				if (direction >= 0 && direction < 100) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}

			if (fpno == 8 || fpno == 9) {
				if (direction >= 190 && direction < 250) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 250 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 0 && direction < 40) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 40 && direction < 190) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}

			if (fpno == 10 || fpno == 11) {
				if (direction >= 100 && direction < 160) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 160 && direction < 310) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 310 && direction <= 360) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
				if (direction >= 0 && direction < 100) canvas.drawBitmap(migiyajirusi, 0, 0, paint);

				if (flag == false) {
					flag = true;
					refDistance = drDistance;
				}

				arDistance = drDistance - refDistance;
				BigDecimal bdDistance = new BigDecimal(arDistance); // 進んだ距離をBigDecimal型にする
				BigDecimal dispDist = bdDistance.setScale(1, BigDecimal.ROUND_DOWN); // 小数第2位　以下を切り捨てる
				paint.setColor(Color.WHITE);
				paint.setTextSize(100);
				BigDecimal mokuteki = new BigDecimal("50.0"); // 最後のジオフェンスから目的地までの距離
				BigDecimal nokori = mokuteki.subtract(dispDist); // 目的地点までの残り距離
				canvas.drawText("目的地までの距離\r\n" + nokori.toString() + "m", 150, 1000, paint);
			}
			
			break;
			
		case 4:  //デルタビスタ → 426教室　測定点多
			if (fpno == 1 || fpno == 2) {
				if (direction >= 0 && direction < 55) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction > 355 && direction <= 360) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 55 && direction <= 205) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction > 205 && direction <= 355) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}

			if (fpno == 3 || fpno == 4 || fpno == 5) {
				if (direction >= 5 && direction < 65) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 65 && direction <= 215) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction > 215 && direction <= 360) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
				if (direction >= 0 && direction < 5 ) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}
			
			if (fpno == 6 || fpno == 7) {
				if (direction >= 100 && direction < 160) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 160 && direction < 310) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 310 && direction <= 360) canvas.drawBitmap(migiyajirusi, 1000, 0, paint);
				if (direction >= 0 && direction < 100) canvas.drawBitmap(migiyajirusi, 1000, 0, paint);
			}

			if (fpno == 8 || fpno == 9) {
				if (direction >= 190 && direction < 250) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 250 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 0 && direction < 40) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 40 && direction < 190) canvas.drawBitmap(migiyajirusi, 1000, 0, paint);
			}

			if (fpno == 10 || fpno == 11) {
				if (direction >= 100 && direction < 160) canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 160 && direction < 310) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 310 && direction <= 360) canvas.drawBitmap(migiyajirusi, 1000, 0, paint);
				if (direction >= 0 && direction < 100) canvas.drawBitmap(migiyajirusi, 1000, 0, paint);

				if (flag == false) {
					flag = true;
					refDistance = drDistance;
				}

				arDistance = drDistance - refDistance;
				BigDecimal bdDistance = new BigDecimal(arDistance); // 進んだ距離をBigDecimal型にする
				BigDecimal dispDist = bdDistance.setScale(1, BigDecimal.ROUND_DOWN); // 小数第2位　以下を切り捨てる
				paint.setColor(Color.WHITE);
				paint.setTextSize(100);
				BigDecimal mokuteki = new BigDecimal("50.0"); // 最後のジオフェンスから目的地までの距離
				BigDecimal nokori = mokuteki.subtract(dispDist); // 目的地点までの残り距離
				canvas.drawText("目的地までの距離\r\n" + nokori.toString() + "m", 150, 1000, paint);
			}
			
			break;
			
		case 5:  //実験データ計測用
			if (fpno == 1 || fpno == 2) {
				if (direction > 220 && direction < 280)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 70)	canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 280 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 70 && direction <= 220) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}

			if (fpno == 3 || fpno == 4 ||fpno == 7 || fpno == 8 ) {
				if (direction > 170 && direction < 230)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 20) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 230 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 20 && direction <= 170)	canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}
			
			
			if (fpno == 5 || fpno == 6 ) {
				if (direction > 260 && direction < 320)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 110) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 320 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 110 && direction <= 260) canvas.drawBitmap(migiyajirusi, 0, 0, paint);
			}

			if (fpno == 9 || fpno == 10 || fpno == 11) {
				if (direction > 260 && direction < 320)	canvas.drawBitmap(yajirusi, 250, 350, paint);
				if (direction >= 0 && direction < 110) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 320 && direction <= 360) canvas.drawBitmap(hidariyajirusi, 1000, 0, paint);
				if (direction >= 110 && direction <= 260) canvas.drawBitmap(migiyajirusi, 0, 0, paint);

				//最後のジオフェンスからは「目的地までの距離」を表示するため
				//最後のジオフェンスに入った時点で、進んだ距離の累計を0に戻す
				//以下は、この操作を1度だけ行うためのflagである
				if (flag == false) {
					flag = true;
					refDistance = drDistance;
				}

				arDistance = drDistance - refDistance;
				BigDecimal bdDistance = new BigDecimal(arDistance); // 進んだ距離をBigDecimal型にする
				BigDecimal dispDist = bdDistance.setScale(1, BigDecimal.ROUND_DOWN); // 小数第2位　以下を切り捨てる
				paint.setColor(Color.WHITE);
				paint.setTextSize(100);
				BigDecimal mokuteki = new BigDecimal("50.0"); // 最後のジオフェンスから目的地までの距離
				BigDecimal nokori = mokuteki.subtract(dispDist); // 目的地点までの残り距離 [最後のジオフェンスから目的地までの距離 - 最後のジオフェンスから進んだ距離]
				canvas.drawText("目的地までの距離\r\n" + nokori.toString() + "m", 150,	1000, paint);

			}

			break;
			
			
		}

		paint2.setColor(Color.WHITE);
		paint2.setTextSize(100);
		canvas.drawText("測定点" + Integer.toString(fpno), 50, 150, paint2);
		//canvas.drawText(Float.toString(direction), 200, 200, paint2);  //端末の向いている方向を画面に表示する

	}

	// (3)センサー値の取得と再描画
	public void drawScreen(float preDirection, float predrDistance, int prefpno, int preplaceNo) {
		// センサーの値から端末の向きを計算する
		direction = (preDirection + 450) % 360;
		drDistance = predrDistance;
		fpno = prefpno + 1;
		placeNo = preplaceNo;
		// onDrawを呼び出して再描画
		invalidate();
	}

}