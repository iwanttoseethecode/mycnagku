package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.channel.ChannelItem;
import com.guantang.cangkuonline.channel.DragAdapter;
import com.guantang.cangkuonline.channel.DragGrid;
import com.guantang.cangkuonline.channel.OtherAdapter;
import com.guantang.cangkuonline.channel.OtherGridView;
import com.guantang.cangkuonline.eventbusBean.ArrayFunctionNameEvent;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;

import de.greenrobot.event.EventBus;



public class ChannelActivity extends Activity implements OnItemClickListener {
	/** �û���Ŀ��GRIDVIEW */
	private DragGrid userGridView;
	/** ������Ŀ��GRIDVIEW */
	private OtherGridView otherGridView;
	/** �û���Ŀ��Ӧ���������������϶� */
	DragAdapter userAdapter;
	/** ������Ŀ��Ӧ�������� */
	OtherAdapter otherAdapter;
	/** ������Ŀ�б� */
	ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
	/** �û���Ŀ�б� */
	ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
	/** �Ƿ����ƶ�����������Ƕ���������Ž��е����ݸ��棬�����������Ϊ�˱������̫Ƶ����ɵ����ݴ��ҡ� */
	boolean isMove = false;
	private static int IS_SELECT=1;
	private static int ISNOT_SELECT=0;
	private SharedPreferences mSharedPreferences;
	private ImageButton backImgbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe_activity);
        mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
        initView();
		initData();
    }

    /** ��ʼ������ */
	private void initView() {	
		
		backImgbtn = (ImageButton) findViewById(R.id.back);
		userGridView = (DragGrid) findViewById(R.id.userGridView);
		otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
		backImgbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				saveChannel();
				finish();
			}
		});
	}
	
    /** ��ʼ������ */
	private void initData(){
		getUserOtherItem();
		userAdapter = new DragAdapter(this, userChannelList);
		userGridView.setAdapter(userAdapter);
		otherAdapter = new OtherAdapter(this, otherChannelList);
		otherGridView.setAdapter(this.otherAdapter);
		// ����GRIDVIEW��ITEM�ĵ������
		otherGridView.setOnItemClickListener(this);
		userGridView.setOnItemClickListener(this);
	}
	
	/** ��ȡ�û��Ĺ���ѡ�����������ѡ��*/
	public void getUserOtherItem(){
		ArrayList<String> function_name = new ArrayList<String>();
		function_name.add("������Ʒ");
		function_name.add("�������");
		function_name.add("��������");
		function_name.add("�����̵�");
		function_name.add("��Ʒ����");
		function_name.add("���ص���");
		function_name.add("������ϸ");
		function_name.add("����ͬ��");
		function_name.add("����˵���");
		function_name.add("����");
		for(int i=0;i<8;i++){
			String userfunctionItem = mSharedPreferences.getString("gridview_item"+i, "");
			if(!userfunctionItem.equals("")){
				function_name.remove(userfunctionItem);
				ChannelItem channelitem = new ChannelItem(i, userfunctionItem, i, IS_SELECT);
				userChannelList.add(channelitem);
			}
		}
		for(int i=0;i<10-userChannelList.size();i++){
			ChannelItem channelitem = new ChannelItem(i, function_name.get(i), i, ISNOT_SELECT);
			otherChannelList.add(channelitem);
		}
		
	}
	
	/** GRIDVIEW��Ӧ��ITEM��������ӿ� */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		// ��������ʱ��֮ǰ������û��������ô���õ���¼���Ч
		if (isMove) {
			return;
		}
		switch (parent.getId()) {
		case R.id.userGridView:
			final ImageView moveImageView1 = getView(view);
			if (moveImageView1 != null) {
				TextView newTextView = (TextView) view
						.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem channel = ((DragAdapter) parent.getAdapter())
						.getItem(position);// ��ȡ�����Ƶ������
				otherAdapter.setVisible(false);
				// ��ӵ����һ��
				otherAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// ��ȡ�յ������
							otherGridView.getChildAt(
									otherGridView.getLastVisiblePosition())
									.getLocationInWindow(endLocation);
							MoveAnim(moveImageView1, startLocation,
									endLocation, channel, userGridView);
							userAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		case R.id.otherGridView:
			final ImageView moveImageView2 = getView(view);
			if (moveImageView2 != null) {
				TextView newTextView = (TextView) view
						.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem channel = ((OtherAdapter) parent.getAdapter())
						.getItem(position);
				if(userChannelList.size()<8){
					// ��ӵ����һ��
					userAdapter.setVisible(false);
					userAdapter.addItem(channel);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							try {
								int[] endLocation = new int[2];
								// ��ȡ�յ������
								userGridView.getChildAt(
										userGridView.getLastVisiblePosition())
										.getLocationInWindow(endLocation);
								MoveAnim(moveImageView2, startLocation,
										endLocation, channel, otherGridView);
								otherAdapter.setRemove(position);
							} catch (Exception localException) {
							}
						}
					}, 50L);
				}else{
					Toast.makeText(this, "���ù���ѡ��������8��", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * ���ITEM�ƶ�����
	 * 
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,
			int[] endLocation, final ChannelItem moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		// ��ȡ���ݹ�����VIEW������
		moveView.getLocationInWindow(initLocation);
		// �õ�Ҫ�ƶ���VIEW,�������Ӧ��������
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView,
				initLocation);
		// �����ƶ�����
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);// ����ʱ��
		// ��������
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// ����Ч��ִ����Ϻ�View���󲻱�������ֹ��λ��
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof �����ж�2��ʵ���ǲ���һ�����жϵ������DragGrid����OtherGridView
				if (clickGridView instanceof DragGrid) {
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				} else {
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}

	/**
	 * ��ȡ�ƶ���VIEW�������ӦViewGroup��������
	 * 
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation){
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	/**
	 * �����ƶ���ITEM��Ӧ��ViewGroup��������
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	/**
	 * ��ȡ�����Item�Ķ�ӦView��
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}

	/** �˳�ʱ�򱣴�ѡ������ݿ������ */
	private void saveChannel() {
//		ChannelManage.getManage(AppApplication.getApp().getSQLHelper())
//				.deleteAllChannel();
//		ChannelManage.getManage(AppApplication.getApp().getSQLHelper())
//				.saveUserChannel(userAdapter.getChannnelLst());
//		ChannelManage.getManage(AppApplication.getApp().getSQLHelper())
//				.saveOtherChannel(otherAdapter.getChannnelLst());
		Iterator<ChannelItem> it=userChannelList.iterator();
		ArrayList<String> userfunctionname = new ArrayList<String>();
		while(it.hasNext()){
			userfunctionname.add(it.next().getName());
		}
		EventBus.getDefault().post(new ArrayFunctionNameEvent(userfunctionname));
	}
	
	@Override
	public void onBackPressed() {
		saveChannel();
		super.onBackPressed();
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO �Զ����ɵķ������
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
    		saveChannel();
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    }
}
