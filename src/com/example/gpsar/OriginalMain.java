package com.example.gpsar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class OriginalMain extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		// ボタンのオブジェクトを取得
		Button btn = (Button) findViewById(R.id.button);

		// クリックイベントを受け取れるようにする
		btn.setOnClickListener(new OnClickListener() {
			// このメソッドがクリック毎に呼び出される
			public void onClick(View v) {
				// ここにクリックされたときの処理を記述
							
				startAct();
			}
		});

	}

	public void startAct() {
		
		//身長の取得
		EditText et = (EditText) findViewById(R.id.textEdit);
		et.selectAll();
        String sintyo = et.getText().toString();
        
        //目的地の取得
        Spinner spinner = (Spinner)findViewById(R.id.spinner);	         
        String place = spinner.getSelectedItem().toString();

		
		Intent intent = new Intent();
		intent.setClassName("com.example.gpsar","com.example.gpsar.MainActivity");
        intent.putExtra("com.example.gpsar.sintyoString", sintyo);
        intent.putExtra("com.example.gpsar.placeString", place);
 
		startActivity(intent);

	}

}
