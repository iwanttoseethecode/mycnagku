package com.guantang.cangkuonline.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;

public class PiCiFragment extends Fragment{
	@Override
	public void onAttach(Activity activity) {
		// TODO �Զ����ɵķ������
		super.onAttach(activity);
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
		View v = inflater.inflate(R.layout.picifragment, null);
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onActivityCreated(savedInstanceState);
	}
}
