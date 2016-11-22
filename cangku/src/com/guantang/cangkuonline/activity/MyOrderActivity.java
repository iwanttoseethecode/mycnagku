package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.List;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.customview.ControlScrollViewPager;
import com.guantang.cangkuonline.customview.PagerSlidingTabStrip;
import com.guantang.cangkuonline.eventbusBean.ObjectNine;
import com.guantang.cangkuonline.fragment.AllOrderFragment;
import com.guantang.cangkuonline.fragment.BeiBoHuiFragment;
import com.guantang.cangkuonline.fragment.DaiShenHeFragment;
import com.guantang.cangkuonline.fragment.DaiZhiXingFragment;
import com.guantang.cangkuonline.fragment.FinishOrderFragment;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyOrderActivity extends FragmentActivity implements OnClickListener{
	private ImageButton backImgBtn;
	private TextView filterTxtView;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ControlScrollViewPager mControlScrollViewPager;
	private List<String> titleList = new ArrayList<String>();
	private List<Fragment> fragmentlist=new ArrayList<Fragment>();
	private MyPagerAdapter mMyPagerAdapter;
	
	public interface DDfilterInterface{
		void execute(ObjectNine obj);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myorder);
		initView();
		init();
	}
	
	@Override
	protected void onDestroy() {
		// TODO �Զ����ɵķ������
		super.onDestroy();
	}
	
	public void initView(){
		backImgBtn = (ImageButton) findViewById(R.id.back);
		filterTxtView = (TextView) findViewById(R.id.filter);
		mPagerSlidingTabStrip =(PagerSlidingTabStrip) findViewById(R.id.pagerTabStrip1);
		mControlScrollViewPager = (ControlScrollViewPager) findViewById(R.id.viewpagercontent1);
		backImgBtn.setOnClickListener(this);
		filterTxtView.setOnClickListener(this);
		mControlScrollViewPager.setScrollable(false);
	}
	
	public void init(){
		titleList.add("ȫ��");
		titleList.add("�����");
		titleList.add("��ִ��");
		titleList.add("������");
		titleList.add("�����");
		
		AllOrderFragment mAllOrderFragment = new AllOrderFragment();
		DaiShenHeFragment mDaiShenHeFragment = new DaiShenHeFragment();
		DaiZhiXingFragment mDaiZhiXingFragment = new DaiZhiXingFragment();
		FinishOrderFragment mFinishOrderFragment = new FinishOrderFragment();
		BeiBoHuiFragment mBeiBoHuiFragment = new BeiBoHuiFragment();
		
		fragmentlist.add(mAllOrderFragment);
		fragmentlist.add(mDaiShenHeFragment);
		fragmentlist.add(mDaiZhiXingFragment);
		fragmentlist.add(mBeiBoHuiFragment);
		fragmentlist.add(mFinishOrderFragment);
		
		mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		mControlScrollViewPager.setAdapter(mMyPagerAdapter);
		mPagerSlidingTabStrip.setViewPager(mControlScrollViewPager);
		mMyPagerAdapter.setData(fragmentlist);
		mControlScrollViewPager.setOffscreenPageLimit(5);
		setPageTitlesValue();
		
//		Intent intent = getIntent();
//		mControlScrollViewPager.setCurrentItem(intent.getIntExtra("myorderstart", 0));
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
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.filter:
			Intent intent = new Intent(this,OrderfilterActivity.class);
			startActivityForResult(intent, 1);
			break;
		}
	}
	
	class GetDDAllInfo_1_0AsyncTask extends AsyncTask<Void,Void,String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO �Զ����ɵķ������
			String jsonString = WebserviceImport_more.GetDDAllInfo_1_0(10, -1); // -1�ǵ���1����
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO �Զ����ɵķ������
			super.onPostExecute(result);
			
		}
	}
	
	class MyPagerAdapter extends FragmentPagerAdapter{
		
		private List<Fragment> myList = new ArrayList<Fragment>();

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO �Զ����ɵĹ��캯�����
		}
		
		public void setData(List<Fragment> List){
			this.myList = List;
			notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO �Զ����ɵķ������
			return myList.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO �Զ����ɵķ������
			return myList.size();
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO �Զ����ɵķ������
			return titleList.get(position);
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO �Զ����ɵķ������
		super.onActivityResult(arg0, arg1, arg2);
		if(arg0 == 1){
			if(arg1 == 1){
				String sql = arg2.getStringExtra("sql");
				String sqfromtime = arg2.getStringExtra("sqfromtime");
				String sqtotime = arg2.getStringExtra("sqtotime");
				
				for(int i = 0; i<fragmentlist.size();i++){
					 ((DDfilterInterface) fragmentlist.get(i)).execute(new ObjectNine(sql, sqfromtime, sqtotime));
				}
			}
		}
	}

	
}
