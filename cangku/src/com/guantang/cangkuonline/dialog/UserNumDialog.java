package com.guantang.cangkuonline.dialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.guantang.cangkuonline.R;

public class UserNumDialog {
	private Context mcontext;
	private ListView list;
	private EditText ed;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private String[] str={"1","3","6","10","20"};
	private  List<Map<String, Object>> ls= new ArrayList<Map<String, Object>>();
	private onItemClick mClick;
	private onButtonClick mButtonClick;
	public UserNumDialog(Context context) {
		this.mcontext = context;
	}
	public void showDialog(){
		LayoutInflater inflater = LayoutInflater.from(mcontext);
		View dialogView = inflater.inflate(R.layout.dialog_usernum, null);
		list=(ListView) dialogView.findViewById(R.id.list);
		ed=(EditText) dialogView.findViewById(R.id.ed);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				dismiss();
				if(null!=mClick){
					mClick.onClick(str[position]);
				}
			}
			
		});
		ls.clear();
		for(int i=0;i<str.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", str[i]+"��");
			ls.add(map);
		}
		setAdapter(ls);
		builder = new AlertDialog.Builder(mcontext);
		builder.setTitle("��ѡ���û���");
		builder.setView(dialogView);
		builder.setNegativeButton("�ر�", new DialogInterface.OnClickListener (){
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Field field;
				try {
					field = dialog.getClass()
							.getSuperclass().getDeclaredField(
							"mShowing" );
					field.setAccessible( true );
					// ��mShowing������Ϊfalse����ʾ�Ի����ѹر� 
					field.set(dialog, true );
					
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.dismiss();
			}
			
		});
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener (){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Field field;
				try {
					field = dialog.getClass()
							.getSuperclass().getDeclaredField(
							"mShowing" );
					field.setAccessible( true );
					// ��mShowing������Ϊfalse����ʾ�Ի����ѹر� 
					field.set(dialog, false );
					
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
				String str=ed.getText().toString();
				if(null==str||str.equals("")){
					Toast.makeText(mcontext, "��ѡ�����������", Toast.LENGTH_SHORT).show();
				}else{
					if(Integer.parseInt(str)>20){
						try {
							field = dialog.getClass()
									.getSuperclass().getDeclaredField(
									"mShowing" );
							field.setAccessible( true );
							// ��mShowing������Ϊfalse����ʾ�Ի����ѹر� 
							field.set(dialog, true );
							
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dialog.dismiss();
						if(null!=mButtonClick){
							mButtonClick.onClick(str);
						}
					}else{
						Toast.makeText(mcontext, "���������20���ϵ�����", Toast.LENGTH_SHORT).show();
					}
				}
				
			}
			
		});
		dialog=builder.show();
	}
	public void dismiss(){
		dialog.dismiss();
	}
	private void setAdapter(List<Map<String, Object>> lss){
		SimpleAdapter listItemAdapter = new SimpleAdapter(mcontext, lss,
					android.R.layout.simple_list_item_1,
					new String[] { "name" }, new int[] { android.R.id.text1 });
		 list.setAdapter(listItemAdapter);
	}
	public interface onItemClick{
		public void onClick(String num);
	}
	public void setOnItemClick(onItemClick mClick){
		this.mClick=mClick;
	}
	public interface onButtonClick{
		public void onClick(String num);
	}
	public void setButtonClick(onButtonClick mClick){
		this.mButtonClick=mClick;
	}
}
