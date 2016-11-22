package com.guantang.cangkuonline.activity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.TagFlowLayout.FlowLayout;
import com.guantang.cangkuonline.TagFlowLayout.TagAdapter;
import com.guantang.cangkuonline.TagFlowLayout.TagFlowLayout;
import com.guantang.cangkuonline.XListView.XListView;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.activity.MySubmitDJActivity.MyDJAsynctask;
import com.guantang.cangkuonline.adapter.JSONDataAdapter;
import com.guantang.cangkuonline.customview.ControlScrollViewPager;
import com.guantang.cangkuonline.customview.PagerSlidingTabStrip;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.eventbusBean.ObjectTen;
import com.guantang.cangkuonline.fragment.DJFragment;
import com.guantang.cangkuonline.fragment.DiaoboDJFragment;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

public class NetDJActivity extends FragmentActivity implements OnClickListener{
	
	private TextView searchTxtView;
	private ImageButton backBtn;
	private SharedPreferences mSharedPreferences;
	private List<JSONObject> djlist = new ArrayList<JSONObject>();
	private String sql = "",sql1 = "";
	private String[] str1 = { DataBaseHelper.HPM_ID, DataBaseHelper.MVDH,
			DataBaseHelper.MVDT, DataBaseHelper.CKMC, DataBaseHelper.CKID,
			DataBaseHelper.DepName, DataBaseHelper.DepID,
			DataBaseHelper.DWName, DataBaseHelper.JBR,
			DataBaseHelper.BZ, DataBaseHelper.hpDetails,
			DataBaseHelper.actType, DataBaseHelper.mType, DataBaseHelper.Lrsj,
			DataBaseHelper.Lrr };
	private String[] str2 = { "hpm_id", "mvdh", "mvdt", "ckName", "ckid",
			"depName", "depId", "dwName", "jbr", "bz", "hpDetails",
			"actType", "mType", "lrsj", "lrr" };
	private SimpleDateFormat formatter1;
	private JSONDataAdapter mJSONDataAdapter;
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private String typeString,todaytime;
	private Boolean firstflag = true;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ControlScrollViewPager mControlScrollViewPager;
	private List<String> titleList = new ArrayList<String>();
	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	private MyPagerAdapter mMyPagerAdapter;
	
	public interface DJfilterInterface{
		void execute(ObjectTen obj);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_netdj);
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initView();
		init();
	}
	
	
	
	public void initView() {
		backBtn = (ImageButton) findViewById(R.id.back);
		searchTxtView = (TextView) findViewById(R.id.search);
		mPagerSlidingTabStrip =(PagerSlidingTabStrip) findViewById(R.id.pagerTabStrip1);
		mControlScrollViewPager = (ControlScrollViewPager) findViewById(R.id.viewpagercontent1);
		searchTxtView.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		mControlScrollViewPager.setScrollable(true);

	}

	public void init() {
		
		titleList.add("��ⵥ��");
		titleList.add("���ⵥ��");
		titleList.add("�̵㵥��");
		titleList.add("��������");
		
		DJFragment rukuDJFragment = DJFragment.getInstance(1);
		DJFragment chukuDJFragment = DJFragment.getInstance(2);
		DJFragment pandianDJFragment = DJFragment.getInstance(0);
		DiaoboDJFragment diaoboDJFragment = new DiaoboDJFragment();
		
		fragmentList.add(rukuDJFragment);
		fragmentList.add(chukuDJFragment);
		fragmentList.add(pandianDJFragment);
		fragmentList.add(diaoboDJFragment);
		
		mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		mControlScrollViewPager.setAdapter(mMyPagerAdapter);
		mPagerSlidingTabStrip.setViewPager(mControlScrollViewPager);
		mMyPagerAdapter.setData(fragmentList);
		mControlScrollViewPager.setOffscreenPageLimit(4);
		setPageTitlesValue();
			
		if (WebserviceImport.isOpenNetwork(this)) {
			new MyCheckPermissions().executeOnExecutor(cacheThreadPool);
		} else {
			Toast.makeText(this, "����δ����", Toast.LENGTH_SHORT).show();
		}
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
		Calendar calender = Calendar.getInstance();
		switch(v.getId()){
		case R.id.back:
			finish();
		break;
		case R.id.search:
			intent.setClass(this, NetDJfilterActivity.class);
			startActivityForResult(intent, 1);
			break;
		}
	}
	
	class MyCheckPermissions extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO �Զ����ɵķ������
			String jsonString = WebserviceImport_more.Gt_MovemLevel_1_0(mSharedPreferences);
			return jsonString;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO �Զ����ɵķ������
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					String permissionString = jsonObject.getString("Data");
					String[] permission = permissionString.split(",");
					StringBuilder sayBuilder = new StringBuilder();
					if(permission[0].equals("1")){
						sayBuilder.append(" ��� ");
					}
					if(permission[1].equals("1")){
						sayBuilder.append("���� ");
					}
					if(permission[2].equals("1")){
						sayBuilder.append("�̵� ");
					}
					if(!sayBuilder.toString().isEmpty()){
						Toast.makeText(NetDJActivity.this, "��ӵ�в鿴����"+sayBuilder.toString()+"�ĵ���Ȩ��", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(NetDJActivity.this, "��û����Ӧ�ĵ��ݲ鿴Ȩ��", Toast.LENGTH_SHORT).show();
					}
					break;
				}
			} catch (JSONException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO �Զ����ɵķ������
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
				String fromtime = data.getStringExtra("fromtime");
				String totime = data.getStringExtra("totime");
				sql = "mvdt >='" + fromtime
						+ " 00:00:00'  and " + "mvdt <='"
						+ totime + " 23:59:59' ";
				sql1 = " mvdt >='" + fromtime
						+ " 00:00:00'  and " + "mvdt <='"
						+ totime + " 23:59:59' ";
				
				int ckid = data.getIntExtra("ckid", -1);
				if (ckid != -1) {
					sql = sql + " and " + DataBaseHelper.CKID + " = " + ckid
							+ " ";
					sql1 = sql1 + " and " + DataBaseHelper.sckid +" = "+ckid+" or "+DataBaseHelper.dckid + " = "+ckid+" ";
				}

				String dh = data.getStringExtra("dh");
				if (!dh.equals("����")) {
					sql = sql + " and " + DataBaseHelper.MVDH + " = '" + dh
							+ "' ";
					sql1 = sql1 + " and " + DataBaseHelper.MVDH + " = '" + dh
							+ "' ";
				}
				
				for(int i = 0 ; i<fragmentList.size();i++){
					((DJfilterInterface) fragmentList.get(i)).execute(new ObjectTen(sql, sql1));
				}
				
			}
		}
	}
	
	class MyPagerAdapter extends FragmentPagerAdapter{
		
		private List<Fragment> mList = new ArrayList<Fragment>();
		
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}
		
		public void setData(List<Fragment> list){
			this.mList = list;
			notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return mList.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titleList.get(position);
		}
		
	}
	
}
