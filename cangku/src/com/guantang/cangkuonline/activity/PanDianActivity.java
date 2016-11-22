package com.guantang.cangkuonline.activity;

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

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class PanDianActivity extends Activity implements OnClickListener {
	private ImageButton backImgBtn, saveImgBtn;
	private TextView dateTextView, ckTextView, pandian_allTextView,
			pandian_winTextView, pandian_loseTextView;
	private EditText dhEditText, jbrEditText, bzEditText;
	private LinearLayout cklayout, addhpLayout, tongjilayout;
	private RelativeLayout pandian_edLayout;
	private Button commitBtn;
	private ImageView addhp1ImageView;
	private List<Map<String, Object>> ck_ls = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> getList = new ArrayList<Map<String, Object>>();
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseCheckMethod dm_cm = new DataBaseCheckMethod(this);
	private String[] str = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL, DataBaseHelper.ADDR,
			DataBaseHelper.INACT, DataBaseHelper.OUTACT, DataBaseHelper.BZ };
	
	private String[] str3 = { DataBaseHelper.HPID, DataBaseHelper.MID,
			DataBaseHelper.MVDDATE, DataBaseHelper.MSL, DataBaseHelper.MVDType,
			DataBaseHelper.DH, DataBaseHelper.BTKC, DataBaseHelper.ATKC,
			DataBaseHelper.MVDirect, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.MVType, DataBaseHelper.CKID };
	
	private String[] strs1 = { DataBaseHelper.MVDH, DataBaseHelper.MVDT,
			DataBaseHelper.DWName, DataBaseHelper.JBR, DataBaseHelper.BZ,
			DataBaseHelper.mType, DataBaseHelper.actType,
			DataBaseHelper.DepName, DataBaseHelper.DepID, DataBaseHelper.CKMC,
			DataBaseHelper.CKID, DataBaseHelper.Lrr, DataBaseHelper.Lrsj,
			DataBaseHelper.DWID, DataBaseHelper.Details, DataBaseHelper.HPzj };
	private String[] strs2 = { "mvdh", "mvdt", "dwName", "jbr", "bz", "mType",
			"actType", "depName", "depId", "ckName", "ckid", "lrr", "lrsj",
			"dwid", "hpDetails", "hpzjz" };
	private String[] strs3 = { DataBaseHelper.HPID, DataBaseHelper.MVDDATE,
			DataBaseHelper.MSL, DataBaseHelper.MVDType, DataBaseHelper.DH,
			DataBaseHelper.MVDirect, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.MVType, DataBaseHelper.CKID, DataBaseHelper.BTKC,
			DataBaseHelper.ATKC };
	private String[] strs4 = { "hpid", "mvddt", "msl", "mdType", "mvddh",
			"mddirect", "dj", "zj", "dactType", "ckid", "Btkc", "Atkc" };
	
	private ExecutorService cacheThreadpool = Executors.newCachedThreadPool();
	private int ckid = -1, op_type = 0;
	private String ckmc, djid;
	private SimpleDateFormat formatter1, formatter2;
	private ProgressDialog pro_Dialog;
	private AlertDialog dialog;
	private SharedPreferences mSharePreferences;
	private DataBaseMethod dm = new DataBaseMethod(this);
	private DataBaseCheckMethod dm_ck=new DataBaseCheckMethod(this);
	/**
	 * �Ƿ�Ե��ݽ����˲����ı�־���Ա�ȷ���Ƿ񱣴�õ��ݣ�true��ʾ�����˸õ��ݣ�false��ʾû�в����õ��ݡ�
	 * */
	private Boolean operationFlag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pandian);
		
		mSharePreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		
		initView();
		init();
	}

	public void initView() {
		backImgBtn = (ImageButton) findViewById(R.id.back);
		saveImgBtn = (ImageButton) findViewById(R.id.save);
		dateTextView = (TextView) findViewById(R.id.date);
		ckTextView = (TextView) findViewById(R.id.ck);
		pandian_allTextView = (TextView) findViewById(R.id.pandian_all);
		pandian_winTextView = (TextView) findViewById(R.id.pandian_win);
		pandian_loseTextView = (TextView) findViewById(R.id.pandian_lose);
		dhEditText = (EditText) findViewById(R.id.dh);
		jbrEditText = (EditText) findViewById(R.id.jbr);
		bzEditText = (EditText) findViewById(R.id.bz);
		cklayout = (LinearLayout) findViewById(R.id.cklayout);
		addhpLayout = (LinearLayout) findViewById(R.id.addhp);
		pandian_edLayout = (RelativeLayout) findViewById(R.id.pandian_ed);
		commitBtn = (Button) findViewById(R.id.commitBtn);
		addhp1ImageView = (ImageView) findViewById(R.id.addhp1);
		tongjilayout = (LinearLayout) findViewById(R.id.tongjilayout);
		
		dhEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO �Զ����ɵķ������
				dm_ck.update_movem(DataBaseHelper.MVDH, s.toString().trim().replace("'", ""), djid);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO �Զ����ɵķ������
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO �Զ����ɵķ������
//				if(s.toString().indexOf("'")>-1){
//					int pos=s.toString().indexOf("'");
//					s.delete(pos,pos+1);
//				}
				if(!s.toString().equals("")){
					operationFlag=true;
				}
				s.toString().replace("'", "");
			}
		});
		
		jbrEditText.setText(MyApplication.getInstance().getSharedPreferences().getString(ShareprefenceBean.USERNAME, ""));
		jbrEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO �Զ����ɵķ������
				dm_ck.update_movem(DataBaseHelper.JBR, s.toString().trim().replace("'", ""), djid);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO �Զ����ɵķ������

			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO �Զ����ɵķ������
