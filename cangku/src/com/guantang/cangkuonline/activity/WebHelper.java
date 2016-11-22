package com.guantang.cangkuonline.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseImport;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.CommonDialog;
import com.guantang.cangkuonline.dialog.CommonDialog.DialogContentListener;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class WebHelper extends Activity implements OnClickListener,OnCheckedChangeListener {
	private ImageButton back, help;

	private Button ok;
	private TextView text, datehp,dateckkc, datelb, dateck, datetype, datedw;
	private CheckBox ck_hp,ck_ckkc, ck_dw, ck_type, ck_yg, ck_ck, ck_lb,radBtn;
	private ProgressDialog  pro_dialog;
	private CommonDialog mpDialog;
	private RelativeLayout all_layout;
	private List<Map<String, Object>> ls;
	private String[] str = { DataBaseHelper.ID, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.HPTM, DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC, DataBaseHelper.JLDW, DataBaseHelper.HPSX,
			DataBaseHelper.HPXX, DataBaseHelper.SCCS, DataBaseHelper.BZ,
			DataBaseHelper.RKCKJ, DataBaseHelper.CKCKJ, DataBaseHelper.CKCKJ2,
			DataBaseHelper.JLDW2, DataBaseHelper.BigNum, DataBaseHelper.RES1,
			DataBaseHelper.RES2, DataBaseHelper.RES3, DataBaseHelper.RES4,
			DataBaseHelper.RES5, DataBaseHelper.RES6, DataBaseHelper.LBS,
			DataBaseHelper.KCJE, DataBaseHelper.LBID, DataBaseHelper.LBIndex,
			DataBaseHelper.ImagePath };
	private String[] str1 = { "id", "HPMC", "HPBM", "HPTBM", "GGXH", "CurrKc",
			"JLDW", "HPSX", "HPXX", "SCCS", "BZ", "RKCKJ", "CKCKJ", "CKCKJ2",
			"JLDW2", "BigNum", "res1", "res2", "res3", "res4", "res5", "res6",
			"LBS", "kcje", "LBID", "LBIndex", "ImageUrl" };
	private String[] str2 = { DataBaseHelper.ID, DataBaseHelper.Name,
			DataBaseHelper.Lev, DataBaseHelper.PID, DataBaseHelper.Ord,
			DataBaseHelper.Sindex };
	private String[] str3 = { "ID", "name", "lev", "PID", "ord", "sindex" };
	private String[] str4 = { DataBaseHelper.ID, DataBaseHelper.DWName,
			DataBaseHelper.ADDR, DataBaseHelper.FAX, DataBaseHelper.YB,
			DataBaseHelper.Net, DataBaseHelper.LXR, DataBaseHelper.TEL,
			DataBaseHelper.Email, DataBaseHelper.BZ, DataBaseHelper.DWtype };
	private String[] str5 = { "id", "dwName", "addr", "fax", "yb", "www",
			"lxr", "tel", "email", "bz", "dwType" };
	private String[] str6 = { DataBaseHelper.GID, DataBaseHelper.ItemID,
			DataBaseHelper.ItemV, DataBaseHelper.Ord };
	private String[] str7 = { "GID", "ItemID", "ItemV", "ord" };
	private String[] str10 = { DataBaseHelper.GID, DataBaseHelper.ItemV };
	private String[] str11 = { "dirc", "name" };
	private String[] str12 = { DataBaseHelper.ID, DataBaseHelper.HPID,
			DataBaseHelper.CKID, DataBaseHelper.KCSL };
	private String[] str13 = { "id", "hpid", "ckid", "kcsl" };
	private String[] str14 = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL, DataBaseHelper.ADDR,
			DataBaseHelper.INACT, DataBaseHelper.OUTACT, DataBaseHelper.BZ };
	private String[] str15 = { "ID", "CKMC", "FZR", "TEL", "ADDR", "inact",
			"outact", "BZ" };
	private String[] str16 = { "ID", "name", "dwlevel", "PID", "dwOrder",
			"dwIndex" };
	private String[] str17 = { DataBaseHelper.ID, DataBaseHelper.Name,
			DataBaseHelper.depLevel, DataBaseHelper.PID,
			DataBaseHelper.depOrder, DataBaseHelper.depindex };
	private DataBaseOperateMethod dm_op;
	private DataBaseMethod dm;
	private SimpleDateFormat formatter;
	private SQLiteDatabase db;
	private Thread thread;
	private boolean flag = false;
	private int hp_num = 0, lb_num = 0, dw_num = 0, conf_num = 0, ck_num = 0,
			ckkc_num = 0;
	private SharedPreferences mSharedPreferences;
	
	private boolean stopflag = false; // true��ʾֹͣ���أ�false��ʾ��������
	private LinkedList<Runnable> loadRunnableList = new LinkedList<Runnable>();
	private Handler runnableHandler;
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(6);
	/**
	 * ����runnableHandler��ʼ�����
	 * */
	private volatile Semaphore runnableHandlerSemaphore = new Semaphore(1);
	/**
	 * �����߳�һ��һ��ִ��
	 * */
	private volatile Semaphore mSemaphore = new Semaphore(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webhelper);
		mSharedPreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initControl();
		init();
	}

	public void initControl() {
		back = (ImageButton) findViewById(R.id.back);
		help = (ImageButton) findViewById(R.id.help);

		ok = (Button) findViewById(R.id.gx);
		text = (TextView) findViewById(R.id.text);
		datehp = (TextView) findViewById(R.id.date_hp);
		dateckkc = (TextView) findViewById(R.id.date_ckkc);
		datelb = (TextView) findViewById(R.id.date_lb);
		datedw = (TextView) findViewById(R.id.date_dw);
		datetype = (TextView) findViewById(R.id.date_type);
		dateck = (TextView) findViewById(R.id.date_ck);
		ck_hp = (CheckBox) findViewById(R.id.ck_hp);
		ck_ckkc = (CheckBox) findViewById(R.id.ck_ckkc);
		ck_dw = (CheckBox) findViewById(R.id.ck_dw);
		ck_type = (CheckBox) findViewById(R.id.ck_type);
		ck_yg = (CheckBox) findViewById(R.id.ck_yg);
		ck_ck = (CheckBox) findViewById(R.id.ck_ck);
		ck_lb = (CheckBox) findViewById(R.id.ck_lb);
		all_layout = (RelativeLayout) findViewById(R.id.all_layout);
		radBtn = (CheckBox) findViewById(R.id.radBtn);
		
		
		
		radBtn.setOnCheckedChangeListener(this);
		ck_hp.setOnCheckedChangeListener(this);
		ck_ckkc.setOnCheckedChangeListener(this);
		ck_dw.setOnCheckedChangeListener(this);
		ck_type.setOnCheckedChangeListener(this);
		ck_yg.setOnCheckedChangeListener(this);
		ck_ck.setOnCheckedChangeListener(this);
		ck_lb.setOnCheckedChangeListener(this);
		
		all_layout.setOnClickListener(this);
		back.setOnClickListener(this);
		help.setOnClickListener(this);
		ok.setOnClickListener(this);

		String time = mSharedPreferences.getString(
				ShareprefenceBean.UPDATE_TIME_HP, "δ��������");
		datehp.setText(time);
		time = mSharedPreferences.getString(ShareprefenceBean.UPDATE_TIME_CKKC,
				"δ��������");
		dateckkc.setText(time);
		time = mSharedPreferences.getString(ShareprefenceBean.UPDATE_TIME_LB,
				"δ��������");
		datelb.setText(time);
		time = mSharedPreferences.getString(ShareprefenceBean.UPDATE_TIME_DW,
				"δ��������");
		datedw.setText(time);
		time = mSharedPreferences.getString(ShareprefenceBean.UPDATE_TIME_TYPE,
				"δ��������");
		datetype.setText(time);
		time = mSharedPreferences.getString(ShareprefenceBean.UPDATE_TIME_CK,
				"δ��������");
		dateck.setText(time);

	}

	public void init() {

		ls = new ArrayList<Map<String, Object>>();
		dm_op = new DataBaseOperateMethod(this);
		dm = new DataBaseMethod(this);
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.all_layout:
			if(radBtn.isChecked()){
				radBtn.setChecked(false);
			}else{
				radBtn.setChecked(true);
			}
			
			break;
		case R.id.dwname:
			// final FileDialog filedialog= new FileDialog(this);
			// filedialog.setOnClickListener(new
			// DialogInterface.OnClickListener(){
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// // TODO Auto-generated method stub
			// dialog.dismiss();
			// String result = filedialog.ed.getText().toString();
			// sp.edit().putString("dwname2", result).commit();
			// if(result.equals("")){
			// result=sp.getString("dwname", "");
			// }
			// WebserviceHelper.DwName=result;
			// dwname.setText(result);
			// }
			// });
			// filedialog.showdialog_edit();
			break;
		case R.id.gx:
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				if (WebserviceImport.isOpenNetwork(this)) {
					try {
						runnableHandlerSemaphore.acquire();
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
					new Thread(handleRunnable).start();
					mayLoadData();
				} else {
					Toast.makeText(this, "����δ���ӣ���������״��", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "���¼�˺�", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.help:
			AlertDialog.Builder builder = new AlertDialog.Builder(
					WebHelper.this);
			builder.setMessage("��ʾ��Ϊȷ��������ģʽ����������ȷ���������µģ�����ʹ������ģʽǰ�������ݡ�");
			builder.setNegativeButton("ȷ��",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			builder.create().show();
			break;
		default:
			break;
		}
	}

    private Runnable handleRunnable=new Runnable() {
		
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			Looper.prepare();
			runnableHandler = new Handler(){
				public void handleMessage(Message msg) {
					stopflag = false;
					switch(msg.what){
					case 0:
						
						mpDialog = new CommonDialog(WebHelper.this, R.layout.custom_progressbar_layout, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO �Զ����ɵķ������
								TextView titleTextView = mpDialog.getView(R.id.title);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								TextView percentTextView = mpDialog.getView(R.id.percentTextView);
								ProgressBar pregressbar = mpDialog.getView(R.id.mybar);
								
								titleTextView.setText("���ڸ��»�Ʒ��Ϣ");
								pregressbar.setMax(100);
								pregressbar.setProgress(0);
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO �Զ����ɵķ������
										stopflag = true;
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();
						
//						mpDialog = new ProgressDialog(WebHelper.this);
//						mpDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//						mpDialog.setTitle("���ڸ��»�Ʒ��Ϣ");
//						mpDialog.setMax(100);
//						mpDialog.setProgress(0);
//						mpDialog.setIndeterminate(false);
//						mpDialog.setCancelable(false);
//						mpDialog.setButton(-2, "ȡ��",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										stopflag = true;
//										dialog.cancel();
//									}
//								});
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 1:
						
						mpDialog = new CommonDialog(WebHelper.this, R.layout.custom_progressbar_layout, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO �Զ����ɵķ������
								TextView titleTextView = mpDialog.getView(R.id.title);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								TextView percentTextView = mpDialog.getView(R.id.percentTextView);
								ProgressBar pregressbar = mpDialog.getView(R.id.mybar);
								
								titleTextView.setText("���ڸ��²ֿ�����Ϣ");
								pregressbar.setMax(100);
								pregressbar.setProgress(0);
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO �Զ����ɵķ������
										stopflag = true;
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();
						
//						mpDialog = new ProgressDialog(WebHelper.this);
//						mpDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//						mpDialog.setTitle("���ڸ��²ֿ�����Ϣ");
//						mpDialog.setMax(100);
//						mpDialog.setProgress(0);
//						mpDialog.setIndeterminate(false);
//						mpDialog.setCancelable(false);
//						mpDialog.setButton(-2, "ȡ��",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										stopflag = true;
//										dialog.cancel();
//
//									}
//
//								});
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 2:
						
						mpDialog = new CommonDialog(WebHelper.this, R.layout.prompt_dialog_layout2, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO �Զ����ɵķ������
								TextView contentTextView = mpDialog.getView(R.id.content_txtview);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								contentTextView.setText("���ڸ��»�Ʒ����");
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO �Զ����ɵķ������
										stopflag = true;
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();
						
//						mpDialog = new ProgressDialog(WebHelper.this);
//						mpDialog.setMessage("���ڸ��»�Ʒ����");
//						mpDialog.setCancelable(false);
//						mpDialog.setButton(-2, "ȡ��",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										stopflag = true;
//										dialog.cancel();
//									}
//
//								});
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 3:
						
						mpDialog = new CommonDialog(WebHelper.this, R.layout.prompt_dialog_layout2, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO �Զ����ɵķ������
								TextView contentTextView = mpDialog.getView(R.id.content_txtview);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								contentTextView.setText("���ڸ���������λ");
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO �Զ����ɵķ������
										stopflag = true;
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();
						
//						mpDialog = new ProgressDialog(WebHelper.this);
//						mpDialog.setMessage("���ڸ���������λ");
//						mpDialog.setCancelable(false);
//						mpDialog.setButton(-2, "ȡ��",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										stopflag = true;
//										dialog.cancel();
//									}
//
//								});
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 4:
						
						mpDialog = new CommonDialog(WebHelper.this, R.layout.prompt_dialog_layout2, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO �Զ����ɵķ������
								TextView contentTextView = mpDialog.getView(R.id.content_txtview);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								contentTextView.setText("���ڸ���������λ");
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO �Զ����ɵķ������
										
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();
						
//						mpDialog = new ProgressDialog(WebHelper.this);
//						mpDialog.setMessage("���ڸ��²�������");
//						mpDialog.setCancelable(true);
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 5:
						
						mpDialog = new CommonDialog(WebHelper.this, R.layout.prompt_dialog_layout2, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO �Զ����ɵķ������
								TextView contentTextView = mpDialog.getView(R.id.content_txtview);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								contentTextView.setText("���ڸ��²ֿ���Ϣ");
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO �Զ����ɵķ������
										
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();
						
//						mpDialog = new ProgressDialog(WebHelper.this);
//						mpDialog.setMessage("���ڸ��²ֿ���Ϣ");
//						mpDialog.setCancelable(true);
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 6:
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					}
				};
			};
			runnableHandlerSemaphore.release();
			Looper.loop();
		}
	};
	
	private void mayLoadData() {
		try {
			if(runnableHandler==null){
				runnableHandlerSemaphore.acquire();
			}
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}	
		
		if(ck_hp.isChecked()){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			loadRunnableList.add(hpxinxiRunnable);
			msg.what=0;
			runnableHandler.sendMessage(msg);
		}
		if(ck_ckkc.isChecked()){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			loadRunnableList.add(ckkcRunnable);
			msg.what=1;
			runnableHandler.sendMessage(msg);
		}
		if(ck_lb.isChecked()){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			loadRunnableList.add(hpleixiRunnable);
			msg.what=2;
			runnableHandler.sendMessage(msg);
		}
		if(ck_dw.isChecked()){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			loadRunnableList.add(dwxinxiRunnable);
			msg.what=3;
			runnableHandler.sendMessage(msg);
		}
		if(ck_type.isChecked()){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			loadRunnableList.add(canshuxinxiRunnable);
			msg.what=4;
			runnableHandler.sendMessage(msg);
		}
		if(ck_ck.isChecked()){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			loadRunnableList.add(cangkuxinxiRunnable);
			msg.what=5;
			runnableHandler.sendMessage(msg);
		}
		
		Message msg = Message.obtain();
		try {
			mSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		loadRunnableList.add(tongjiRunnable);
		msg.what=6;
		runnableHandler.sendMessage(msg);
		
		runnableHandlerSemaphore.release();
	}
	
	private Handler myHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_HP,formatter.format(new Date(System.currentTimeMillis()))).commit();
//				Toast.makeText(WebHelper.this, "��Ʒ��Ϣ�������", Toast.LENGTH_SHORT).show();
				break;
			case -2:
				Toast.makeText(WebHelper.this, "��ȡ��Ʒ�����쳣", Toast.LENGTH_SHORT)
						.show();
				break;
			case -3:
				Toast.makeText(WebHelper.this, "��Ʒ��Ϣ�����쳣", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_CKKC,formatter.format(new Date(System.currentTimeMillis()))).commit();
//				Toast.makeText(WebHelper.this, "�ֿ�����Ϣ�������", Toast.LENGTH_SHORT).show();
				break;
			case -9:
				Toast.makeText(WebHelper.this, "��ȡ�ֿ�����Ϣ�����쳣", Toast.LENGTH_SHORT).show();
				break;
			case -7:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_CKKC,formatter.format(new Date(System.currentTimeMillis()))).commit();
				Toast.makeText(WebHelper.this, "�ֿ�����Ϣ�����쳣", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_LB,formatter.format(new Date(System.currentTimeMillis()))).commit();
//				Toast.makeText(WebHelper.this, "��Ʒ���͸������", Toast.LENGTH_SHORT).show();
				break;
			case -4:
				Toast.makeText(WebHelper.this, "��Ʒ���������쳣", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_DW,formatter.format(new Date(System.currentTimeMillis()))).commit();
//				Toast.makeText(WebHelper.this, "������λ�������", Toast.LENGTH_SHORT).show();
				break;
			case -5:
				Toast.makeText(WebHelper.this, "������λ�����쳣", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_TYPE,formatter.format(new Date(System.currentTimeMillis()))).commit();
				Toast.makeText(WebHelper.this, "�����������", Toast.LENGTH_SHORT).show();
				break;
			case -6:
				Toast.makeText(WebHelper.this, "���������쳣", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_CK,formatter.format(new Date(System.currentTimeMillis()))).commit();
//				Toast.makeText(WebHelper.this, "�ֿ���Ϣ�������", Toast.LENGTH_SHORT).show();
				break;
			case -8:
				Toast.makeText(WebHelper.this, "�ֿ���Ϣ�����쳣", Toast.LENGTH_SHORT).show();
				break;
			case 6:
				String time = mSharedPreferences.getString(
						ShareprefenceBean.UPDATE_TIME_HP, "δ��������");
						datehp.setText(time);
						time = mSharedPreferences.getString(
						ShareprefenceBean.UPDATE_TIME_CKKC,"δ��������");
						dateckkc.setText(time);
						time = mSharedPreferences.getString(
						ShareprefenceBean.UPDATE_TIME_LB, "δ��������");
						datelb.setText(time);
						time = mSharedPreferences.getString(
						ShareprefenceBean.UPDATE_TIME_DW, "δ��������");
						datedw.setText(time);
						time = mSharedPreferences.getString(
						ShareprefenceBean.UPDATE_TIME_TYPE, "δ��������");
						datetype.setText(time);
						time = mSharedPreferences.getString(
						ShareprefenceBean.UPDATE_TIME_CK, "δ��������");
						dateck.setText(time);
				break;
			}
		};
	};
	
	private Runnable hpxinxiRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			Message msg = Message.obtain();
			try {
				db = DataBaseImport.getInstance(WebHelper.this).getReadableDatabase();
				db.beginTransaction();
				dm_op.del_tableContent(DataBaseHelper.TB_HP, db);
				int total = WebserviceImport.Get_Total(
						WebserviceHelper.GetHp_Total, -1, mSharedPreferences);
				int leave = WebserviceImport.GetHp_Total_Leave("-1",
						mSharedPreferences);
				String id = "0", date = "";
				hp_num = 0;
				String[] values = new String[str.length];
				if (total >= 0 && (leave > 0 || leave == 0)) {
						while (leave != 0) {
							if (stopflag == true) {
								break;
							}
							if (total == leave) {
								ls = WebserviceImport.GetHpInfo_top(200, "0", 1,
										-1, str, str1, mSharedPreferences);
								if (!ls.isEmpty()) {
									id = (String) ls.get(ls.size() - 1).get(
											DataBaseHelper.ID);
									leave = WebserviceImport.GetHp_Total_Leave(id,
											mSharedPreferences);
//									mpDialog.setProgress(GtProgress(leave, total));
									((ProgressBar) mpDialog.getView(R.id.mybar)).setProgress(GtProgress(leave, total));
									date = formatter.format(new Date(System
											.currentTimeMillis()));
									for (int i = 0; i < ls.size(); i++) {
										for (int j = 0; j < str.length; j++) {

											if (RightsHelper.isHaveRight(
													RightsHelper.system_cw,
													mSharedPreferences) == true) {
												values[j] = (String) ls.get(i).get(
														str[j]);
											} else {
												if (str[j]
														.equals(DataBaseHelper.RKCKJ)
														|| str[j]
																.equals(DataBaseHelper.CKCKJ)
														|| str[j]
																.equals(DataBaseHelper.CKCKJ2)
														|| str[j]
																.equals(DataBaseHelper.KCJE)) {
													values[j] = "";
												} else {
													values[j] = (String) ls.get(i)
															.get(str[j]);
												}
											}
										}
										dm_op.insert_into_fromfile(
												DataBaseHelper.TB_HP, str, values,
												db, date);
										hp_num++;
									}
								}
							} else {
								ls = WebserviceImport.GetHpInfo_top(200, id, 1, -1,
										str, str1, mSharedPreferences);
								if (!ls.isEmpty()) {
									id = (String) ls.get(ls.size() - 1).get(
											DataBaseHelper.ID);
									leave = WebserviceImport.GetHp_Total_Leave(id,
											mSharedPreferences);
//									mpDialog.setProgress(GtProgress(leave, total));
									((ProgressBar) mpDialog.getView(R.id.mybar)).setProgress(GtProgress(leave, total));
									date = formatter.format(new Date(System
											.currentTimeMillis()));
									for (int i = 0; i < ls.size(); i++) {
										for (int j = 0; j < str.length; j++) {
											if (RightsHelper.isHaveRight(
													RightsHelper.system_cw,
													mSharedPreferences) == true) {
												values[j] = (String) ls.get(i).get(
														str[j]);
											} else {
												if (str[j]
														.equals(DataBaseHelper.RKCKJ)
														|| str[j]
																.equals(DataBaseHelper.CKCKJ)
														|| str[j]
																.equals(DataBaseHelper.CKCKJ2)
														|| str[j]
																.equals(DataBaseHelper.KCJE)) {
													values[j] = "";
												} else {
													values[j] = (String) ls.get(i)
															.get(str[j]);
												}
											}
										}
										dm_op.insert_into_fromfile(
												DataBaseHelper.TB_HP, str, values,
												db, date);
										hp_num++;
									}
								}
							}
						}
					msg.what = 0;
				} else if (total <= -1 || leave < 0) {
					msg.what = -2;
				} else {
					msg.what = 0;
				}
			} catch (Exception e) {
				msg.what = -3;
			} finally {
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
			
		}
	};

	private Runnable ckkcRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			Message msg = Message.obtain();
			try{
				db = DataBaseImport.getInstance(WebHelper.this).getReadableDatabase();
				db.beginTransaction();
				dm_op.del_tableContent(DataBaseHelper.TB_CKKC, db);
				int total = WebserviceImport
						.GetCKKC_Total(mSharedPreferences);
				int leave = WebserviceImport.GetCKKC_Total_Leave("-1",
						mSharedPreferences);
				String id = "0";
				String[] values = new String[str12.length];
				if (total >= 0 && (leave > 0 || leave == 0)) {
						while (leave != 0) {
							if (stopflag == true) {
								break;
							}
							if (total == leave) {
								ls = WebserviceImport.GetCKKC_top(200,
										"0", str12, str13,
										mSharedPreferences);
								if (!ls.isEmpty()) {
									id = (String) ls.get(ls.size() - 1)
											.get(DataBaseHelper.ID);
									leave = WebserviceImport
											.GetCKKC_Total_Leave(id,
													mSharedPreferences);
//									mpDialog.setProgress(GtProgress(
//											leave, total));
									((ProgressBar) mpDialog.getView(R.id.mybar)).setProgress(GtProgress(leave, total));
									for (int i = 0; i < ls.size(); i++) {
										for (int j = 0; j < str12.length; j++) {
											values[j] = (String) ls
													.get(i).get(
															str12[j]);
										}
										dm_op.insert_into_fromfile(
												DataBaseHelper.TB_CKKC,
												str12, values, db);
										ckkc_num++;
									}
								}
							} else {
								ls = WebserviceImport.GetCKKC_top(200,
										id, str12, str13,
										mSharedPreferences);
								if (!ls.isEmpty()) {
									id = (String) ls.get(ls.size() - 1)
											.get(DataBaseHelper.ID);
									leave = WebserviceImport
											.GetCKKC_Total_Leave(id,
													mSharedPreferences);
//									mpDialog.setProgress(GtProgress(
//											leave, total));
									((ProgressBar) mpDialog.getView(R.id.mybar)).setProgress(GtProgress(leave, total));
									for (int i = 0; i < ls.size(); i++) {
										for (int j = 0; j < str12.length; j++) {
											values[j] = (String) ls
													.get(i).get(
															str12[j]);
										}
//										dm_op.Del_CKKC(
//												(String) ls
//														.get(i)
//														.get(DataBaseHelper.HPID),
//												(String) ls
//														.get(i)
//														.get(DataBaseHelper.CKID),
//												db);
										dm_op.insert_into_fromfile(
												DataBaseHelper.TB_CKKC,
												str12, values, db);
										ckkc_num++;
									}

								}
							}
						}
						msg.what=1;
				} else {
					msg.what = -9;
				}
			} catch (Exception e) {
				msg.what = -7;
			} finally {
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
		}
	};
	
	private Runnable hpleixiRunnable= new Runnable() {
		public void run() {
			Message msg = Message.obtain();
			try {
				int maxid = WebserviceImport.GetMaxID_LB(mSharedPreferences);
				String id = "0";
				lb_num = 0;
				String[] values = new String[str2.length];
				db = DataBaseImport.getInstance(WebHelper.this).getReadableDatabase();
				db.beginTransaction();
				dm_op.del_tableContent(DataBaseHelper.TB_hplbTree, db);
				while (!id.equals(String.valueOf(maxid))) {
					if (stopflag == true) {
						break;
					}
					ls = WebserviceImport.GetHPLB(200, id, str2,
							str3, mSharedPreferences);
					if (!ls.isEmpty()) {
						for (int i = 0; i < ls.size(); i++) {
							for (int j = 0; j < str2.length; j++) {
								values[j] = (String) ls.get(i).get(
										str2[j]);
							}
							// dm.del_ByID(DataBaseHelper.TB_hplbTree,
							// (String)
							// ls.get(i).get(DataBaseHelper.ID),
							// db);
							dm_op.insert_into_fromfile(
									DataBaseHelper.TB_hplbTree,
									str2, values, db);
							lb_num++;
						}
						id = (String) ls.get(ls.size() - 1).get(
								DataBaseHelper.ID);
					} else {
						break;
					}
				}
				msg.what=2;
			} catch (Exception e) {
				msg.what = -4;
			} finally {
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
		}
	};
	
	private Runnable dwxinxiRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			Message msg = Message.obtain();
			try {
			int maxid = WebserviceImport
					.GetMaxID_DW(mSharedPreferences);
			String id = "0";
			dw_num = 0;
			String[] values = new String[str4.length];
			db = DataBaseImport.getInstance(WebHelper.this).getReadableDatabase();
			db.beginTransaction();
			dm_op.del_tableContent(DataBaseHelper.TB_Company, db);
				while (!id.equals(String.valueOf(maxid))) {
					if (stopflag == true) {
						break;
					}
					ls = WebserviceImport.GetDW(200, id, str4,
							str5, mSharedPreferences);
					if (!ls.isEmpty()) {
						for (int i = 0; i < ls.size(); i++) {
							for (int j = 0; j < str4.length; j++) {
								values[j] = (String) ls.get(i).get(
										str4[j]);
							}
							// dm.del_ByID(DataBaseHelper.TB_Company,
							// (String)
							// ls.get(i).get(DataBaseHelper.ID),
							// db);
							dm_op.insert_into_fromfile(
									DataBaseHelper.TB_Company,
									str4, values, db);
							dw_num++;
						}
						id = (String) ls.get(ls.size() - 1).get(
								DataBaseHelper.ID);
					} else {
						break;
					}
				}
				msg.what=3;
			} catch (Exception e) {
				msg.what = -5;
			} finally {
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
		}
	};
	
	private Runnable canshuxinxiRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			Message msg = Message.obtain();
			try {
			conf_num = 0;
			String[] values = new String[str6.length];
			db = DataBaseImport.getInstance(WebHelper.this).getReadableDatabase();
			db.beginTransaction();
			dm_op.del_tableContent(DataBaseHelper.TB_Conf, db);
				ls = WebserviceImport.GetConf("�Զ����ֶ�", "", str6,
						str7, mSharedPreferences);
				if (!ls.isEmpty()) {
//					dm_op.Del_Conf(new String[] { "�Զ����ֶ�" }, db);
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str6.length; j++) {
							values[j] = (String) ls.get(i).get(
									str6[j]);
						}

						dm_op.insert_into_fromfile(
								DataBaseHelper.TB_Conf, str6,
								values, db);
						conf_num++;
					}
				}
				
				ls = WebserviceImport.GetConf("�����Զ����ֶ�", "", str6, str7, mSharedPreferences);
				if(!ls.isEmpty()){
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str6.length; j++) {
							values[j] = (String) ls.get(i).get(
									str6[j]);
						}

						dm_op.insert_into_fromfile(
								DataBaseHelper.TB_Conf, str6,
								values, db);
						conf_num++;
					}
				}
				
				
				ls = WebserviceImport.GetConf("������Ϣ", "", str6,
						str7, mSharedPreferences);
				if (!ls.isEmpty()) {
//					dm_op.Del_Conf(new String[] { "������Ϣ" }, db);
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str6.length; j++) {
							values[j] = (String) ls.get(i).get(
									str6[j]);
						}
						dm_op.insert_into_fromfile(
								DataBaseHelper.TB_Conf, str6,
								values, db);
						conf_num++;
					}
				}
				
				ls = WebserviceImport.GetConf("����ʶ��", "", str6, str7, mSharedPreferences);
				if(!ls.isEmpty()){
//					dm_op.Del_Conf(new String[] { "����ʶ��" }, db);
					for(int i=0;i<ls.size();i++){
						for(int j = 0;j<str6.length;j++){
							values[j] = ls.get(i).get(str6[j]).toString();
						}
						dm_op.insert_into_fromfile(DataBaseHelper.TB_Conf, str6, values, db);
						conf_num++;
					}
				}
				
				ls = WebserviceImport.GetIOType(str10, str11,
						mSharedPreferences);
				values = new String[str10.length];
				if (!ls.isEmpty()) {
//					dm_op.Del_Conf(new String[] { "�������" }, db);
//					dm_op.Del_Conf(new String[] { "��������" }, db);
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str10.length; j++) {
							if (str10[j].equals(DataBaseHelper.GID)) {
								if (ls.get(i).get(str10[j])
										.equals("1")) {
									values[j] = "�������";
								} else if (ls.get(i).get(str10[j])
										.equals("2")) {
									values[j] = "��������";
								}
							} else {
								values[j] = (String) ls.get(i).get(
										str10[j]);
							}
						}
						dm_op.insert_into_fromfile(
								DataBaseHelper.TB_Conf, str10,
								values, db);
						conf_num++;
					}
				}
				ls = WebserviceImport.GetDep(str17, str16,mSharedPreferences);
				values = new String[str16.length];
				dm_op.del_tableContent(DataBaseHelper.TB_depTree, db);
				if (!ls.isEmpty()) {
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str16.length; j++) {
							values[j] = (String) ls.get(i).get(str17[j]);
						}
