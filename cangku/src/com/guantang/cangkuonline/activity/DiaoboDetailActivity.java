package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.adapter.DDdetailAdapter;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.eventbusBean.ObjectSeven;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.sortlistview.CharacterParser;
import com.guantang.cangkuonline.sortlistview.ClearEditText;
import com.guantang.cangkuonline.sortlistview.PinyinComparator;
import com.guantang.cangkuonline.sortlistview.SideBar;
import com.guantang.cangkuonline.sortlistview.SortModel;
import com.guantang.cangkuonline.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenu;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuCreator;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuItem;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import de.greenrobot.event.EventBus;

public class DiaoboDetailActivity extends Activity
		implements OnItemClickListener, OnMenuItemClickListener, OnClickListener {
	private SharedPreferences mSharedPreferences;
	private ImageButton backImgBtn, delImgBtn;
	private SwipeMenuListView mListView;
	private TextView dialog, detectiontxtView;
	private SideBar sideBar;

	// ������ϸ���ֶ�
	private String[] str1 = {DataBaseHelper.HPID,DataBaseHelper.SL,DataBaseHelper.DJ,DataBaseHelper.ZJ,DataBaseHelper.MID,DataBaseHelper.Note};
	private String[] str2 = {DataBaseHelper.ID,DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.HPTM, DataBaseHelper.GGXH};
	
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private DataBaseMethod dm = new DataBaseMethod(this);
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private int where = 0, dirc = 0;
	private String ddh = "", djid = "";

	private MyAdapter myAdapter;
	private ClearEditText mClearEditText;
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList = new ArrayList<SortModel>();

	/**
	 * ����ƴ��������ListView�����������
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_djdetail);
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);

		Intent intent = getIntent();
		djid = intent.getStringExtra("djid");

		initView();
		init();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		list = dm_op.Gt_Transd(djid, str1);
		ListIterator<Map<String,Object>> it = list.listIterator();
		while(it.hasNext()){
			Map<String,Object> map = it.next();
			String hpidString = map.get(DataBaseHelper.HPID).toString();
			List<Map<String, Object>> hpList = dm.Gethp(str2, hpidString);
			map.put(DataBaseHelper.HPMC,
					hpList.get(0).get(DataBaseHelper.HPMC).toString());
			map.put(DataBaseHelper.HPBM,
					hpList.get(0).get(DataBaseHelper.HPBM).toString());
			map.put(DataBaseHelper.GGXH,
					hpList.get(0).get(DataBaseHelper.GGXH).toString());
			map.put(DataBaseHelper.ID,
					hpList.get(0).get(DataBaseHelper.ID).toString());
			it.set(map);
		}
		myAdapter = new MyAdapter(this, SourceDateList, R.layout.djdetailsitem_dd);
		mListView.setAdapter(myAdapter);
		
		if (!list.isEmpty()) {
			SourceDateList = filledData();
			// ����a-z��������
			Collections.sort(SourceDateList, pinyinComparator);
			myAdapter.setData(SourceDateList);
			mListView.setSelection(where);
		}
	}

	public void initView() {
		backImgBtn = (ImageButton) findViewById(R.id.back);
		delImgBtn = (ImageButton) findViewById(R.id.del);
		mListView = (SwipeMenuListView) findViewById(R.id.hplist2);
		detectiontxtView = (TextView) findViewById(R.id.detectiontxtView);
		detectiontxtView.setVisibility(View.GONE);

		// ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		sideBar.setTextView(dialog);

		backImgBtn.setOnClickListener(this);

		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		mListView.setMenuCreator(creator);
		mListView.setOnMenuItemClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	public void init() {
		// �����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = myAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mListView.setSelection(position);
				}

			}
		});
		// �������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				// ������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO �Զ����ɵķ������
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					where = mListView.getFirstVisiblePosition();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO �Զ����ɵķ������

			}
		});
	}

	/**
	 * ΪListView�������,��Ϊÿ��Ԫ��������ĸ
	 * 
	 * @param date
	 * @return
	 */
	public List<SortModel> filledData() {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for (int i = 0; i < list.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setMyMap(list.get(i));
			// ����ת����ƴ��
			String pinyin = characterParser.getSelling(list.get(i).get(DataBaseHelper.HPMC).toString());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}

		return mSortList;
	}

	/**
	 * ����������е�ֵ���������ݲ�����ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		SourceDateList = filledData();
		if (TextUtils.isEmpty(filterStr)) {

		} else {
			for (SortModel sortModel : SourceDateList) {
				String name = (String) sortModel.getMyMap().get(DataBaseHelper.HPMC);
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
			SourceDateList.clear();
			SourceDateList = filterDateList;
		}
		// ����a-z��������
		Collections.sort(SourceDateList, pinyinComparator);
		myAdapter.setData(SourceDateList);
		mListView.setSelection(where);
	}

	public void setDelList(Map<String, Object> map) {
		dm_op.Del_transd(djid, map.get(DataBaseHelper.HPID).toString());
		list.remove(map);

	}

	@Override
	public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		// TODO �Զ����ɵķ������
		SortModel model = SourceDateList.get(position);
		switch (index) {
		case 0:
			setDelList(model.getMyMap());
			SourceDateList.remove(position);
			myAdapter.setData(SourceDateList);
			mListView.setSelection(where);
			EventBus.getDefault().post(new ObjectSeven(model.getMyMap()));
			break;

		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO �Զ����ɵķ������
		SortModel model = (SortModel) arg0.getAdapter().getItem(arg2);
		Map<String, Object> map = model.getMyMap();
		 Intent intent = new Intent(this,DiaoboHpListDetailsWriteActivity.class);
		 intent.putExtra("hpid", map.get(DataBaseHelper.HPID).toString());
		 intent.putExtra("djid", djid);
		 startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
	
	class MyAdapter extends CommonAdapter<SortModel> implements SectionIndexer{

		public MyAdapter(Context mContext, List<SortModel> mList, int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(com.guantang.cangkuonline.commonadapter.ViewHolder holder, SortModel item) {
			// TODO Auto-generated method stub
			TextView itemnameTxtView = (TextView) holder.getView(R.id.itemname);
			TextView itemcodeTxtView = (TextView) holder.getView(R.id.itemcode);
			TextView itemggTxtView = (TextView) holder.getView(R.id.itemgg);
			TextView itemnumTxtView = (TextView) holder.getView(R.id.itemnum);
			TextView itemdjTxtView = (TextView) holder.getView(R.id.itemdj);
			
			Map<String,Object> map = item.getMyMap();
			itemnameTxtView.setText(map.get(DataBaseHelper.HPMC).toString());
			itemcodeTxtView.setText(map.get(DataBaseHelper.HPBM).toString());
			itemggTxtView.setText(map.get(DataBaseHelper.GGXH).toString());
			itemnumTxtView.setText(map.get(DataBaseHelper.SL).toString());
			itemdjTxtView.setText(map.get(DataBaseHelper.DJ).toString());
		}
		/**
		 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
		 */
		@Override
		public int getPositionForSection(int arg0) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = ((SortModel) mList.get(i)).getSortLetters();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == arg0) {
					return i;
				}
			}
			
			return -1;
		}

		/**
		 * ����ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
		 */
		@Override
		public int getSectionForPosition(int position) {
			// TODO Auto-generated method stub
			return ((SortModel) mList.get(position)).getSortLetters().charAt(0);
		}

		/**
		 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#���档
		 * 
		 * @param str
		 * @return
		 */
		private String getAlpha(String str) {
			String  sortStr = str.trim().substring(0, 1).toUpperCase();
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if (sortStr.matches("[A-Z]")) {
				return sortStr;
			} else {
				return "#";
			}
		}

		@Override
		public Object[] getSections() {
			// TODO �Զ����ɵķ������
			return null;
		}
	}
	
}
