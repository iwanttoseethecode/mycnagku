package com.guantang.cangkuonline.mulu;


public class ElementBean {
	private String id;
	private String pid;//Ԫ����һ��ĸ�id
	private String name;
	private int lev;//��ǰ�����ڵڶ��ٲ�
	
	public int getLev() {
		return lev;
	}
	public void setLev(int lev) {
		this.lev = lev;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