//						dm.del_Dep((String) ls.get(i).get("id"), db);
						dm_op.insert_into_fromfile(
								DataBaseHelper.TB_depTree, str17,values, db);
						conf_num++;
					}
				}
				msg.what = 4;
			} catch (Exception e) {
				msg.what = -6;
			} finally {
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				MyApplication.getInstance().setALLPoint();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
		}
	};
	
	private Runnable cangkuxinxiRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			Message msg = Message.obtain();
			try {
				db = DataBaseImport.getInstance(WebHelper.this).getReadableDatabase();
				db.beginTransaction();
				dm_op.del_tableContent(DataBaseHelper.TB_CK, db);
				ck_num = 0;
				ls = WebserviceImport.GetCK(str14, str15,
						mSharedPreferences);
				if (ls != null && ls.size() != 0) {
					String[] values = new String[str14.length];
					for (int i = 0; i < ls.size(); i++) {
						String id = (String) ls.get(i).get(
								DataBaseHelper.ID);
						if (id != null && !id.equals("")) {
							for (int j = 0; j < str14.length; j++) {
								values[j] = (String) ls.get(i).get(
										str14[j]);
							}
//							 dm_op.Del_CK(id);
							dm_op.insert_into1(DataBaseHelper.TB_CK,
									str14, values, db);
							ck_num++;
						}
					}
				}
				msg.what=5;
			} catch (Exception e) {
				// TODO: handle exception
				msg.what=-8;
			} finally{
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
		}
	};
	

	private Runnable tongjiRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			Looper.prepare();
			StringBuilder strBuilder = new StringBuilder();
			if (ck_hp.isChecked()) {
				strBuilder.append("��Ʒ��Ϣ���£�" + String.valueOf(hp_num)
						+ "��\n");
			}
			if(ck_ckkc.isChecked()){
				strBuilder.append("�ֿ�����Ϣ���£�" + String.valueOf(ckkc_num)
						+ "��\n");
			}
			if (ck_lb.isChecked()) {
				strBuilder.append("��Ʒ���͸��£�" + String.valueOf(lb_num)
						+ "��\n");
			}
			if (ck_dw.isChecked()) {
				strBuilder.append("������λ��Ϣ���£�" + String.valueOf(dw_num)
						+ "��\n");
			}
			if (ck_type.isChecked()) {
				strBuilder.append("�������£�" + String.valueOf(conf_num)
						+ "��\n");
			}
			if (ck_ck.isChecked()) {
				strBuilder.append("�ֿ���Ϣ���£�" + String.valueOf(ck_num) + "��");
			}
			
			mpDialog = new CommonDialog(WebHelper.this, R.layout.prompt_dialog_layout2, R.style.yuanjiao_dialog);
			mpDialog.setDialogContentListener(new DialogContentListener() {
				
				@Override
				public void contentExecute(View parent, Dialog dialog, Object[] objs) {
					// TODO �Զ����ɵķ������
					TextView contentTextView = mpDialog.getView(R.id.content_txtview);
					TextView cancelTextView = mpDialog.getView(R.id.cancel);
					contentTextView.setText(objs[0].toString());
					cancelTextView.setText("ȷ��");
					cancelTextView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO �Զ����ɵķ������
							hp_num = 0;
							lb_num = 0;
							dw_num = 0;
							conf_num = 0;
							ck_num = 0;
							ckkc_num = 0;
							mpDialog.dismiss();
						}
					});
				}
			}, new Object[]{strBuilder});
			mpDialog.show();
			
			
