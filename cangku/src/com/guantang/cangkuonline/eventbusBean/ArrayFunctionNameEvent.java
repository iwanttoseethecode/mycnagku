package com.guantang.cangkuonline.eventbusBean;
/**
 * ���ݹ�����������
 * */
import java.util.List;

public class ArrayFunctionNameEvent {
	private List<String> functionName_List;

	public ArrayFunctionNameEvent(List<String> List) {
		super();
		this.functionName_List = List;
	}

	public List<String> getFunctionName_List() {
		return functionName_List;
	}
	
}