//				if(s.toString().indexOf("'")>-1){
//					int pos=s.toString().indexOf("'");
//					s.delete(pos,pos+1);
//					
//				}
				if(!s.toString().equals("")){
					operationFlag=true;
				}
				s.toString().replace("'", "");
			}
		});
		bzEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO �Զ����ɵķ������
				dm_ck.update_movem(DataBaseHelper.BZ, s.toString().trim().replace("'", ""), djid);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO �Զ����ɵķ������
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO �Զ����ɵķ������
//				if(s.toString().indexOf("'")>-1){
//					int pos=s.toString().indexOf("'");
//					s.delete(pos,pos+1);
//				}
				if(!s.toString().equals("")){
					operationFlag=true;
				}
				s.toString().replace("'", "");
			}
		});
		backImgBtn.setOnClickListener(this);
		saveImgBtn.setOnClickListener(this);
		dateTextView.setOnClickListener(this);
		cklayout.setOnClickListener(this);
		addhpLayout.setOnClickListener(this);
		pandian_edLayout.setOnClickListener(this);
		commitBtn.setOnClickListener(this);
		addhp1ImageView.setOnClickListener(this);
		tongjilayout.setOnClickListener(this);
	}

	public void init() {
		formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (dm_op.searchUncompleteDJ(0).equals("")
				|| dm_op.searchUncompleteDJ(0) == null) {
			dateTextView.setText(formatter1.format(new Date(System
					.currentTimeMillis())));
			dm_op.insert_into(DataBaseHelper.TB_MoveM, new String[]{DataBaseHelper.MVDT,DataBaseHelper.mType},
					new String[]{formatter1.format(new Date(System.currentTimeMillis())),"0"});
			djid = dm_op.GtMax_DJID();
			dm_op.Update_DJtype(djid, 0);// type 0����û�б��棬1�����ϴ��ͱ��汾�أ�-1�����汾��û�ϴ�
			
			//��û��������֮ǰ���ȸ����ݿ���Ӧ�ֶθ�ֵ��������ֶ�Ϊ��
			dm_ck.update_movem(DataBaseHelper.MVDH, "", djid);
			dm_ck.update_movem(DataBaseHelper.JBR, "", djid);
			dm_ck.update_movem(DataBaseHelper.BZ, "", djid);
			
			//�ж����ֻ��һ���ֿ⣬��Ĭ����ʾ�ֿ⡣
			if(ck_ls.size()==1){
				Map<String, Object> map = ck_ls.get(0);
				ckmc = map.get(DataBaseHelper.CKMC).toString();
				ckTextView.setText(ckmc);
				ckid = Integer.parseInt((String) map.get(DataBaseHelper.ID));
				dm_ck.update_movem(DataBaseHelper.CKMC, ckmc, djid);
				dm_ck.update_movem(DataBaseHelper.CKID, map.get(DataBaseHelper.ID).toString(), djid);
			}
		} else {
			djid=dm_op.searchUncompleteDJ(0);
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setMessage("��δ����̵㵥�ݣ��Ƿ�����ϴε��ݣ�");
			builder1.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dm_op.Del_Moved(djid);
							dm_op.Del_Movem(djid);
							dateTextView.setText(formatter1.format(new Date(System
									.currentTimeMillis())));
							dm_op.insert_into(DataBaseHelper.TB_MoveM,
									new String[] { DataBaseHelper.MVDT,DataBaseHelper.mType },
									new String[] { formatter1.format(new Date(System
											.currentTimeMillis())), "0"});
							djid = dm_op.GtMax_DJID();
							dm_op.Update_DJtype(djid, 0);// type 0����û�б��棬1�����ϴ��ͱ��汾�أ�-1�����汾��û�ϴ�
							
							addhpLayout.setVisibility(View.VISIBLE);
							pandian_edLayout.setVisibility(View.GONE);
							dialog.dismiss();
						}

					});
			builder1.setPositiveButton("����",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO �Զ����ɵķ������
//							commitBtn.setVisibility(View.VISIBLE);
							List<Map<String, Object>> list1=dm_op.Gt_Movem(djid, strs1);
							Map<String, Object> map1=list1.get(0);
							if(map1.get(DataBaseHelper.MVDH)==null||map1.get(DataBaseHelper.MVDH).toString().equals("")){
								dhEditText.setText("");
							}else{
								operationFlag=true;
								dhEditText.setText(map1.get(DataBaseHelper.MVDH).toString());
							}
							if(map1.get(DataBaseHelper.JBR)==null||map1.get(DataBaseHelper.JBR).toString().equals("")){
								jbrEditText.setText(MyApplication.getInstance().getSharedPreferences().getString(ShareprefenceBean.USERNAME, ""));
							}else{
								operationFlag=true;
								jbrEditText.setText(map1.get(DataBaseHelper.JBR).toString());
							}
							if(map1.get(DataBaseHelper.BZ)==null||map1.get(DataBaseHelper.BZ).toString().equals("")){
								bzEditText.setText("");
							}else{
								operationFlag=true;
								bzEditText.setText(map1.get(DataBaseHelper.BZ).toString());
							}
							if(map1.get(DataBaseHelper.CKMC)==null||map1.get(DataBaseHelper.CKMC).toString().equals("")){
								ckTextView.setText("");
							}else{
								operationFlag=true;
								ckTextView.setText(map1.get(DataBaseHelper.CKMC).toString());
								ckid=Integer.parseInt(map1.get(DataBaseHelper.CKID).toString());
							}
							dateTextView.setText(formatter1.format(new Date(System
									.currentTimeMillis())));
							dialog.dismiss();
						}
					});
			builder1.create().show();
		}
	}

	@Override
	protected void onStart() {
		// TODO �Զ����ɵķ������
		double countWin = 0,countKui=0;
		getList = dm_op.Gt_Moved(djid, str3);
		if(getList.size()>0){
			addhpLayout.setVisibility(View.GONE);
			pandian_edLayout.setVisibility(View.VISIBLE);
		}else{
			addhpLayout.setVisibility(View.VISIBLE);
			pandian_edLayout.setVisibility(View.GONE);
		}
		pandian_allTextView.setText(String.valueOf(getList.size()));
		Map<String,Object> map = new HashMap<String,Object>();
		for (int i = 0; i < getList.size(); i++) {
			map=getList.get(i);
			if(map.get(DataBaseHelper.MVType).toString().equals("��Ӯ")){
				if ((!map.get(DataBaseHelper.MSL).toString().equals(""))
						|| (map.get(DataBaseHelper.MSL) != null)) {
					countWin += Float.parseFloat(map.get(DataBaseHelper.MSL).toString());
				}
			}else if(map.get(DataBaseHelper.MVType).toString().equals("�̿�")){
				if ((!map.get(DataBaseHelper.MSL).toString().equals(""))
						|| (map.get(DataBaseHelper.MSL) != null)) {
					countKui +=Float.parseFloat(map
							.get(DataBaseHelper.MSL).toString());
				}
			}
			
		}
		pandian_winTextView.setText(DecimalsHelper.Transfloat(countWin,MyApplication.getInstance().getNumPoint()));
		pandian_loseTextView.setText(DecimalsHelper.Transfloat(countKui,MyApplication.getInstance().getNumPoint()));
		super.onStart();
	}
	
	public void savepdDJ() {
		String details2 ="",hpstr = "";
		if(!getList.isEmpty()){
			hpstr = dm.Gethp(new String[]{DataBaseHelper.HPMC}, getList.get(0).get(DataBaseHelper.HPID).toString()).get(0).get(DataBaseHelper.HPMC).toString();
			details2 = hpstr + " ��"+ pandian_allTextView.getText().toString() + "�ֻ�Ʒ";
		}
		if(pandian_allTextView.getText().toString().equals("1")){
			details2 = hpstr;
		}else if (pandian_allTextView.getText().toString().equals("0")){
			details2 = "�޻�Ʒ��ϸ";
		}else{
			details2 = hpstr + " ��"
					+ pandian_allTextView.getText().toString() + "�ֻ�Ʒ";
		}
		String time = formatter2.format(new Date(System.currentTimeMillis()));
		if(dhEditText.getText().toString()==null){
			dhEditText.setText("");
		}
		if(jbrEditText.getText().toString()==null){
			jbrEditText.setText("");
		}
		if(bzEditText.getText().toString()==null){
			bzEditText.setText("");
		}
		if(ckTextView.getText().toString()==null){
			ckTextView.setText("");
		}
		dm_ck.Check_save_movem(djid,dateTextView.getText().toString(),details2,dhEditText.getText().toString(),
				jbrEditText.getText().toString(),bzEditText.getText().toString(),ckTextView.getText().toString(),
				ckid,details2,mSharePreferences.getString(ShareprefenceBean.USERNAME, ""),time,op_type);
		dm_ck.Check_save_moved(djid, dhEditText.getText().toString());
		
		//�޸�tb_ckkc�Ļ�Ʒ�������޸�tb_hp�Ļ�Ʒ������
		List<Map<String, Object>> mxList = new ArrayList<Map<String,Object>>();
		mxList = dm_op.Gt_Moved(djid, str3);
		if(mxList!=null){
			for(int i=0;i<mxList.size();i++){
				String hpidsString = mxList.get(i).get(DataBaseHelper.HPID).toString();
				float atkcString = Float.valueOf(mxList.get(i).get(DataBaseHelper.ATKC).toString());
				String mvdirectString = mxList.get(i).get(DataBaseHelper.MVDirect).toString();
				float mslString = Float.valueOf(mxList.get(i).get(DataBaseHelper.MSL).toString());
//				dm_op.Del_CKKC(hpidsString, ckid);
//				dm_op.insert_ckkc(hpidsString, ckid, atkcString);
				if(dm_op.Gt_ckkc(hpidsString, ckid)!=null){
					dm_op.Update_CKKC(hpidsString, ckid, atkcString);
				}else{
					dm_op.insert_ckkc(hpidsString, ckid, atkcString);
				}
				if(mvdirectString.equals("1")){
					dm_op.UpdateCurrKc_byhpid(hpidsString, mslString, 1);
				}else if(mvdirectString.equals("2")){
					dm_op.UpdateCurrKc_byhpid(hpidsString, mslString, 2);
				}
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			if(operationFlag || Integer.parseInt(pandian_allTextView.getText().toString())!=0){
				exitDJDialog();
			}else{
				dm_op.Del_Moved(djid);
				dm_op.Del_Movem(djid);
				djid = "";
				finish();
			}
			break;
		case R.id.commitBtn:
			if (ckid < 0) {
				Toast.makeText(this, "��ѡ��ֿ�", Toast.LENGTH_SHORT).show();
			} else {
				if(pandian_allTextView.getText().toString().equals("0")){
					Toast.makeText(PanDianActivity.this, "������ӻ�Ʒ", Toast.LENGTH_SHORT).show();
				}else{
					if(mSharePreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
						if(WebserviceImport.isOpenNetwork(this)){
							savepdDJ();
							pro_Dialog = ProgressDialog.show(this, null, "�����ϴ����ݡ���");
							cacheThreadpool.execute(addRun);
						}else{
							Toast.makeText(this, "����δ����", Toast.LENGTH_SHORT).show();
						}
					}else if(mSharePreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
						savepdDJ();
						dm_op.Update_DJtype(djid, -1);
						Toast.makeText(this, "�����ѱ��棬��������ģʽ���ϴ�", Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			}
			break;
		case R.id.date:
			Calendar calendar = Calendar.getInstance();
			DatePickerDialog dateDialog = new DatePickerDialog(this,
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO �Զ����ɵķ������
							dateTextView.setText(year+ "-"+ new DecimalFormat("00").format(monthOfYear + 1)+ "-"+ new DecimalFormat("00").format(dayOfMonth));
							dm_ck.update_movem(DataBaseHelper.MVDT,year+ "-"+ new DecimalFormat("00").format(monthOfYear + 1)+ "-"+ new DecimalFormat("00").format(dayOfMonth), djid);
							operationFlag=true;
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog.show();
			break;
		case R.id.cklayout:
			final String[] ss2;
			ck_ls = dm_op.Gt_ck(str);
			if (ck_ls != null) {
				ss2 = new String[ck_ls.size()];
				for (int j = 0; j < ck_ls.size(); j++) {
					ss2[j] = (String) ck_ls.get(j).get(DataBaseHelper.CKMC);
				}
				AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
				builder2.setTitle("��ѡ��ֿ�");
				builder2.setSingleChoiceItems(ss2, -1,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (ck_ls.get(which).get(DataBaseHelper.ID) != null) {
									ckTextView.setText(ss2[which]);
									ckid = Integer.parseInt((String) ck_ls.get(
											which).get(DataBaseHelper.ID));
									ckmc = (String) ck_ls.get(which).get(
											DataBaseHelper.CKMC);
									dm_ck.update_movem(DataBaseHelper.CKMC, ckmc, djid);
									dm_ck.update_movem(DataBaseHelper.CKID, ck_ls.get(
											which).get(DataBaseHelper.ID).toString(), djid);
									operationFlag=true;
								}else{
									Toast.makeText(PanDianActivity.this, "�ֿ�id��ȡ����", Toast.LENGTH_SHORT).show();
								}
								dialog.dismiss();
							}
						});
				builder2.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				builder2.create().show();
			}else{
				Toast.makeText(this, "�޲ֿ���Ϣ��û�и��²ֿ���Ϣ", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.addhp:
			if (ckTextView.getText().toString().equals("")) {
				Toast.makeText(this, "����ѡ��ֿ�", Toast.LENGTH_SHORT)
						.show();
			} else {
				
//				if(dhEditText.getText().toString()==null){
//					dhEditText.setText("");
//				}
//				if(jbrEditText.getText().toString()==null){
//					jbrEditText.setText("");
//				}
//				if(bzEditText.getText().toString()==null){
//					bzEditText.setText("");
//				}
//				if(ckTextView.getText().toString()==null){
//					ckTextView.setText("");
//				}
//				dm_ck.Check_savePDCK_movem(djid, dhEditText.getText().toString(), jbrEditText.getText().toString(),
//						bzEditText.getText().toString(), ckTextView.getText().toString(), ckid);
				intent.setClass(this, HP_choseActivity.class);
				intent.putExtra("dh", dhEditText.getText().toString());
				intent.putExtra("djid", djid);
				intent.putExtra("ckid", ckid);
				intent.putExtra("op_type", 0);
				intent.putExtra("dacttype", "");
				startActivityForResult(intent, 1);
			}
			break;
		case R.id.addhp1:
			if (ckTextView.getText().toString().equals("")) {
				Toast.makeText(this, "����ѡ��ֿ�", Toast.LENGTH_SHORT)
						.show();
			} else {
				
//				if(dhEditText.getText().toString()==null){
//					dhEditText.setText("");
//				}
//				if(jbrEditText.getText().toString()==null){
//					jbrEditText.setText("");
//				}
//				if(bzEditText.getText().toString()==null){
//					bzEditText.setText("");
//				}
//				if(ckTextView.getText().toString()==null){
//					ckTextView.setText("");
//				}
//				dm_ck.Check_savePDCK_movem(djid, dhEditText.getText().toString(), jbrEditText.getText().toString(),
//						bzEditText.getText().toString(), ckTextView.getText().toString(), ckid);
				intent.setClass(this, HP_choseActivity.class);
				intent.putExtra("dh", dhEditText.getText().toString());
				intent.putExtra("djid", djid);
				intent.putExtra("ckid", ckid);
				intent.putExtra("op_type", 0);
				intent.putExtra("dacttype", "");
				startActivityForResult(intent, 1);
			}
			break;
		case R.id.tongjilayout:
			intent.setClass(this, DJ_detailActivity.class);
			intent.putExtra("dh", dhEditText.getText().toString());
			intent.putExtra("ckid", ckid);
			intent.putExtra("djid", djid);
			intent.putExtra("op_type", 0);
			intent.putExtra("dacttype", "");
			startActivity(intent);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO �Զ����ɵķ������
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1){
			if(resultCode==1){
				addhpLayout.setVisibility(View.GONE);
				pandian_edLayout.setVisibility(View.VISIBLE);
			}
		}
	}


	private Runnable addRun = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			String[] values_movem=new String[strs1.length];
			List<Map<String, Object>> ls2=dm_op.Gt_Movem(djid, strs1);
			if(ls2!=null){
				for(int j=0;j<strs1.length;j++){
					values_movem[j]=(String) ls2.get(0).get(strs1[j]);
				}
			}
			List<Map<String, Object>> lss=dm_op.Gt_Moved(djid, strs3);
			String[] values_moved=new String[lss.size()*strs3.length];
			for(int j=0;j<lss.size();j++){
				for(int n=0;n<strs3.length;n++){
					values_moved[j*strs3.length+n]=(String) lss.get(j).get(strs3[n]);
				}
			}
			int flag;
			try{
				String type=(String) ls2.get(0).get(DataBaseHelper.mType);
				switch(Integer.parseInt(type)){
				case 0:
					flag=WebserviceImport.AddDJ(values_movem, strs2, values_moved, strs4,WebserviceHelper.AddPD,mSharePreferences);
					break;
					default:
						flag=-1;
						break;
				}
			}catch(Exception e){
				flag=-1;
			}
			if (flag > 0) {
				dm_op.Update_DJtype(djid, 1);// type
												// 0����û�б��棬1�����ϴ��ͱ��汾�أ�-1�����汾��û�ϴ�
			} else {
				dm_op.Update_DJtype(djid, -1);
			}
			msg.what=flag;
			msg.setTarget(mHandler2);
            mHandler2.sendMessage(msg);
		}
	};
	private Handler mHandler2 = new Handler(){
		 public void handleMessage(Message msg){
			 pro_Dialog.dismiss();
				if(msg.what>0){
//					if (mSharePreferences.getBoolean(
//							ShareprefenceBean.FIRST_PROMPT_DJ, true)) {
//						AlertDialog.Builder builder = new AlertDialog.Builder(
//								PanDianActivity.this);
//						builder.setMessage("���ϴ��ĵ���������ҳ����˵��ݡ�");
//						builder.setNegativeButton("ȷ��",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										// TODO Auto-generated method stub
//										djid = "";
//										finish();
//										dialog.dismiss();
//									}
//								});
//						builder.setPositiveButton("������ʾ",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										// TODO Auto-generated method stub
//										mSharePreferences
//												.edit()
//												.putBoolean(
//														ShareprefenceBean.FIRST_PROMPT_DJ,
//														false).commit();
//										djid = "";
//										finish();
//										dialog.dismiss();
//									}
//								});
//						builder.create().show();
//					}else{
//						Toast.makeText(PanDianActivity.this , "�����ϴ��ɹ�", Toast.LENGTH_SHORT).show();
//						djid = "";
//						finish();
//					}
					Toast.makeText(PanDianActivity.this , "�����ϴ��ɹ�", Toast.LENGTH_SHORT).show();
					djid = "";
					finish();
				}else if(msg.what==-502){
					AlertDialog.Builder builder=new AlertDialog.Builder(PanDianActivity.this);
					builder.setMessage("����������,���޸��̵㵥�����ϴ���");
					builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO �Զ����ɵķ������
							dialog.dismiss();
						}
					});
					builder.setPositiveButton("�޸�������", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO �Զ����ɵķ������
							Intent intent = new Intent(PanDianActivity.this, DJMXCheckActivity.class);
							intent.putExtra("op_type", op_type);
							intent.putExtra("djid", djid);
							intent.putExtra("ckid", ckid);
							intent.putExtra("dh", dhEditText.getText().toString());
							intent.putExtra("dacttype", "");
							startActivity(intent);
						}
					});
					builder.create().show();
				}else{
						AlertDialog.Builder builder=new AlertDialog.Builder(PanDianActivity.this);
						int ls=msg.what;
						switch(msg.what){
						case -101:
							builder.setMessage("�ֿ���Ϣ����,����²ֿ���Ϣ�����ϴ���");
							break;
						case -102:
							builder.setMessage("�������������,����²�����Ϣ�����ϴ���");
							break;
						case -103:
							builder.setMessage("�����Ѵ���,���޸ĵ��ź����ϴ���");
							break;
						case -104:
							builder.setMessage("�������ڴ���,��ֹ���뵥��,���޸ĵ��ݺ����ϴ���");
							break;
						case -500:
							builder.setMessage("��治��,��ֹ����,���޸ĵ��ݺ����ϴ���");
							break;
						case -3:
							builder.setMessage("�ʺ���Ϣ��֤����");
							break;
						case -11:
							builder.setMessage("������쳣��");
							break;
						case -12:
							builder.setMessage("���ݽ����쳣��");
							break;
						case -2:
							builder.setMessage("Ȩ�޲���,�����ϴ����ݡ�");
							break;
						case -501:
							builder.setMessage("��Ʒ���ⲻ�㣬���޸ĵ������ϴ�");
							break;
							default:
								break;
						}
						builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						builder.create().show();
				}
		        }
	 };
	 public void exitDJDialog(){
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			View view = LayoutInflater.from(this).inflate(R.layout.dialogbutton, null);
			Button exit_unsaveBtn = (Button) view.findViewById(R.id.exit_unsaveBtn);
			Button exit_saveBtn = (Button) view.findViewById(R.id.exit_saveBtn);
			Button stayBtn = (Button) view.findViewById(R.id.stayBtn);
			exit_unsaveBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO �Զ����ɵķ������
					dm_op.Del_Moved(djid);
					dm_op.Del_Movem(djid);
					djid = "";
					dialog.dismiss();
					finish();
				}
			});
			exit_saveBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO �Զ����ɵķ������
					dialog.dismiss();
					finish();
				}
			});
			stayBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO �Զ����ɵķ������
					dialog.dismiss();
				}
			});
			builder1.setView(view);
			builder1.setTitle("�Ƿ�Ҫ�˳���");
			dialog = builder1.create();
			dialog.show();
		}
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO �Զ����ɵķ������
		 if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			 if(operationFlag || Integer.parseInt(pandian_allTextView.getText().toString())!=0){
				 exitDJDialog();
			 }else{
				 dm_op.Del_Moved(djid);
					dm_op.Del_Movem(djid);
					djid = "";
					finish();
			 }
			 
		 }
			return super.onKeyDown(keyCode, event);
		}
}
