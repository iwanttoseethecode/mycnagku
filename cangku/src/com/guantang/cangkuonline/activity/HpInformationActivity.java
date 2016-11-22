package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.customview.PagerSlidingTabStrip;
import com.guantang.cangkuonline.fragment.HpCkkcFragment;
import com.guantang.cangkuonline.fragment.HpInformationFragment;
import com.guantang.cangkuonline.fragment.HpMovedFragment;
import com.guantang.cangkuonline.fragment.PiCiFragment;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;

public class HpInformationActivity extends FragmentActivity implements OnClickListener,OnPageChangeListener{
	
	private ImageButton backImgBtn;
	private TextView modfiyTextView;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private SharedPreferences mSharedPreferences;
	private String hpid = "",hpmc = "",hpbm = "",ggxh = "",jldw = "",lb = "";
	private List<String> titleList = new ArrayList<String>();
	private ArrayList<Fragment> fragmentlist=new ArrayList<Fragment>();
	private MyPagerAdapter mMyPagerAdapter;
	
	/**
	 * ����ȡ���ݺ󣬰ѰѼ�����λ�ͻ�Ʒ��𴫵ݸ���Ʒ��ϸfragment
	 * */
	public interface TransmitJldwAndLb{
		public abstract void execute(String jldw,String lb);
	};
	private TransmitJldwAndLb mTransmitJldwAndLb;
	public void setTransmitJldwAndLb(TransmitJldwAndLb transmitJldwAndLb){
		mTransmitJldwAndLb = transmitJldwAndLb;
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hpinformation);
		mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		Intent intent = getIntent();
		hpid = intent.getStringExtra("id");
		hpmc = intent.getStringExtra("hpmc");
		hpbm = intent.getStringExtra("hpbm");
		ggxh = intent.getStringExtra("ggxh");
		initView();
	}
	
	@Override
	protected void onStart() {
		// TODO �Զ����ɵķ������
		super.onStart();
		titleList.clear();
		fragmentlist.clear();
		init();
		setPageTitlesValue();
	}
	
	public void initView(){
		backImgBtn = (ImageButton) findViewById(R.id.back);
		modfiyTextView = (TextView) findViewById(R.id.mod);
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerTabStrip1);
		mViewPager = (ViewPager) findViewById(R.id.viewpagercontent1);
		
		backImgBtn.setOnClickListener(this);
		modfiyTextView.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(this);
	}
	
	public void init(){
		titleList.add("��Ʒ��Ϣ");
		titleList.add("�����Ϣ");
		titleList.add("�������ϸ");
		titleList.add("�������");
		
		fragmentlist.add(HpInformationFragment.getInstance(hpid));
		fragmentlist.add(HpCkkcFragment.getInstance(hpid));
		fragmentlist.add(HpMovedFragment.getInstance(hpid,hpmc,hpbm,ggxh));
		fragmentlist.add(new PiCiFragment());
		
		mViewPager.setOffscreenPageLimit(4);
		mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mMyPagerAdapter);
		mPagerSlidingTabStrip.setViewPager(mViewPager);
	}
	
	/**
	 * ����Titile�������
	 */
	private void setPageTitlesValue() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		// ����Ϊtrue ���ȷ���titleλ��
		mPagerSlidingTabStrip.setShouldExpand(true);
		
		mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);

		//(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm)  ����ֵ1ת��dpΪ��λ
		//�����»��ָ��߸߶�
		mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm)); 

		//����ָʾ������
		mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm));

		//�����ı��ִ�С
		mPagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));

		//����ָʾ����ɫ
		mPagerSlidingTabStrip.setIndicatorColor(Color.parseColor("#f5b53a"));

		//����ѡ���ı���ɫ
		mPagerSlidingTabStrip.setSelectedTextColor(Color.parseColor("#f5b53a"));

		//����Title������ɫ
		mPagerSlidingTabStrip.setTabBackground(0);//android.R.color.darker_gray
	}
	
	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.mod:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if (RightsHelper.isHaveRight(RightsHelper.hp_addedit,mSharedPreferences) == true){
					intent.setClass(HpInformationActivity.this, ModifyHPActivity.class);
					intent.putExtra("hpid", hpid);
					startActivity(intent);
				}else{
					Toast.makeText(HpInformationActivity.this, "�Բ�����û�е���ӡ��޸Ļ�Ʒ��Ȩ��",
							Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, "����ģʽ�²����޸Ļ�Ʒ", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	public void setJldw_Lb(String mJldw,String mLb){
		if(mJldw!=null){
			this.jldw = jldw;
		}
		if(mLb!=null){
			this.lb = mLb;
		}
	}
	
	public String getJldw(){
		return jldw;
	}
	
	public String getLb(){
		return lb;
	}
	
	class MyPagerAdapter extends FragmentPagerAdapter{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO �Զ����ɵĹ��캯�����
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO �Զ����ɵķ������
			return fragmentlist.get(arg0);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO �Զ����ɵķ������
			return titleList.get(position);
		}

		@Override
		public int getCount() {
			// TODO �Զ����ɵķ������
			return fragmentlist.size();
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO �Զ����ɵķ������
		if(arg0==2){
			mTransmitJldwAndLb.execute(jldw, lb);
		}
	}
	
}
