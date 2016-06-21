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
	
		// �{�^���̃I�u�W�F�N�g���擾
		Button btn = (Button) findViewById(R.id.button);

		// �N���b�N�C�x���g���󂯎���悤�ɂ���
		btn.setOnClickListener(new OnClickListener() {
			// ���̃��\�b�h���N���b�N���ɌĂяo�����
			public void onClick(View v) {
				// �����ɃN���b�N���ꂽ�Ƃ��̏������L�q
							
				startAct();
			}
		});

	}

	public void startAct() {
		
		//�g���̎擾
		EditText et = (EditText) findViewById(R.id.textEdit);
		et.selectAll();
        String sintyo = et.getText().toString();
        
        //�ړI�n�̎擾
        Spinner spinner = (Spinner)findViewById(R.id.spinner);	         
        String place = spinner.getSelectedItem().toString();

		
		Intent intent = new Intent();
		intent.setClassName("com.example.gpsar","com.example.gpsar.MainActivity");
        intent.putExtra("com.example.gpsar.sintyoString", sintyo);
        intent.putExtra("com.example.gpsar.placeString", place);
 
		startActivity(intent);

	}

}
