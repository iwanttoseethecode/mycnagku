package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.sortlistview.SortModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class DDdetailAdapter extends BaseAdapter implements SectionIndexer{
	private Context context;
	private int dd_dirct = 0;
	private String  ddid = "";
	private List<SortModel> mlist=new ArrayList<SortModel>();
	private LayoutInflater inflater;
	public DDdetailAdapter(Context context, String ddid, int dd_dirct) {
		super();
		this.context = context;
		this.dd_dirct = dd_dirct;
		this.ddid = ddid;
		inflater = LayoutInflater.from(context);
	}
	
	public void setData(List<SortModel> list){
		this.mlist = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO �Զ����ɵķ������
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO �Զ����ɵķ������
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO �Զ����ɵķ������
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO �Զ����ɵķ������
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.djdetailsitem_dd, null);
			holder.itemnameTxtView = (TextView) convertView.findViewById(R.id.itemname);
			holder.itemcodeTxtView = (TextView) convertView.findViewById(R.id.itemcode);
			holder.itemggTxtView = (TextView) convertView.findViewById(R.id.itemgg);
			holder.itemnumTxtView = (TextView) convertView.findViewById(R.id.itemnum);
			holder.itemdjTxtView = (TextView) convertView.findViewById(R.id.itemdj);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Map<String,Object> map = mlist.get(position).getMyMap();
		
		holder.itemnameTxtView.setText(map.get(DataBaseHelper.HPMC).toString());
		holder.itemcodeTxtView.setText(map.get(DataBaseHelper.HPBM).toString());
		holder.itemggTxtView.setText(map.get(DataBaseHelper.GGXH).toString());
		holder.itemnumTxtView.setText(map.get(DataBaseHelper.SL).toString());
		holder.itemdjTxtView.setText(map.get(DataBaseHelper.DJ).toString());
		
		return convertView;
	}
	class ViewHolder {
		TextView itemnameTxtView,itemcodeTxtView,itemggTxtView,itemnumTxtView,itemdjTxtView;
	}
	
	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	@Override
	public int getPositionForSection(int arg0) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = ((SortModel) mlist.get(i)).getSortLetters();
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
		return ((SortModel) mlist.get(position)).getSortLetters().charAt(0);
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
