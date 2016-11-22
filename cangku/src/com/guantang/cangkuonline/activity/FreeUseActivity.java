package com.guantang.cangkuonline.activity;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.startmarket.MarketUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class FreeUseActivity extends Activity {
	private ImageButton backBtn,mImgeButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_freeuse);
		backBtn = (ImageButton) findViewById(R.id.back);
		mImgeButton = (ImageButton) findViewById(R.id.go_appraise_ImgBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				finish();
			}
		});
		mImgeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent1 = MarketUtils.getIntent(FreeUseActivity.this);
				if(MarketUtils.judge(FreeUseActivity.this, intent1)){
					startActivity(intent1);
				}else{
					Toast.makeText(FreeUseActivity.this, "û�ҵ��ֻ�Ӧ���г�", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
