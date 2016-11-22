package com.guantang.cangkuonline.fragment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.activity.DDdetailActivity;
import com.guantang.cangkuonline.activity.MyOrderActivity.DDfilterInterface;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.eventbusBean.ObjectNine;
import com.guantang.cangkuonline.fragment.AllOrderFragment.DDAdapter;
import com.guantang.cangkuonline.fragment.AllOrderFragment.GetDDInfo_1_0AsyncTask;
import com.guantang.cangkuonline.swipemenuXlistview.SwipeMenuXListView;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class DaiZhiXingFragment extends Fragment implements IXListViewListener,DDfilterInterface{
	
	private SwipeMenuXListView mSwipeMenuXListView;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private List<Map<String,Object>> mList = new ArrayList<Map<String,Object>>();
	private String[] str = { DataBaseHelper.ID,DataBaseHelper.Status, DataBaseHelper.DWName,
			DataBaseHelper.LXR, DataBaseHelper.OrderIndex,
			DataBaseHelper.Trades, DataBaseHelper.dirc, DataBaseHelper.Sqdt,
			DataBaseHelper.TEL, DataBaseHelper.zje, DataBaseHelper.yfje,
			DataBaseHelper.syje, DataBaseHelper.BZ, DataBaseHelper.sqr,
			DataBaseHelper.ReqDate, DataBaseHelper.DepName, DataBaseHelper.DWID,DataBaseHelper.sqrID};
	private String[] str2 = { "id","status", "dwName", "lxr", "orderIndex",
			"trades", "dirc", "sqdt", "tel", "zje", "yfje", "syje", "bz",
			"sqr", "ReqDate", "depName", "dwid","sqrID" };
	private DDAdapter mDDAdapter;
	private String sql = "";
	private String sqfromtime = "";
	private String sqtotime = "";
	private Context context;
	
	private Semaphore semaphore = new Semaphore(1);
	
	@Override
	public void onAttach(Activity activity) {
		// TODO �Զ����ɵķ������
		super.onAttach(activity);
		context = activity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		View view = inflater.inflate(R.layout.orderstatus_fragment_layout, null);
		mSwipeMenuXListView = (SwipeMenuXListView) view.findViewById(R.id.djlist);
		mSwipeMenuXListView.setXListViewListener(this);
		mSwipeMenuXListView.setPullLoadEnable(true);// ���ÿ��Լ��ظ���
		mSwipeMenuXListView.setPullRefreshEnable(true);// ���ÿ���ˢ��
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onActivityCreated(savedInstanceState);
		mDDAdapter = new DDAdapter(context, mList, R.layout.dditem_layout);
		mSwipeMenuXListView.setAdapter(mDDAdapter);
//		sqfromtime = formatter.format(System.currentTimeMillis());
//		sqtotime = formatter.format(System.currentTimeMillis());
		if(WebserviceImport.isOpenNetwork(context)){
			new GetDDInfo_1_0AsyncTask().execute("0",sqfromtime,sqtotime,sql);
		}else{
			Toast.makeText(context, "����δ����", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO �Զ����ɵķ������
		super.onDestroy();
	}
	
	class GetDDInfo_1_0AsyncTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO �Զ����ɵķ������
			String jsonString = WebserviceImport_more.GetDDInfo_1_0("Pending", params[0], "10", params[1], params[2], params[3], "-1");
			return jsonString;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO �Զ����ɵķ������
			super.onPostExecute(result);
			stopLoadface();
			try {
				JSONObject jsonObject = new JSONObject(result);
				semaphore.release();
				switch(jsonObject.getInt("Status")){
				case 1:
					parseData(jsonObject);
					break;
				case 2://���ݼ��������ʾ
					parseData(jsonObject);
					if(!mList.isEmpty()){
						Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}
	
	public void parseData(JSONObject jsonObject) throws JSONException{
		JSONObject dataJSONObject = jsonObject.getJSONObject("Data");
		JSONObject pendingJSONObject = dataJSONObject.getJSONObject("Pending");
		JSONArray dsJSONArray = pendingJSONObject.getJSONArray("ds");
		int Length = dsJSONArray.length();
		for(int i = 0; i<Length; i++){
			JSONObject myJSONObject = dsJSONArray.getJSONObject(i);
			Map<String,Object> map = new HashMap<String,Object>();
			for(int j =0;j<18;j++){
				map.put(str[j], myJSONObject.getString(str2[j]));
			}
			mList.add(map);
		}
		mDDAdapter.setData(mList);
	}
	
	class DDAdapter extends CommonAdapter<Map<String,Object>>{

		public DDAdapter(Context mContext, List<Map<String, Object>> mList,
				int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO �Զ����ɵĹ��캯�����
		}

		@Override
		public void convert(ViewHolder holder, final Map<String, Object> item) {
			// TODO �Զ����ɵķ������
			TextView ddhTxtView = holder.getView(R.id.ddhTxtView);
			TextView ddtypeTxtView = holder.getView(R.id.ddtypeTxtView);
			TextView dwTitleTxtView = holder.getView(R.id.dwTitleTxtView);
			TextView dwnameTxtView = holder.getView(R.id.dwnameTxtView);
			TextView yqdhsjTxtView = holder.getView(R.id.yqdhsjTxtView);
			TextView Icon_TxtView = holder.getView(R.id.Icon_TxtView);
			
			Object OrderIndexObject = item.get(DataBaseHelper.OrderIndex);
			ddhTxtView.setText(OrderIndexObject == null?"":OrderIndexObject.toString());
			
			String dricString = item.get(DataBaseHelper.dirc).toString();
			if(dricString != null){
				if(dricString.equals("0")){
					ddtypeTxtView.setText("�ɹ�����");
					dwTitleTxtView.setText("��Ӧ�̣�");
				}else if(dricString.equals("1")){
					ddtypeTxtView.setText("���۶���");
					dwTitleTxtView.setText("�ͻ����ƣ�");
				}
			}
			
			Object dwnameObject = item.get(DataBaseHelper.DWName);
			dwnameTxtView.setText(dwnameObject == null || dwnameObject.equals("null") ?"":dwnameObject.toString());
			
			Object yqdhsjObject = item.get(DataBaseHelper.ReqDate);
			yqdhsjTxtView.setText(yqdhsjObject == null || yqdhsjObject.equals("null") ?"":yqdhsjObject.toString().subSequence(0, 10));
			
			String statusString = item.get(DataBaseHelper.Status).toString();
			if(statusString != null ){
				if(statusString.equals("0")){
					Icon_TxtView.setText("�����");
				}else if(statusString.equals("1")){
					Icon_TxtView.setText("������");
				}else if(statusString.equals("2")||statusString.equals("3")){
					Icon_TxtView.setText("��ִ��");
				}else if(statusString.equals("5")||statusString.equals("7")){
					Icon_TxtView.setText("�����");
				}
			}
			holder.getConvertView().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO �Զ����ɵķ������
					Intent intent = new Intent(context,DDdetailActivity.class);
					intent.putExtra("map", (Serializable) item);
					startActivity(intent);
				}
			});
		}
		
	}

	@Override
	public void onRefresh() {
		// TODO �Զ����ɵķ������
		onLoadTime();
		if(WebserviceImport.isOpenNetwork(context)){
			mList.clear();
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new GetDDInfo_1_0AsyncTask().execute("0",sqfromtime,sqtotime,sql);
		}else{
			stopLoadface();
			Toast.makeText(context, "����δ����", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onLoadMore() {
		// TODO �Զ����ɵķ������
		onLoadTime();
		if(WebserviceImport.isOpenNetwork(context)){
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(mList.isEmpty()){
				new GetDDInfo_1_0AsyncTask().execute("0",sqfromtime,sqtotime,sql);
			}else{
				new GetDDInfo_1_0AsyncTask().execute(mList.get(mList.size()-1).get(DataBaseHelper.ID).toString(),sqfromtime,sqtotime,sql);
			}
		}else{
			stopLoadface();
			Toast.makeText(context, "����δ����", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onLoadTime() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.MONTH) + 1));
		String day = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.DAY_OF_MONTH)));
		String hour = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.HOUR_OF_DAY)));
		String minute = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.MINUTE)));
		String refreshDate = year + "-" + month + "-" + day + " " + hour + ":"
				+ minute;
		mSwipeMenuXListView.setRefreshTime(refreshDate);
	}

	/**
	 * ֹͣ������ض���
	 * */
	public void stopLoadface() {
		mSwipeMenuXListView.stopRefresh();
		mSwipeMenuXListView.stopLoadMore();
	}
	@Override
	public void execute(ObjectNine obj) {
		// TODO �Զ����ɵķ������
		sql = obj.getSql();
		sqfromtime = obj.getSqfromtime();
		sqtotime = obj.getSqtotime();
		mList.clear();
		if(WebserviceImport.isOpenNetwork(context)){
			new GetDDInfo_1_0AsyncTask().execute("0",sqfromtime,sqtotime,sql);
		}else{
			Toast.makeText(context, "����δ����", Toast.LENGTH_SHORT).show();
		}
	}
	
}