//			AlertDialog.Builder builder = new AlertDialog.Builder(
//					WebHelper.this);
//			builder.setMessage(strBuilder.toString());
//			builder.setCancelable(false);
//			builder.setPositiveButton("ȷ��",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog,
//								int which) {
//							// TODO Auto-generated method stub
//							hp_num = 0;
//							lb_num = 0;
//							dw_num = 0;
//							conf_num = 0;
//							ck_num = 0;
//							ckkc_num = 0;
//							dialog.dismiss();
//
//						}
//
//					});
//			builder.create().show();
			Message msg = Message.obtain();
			mSemaphore.release();
			msg.what=6;
			myHandler.sendMessage(msg);
			Looper.loop();
		}
	};
		
	private int GtProgress(int leave, int total) {
		if (leave == total) {
			return 100;
		} else {
			return (total - leave) * 100 / total;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO �Զ����ɵķ������
		
		switch(buttonView.getId()){
		case R.id.ck_hp:
			if(isChecked){
				ck_hp.setBackground(getResources().getDrawable(R.drawable.check_selected));
			}else{
				ck_hp.setBackground(getResources().getDrawable(R.drawable.check_default));
			}
			break;
		case R.id.ck_ckkc:
			if(isChecked){
				ck_ckkc.setBackground(getResources().getDrawable(R.drawable.check_selected));
			}else{
				ck_ckkc.setBackground(getResources().getDrawable(R.drawable.check_default));
			}
			break;
		case R.id.ck_dw:
			if(isChecked){
				ck_dw.setBackground(getResources().getDrawable(R.drawable.check_selected));
			}else{
				ck_dw.setBackground(getResources().getDrawable(R.drawable.check_default));
			}
			break;
		case R.id.ck_type:
			if(isChecked){
				ck_type.setBackground(getResources().getDrawable(R.drawable.check_selected));
			}else{
				ck_type.setBackground(getResources().getDrawable(R.drawable.check_default));
			}
			break;
		case R.id.ck_ck:
			if(isChecked){
				ck_ck.setBackground(getResources().getDrawable(R.drawable.check_selected));
			}else{
				ck_ck.setBackground(getResources().getDrawable(R.drawable.check_default));
			}
			break;
		case R.id.ck_lb:
			if(isChecked){
				ck_lb.setBackground(getResources().getDrawable(R.drawable.check_selected));
			}else{
				ck_lb.setBackground(getResources().getDrawable(R.drawable.check_default));
			}
			break;
		case R.id.radBtn:
			if(isChecked){
				radBtn.setBackground(getResources().getDrawable(R.drawable.check_selected));
				ck_hp.setChecked(true);
				ck_ckkc.setChecked(true);
				ck_dw.setChecked(true);
				ck_type.setChecked(true);
				ck_yg.setChecked(true);
				ck_ck.setChecked(true);
				ck_lb.setChecked(true);
			}else{
				radBtn.setBackground(getResources().getDrawable(R.drawable.check_default));
				ck_hp.setChecked(false);
				ck_ckkc.setChecked(false);
				ck_dw.setChecked(false);
				ck_type.setChecked(false);
				ck_yg.setChecked(false);
				ck_ck.setChecked(false);
				ck_lb.setChecked(false);
			}
			break;
		}
	}
}
