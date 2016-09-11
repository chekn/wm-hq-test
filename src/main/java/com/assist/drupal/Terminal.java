package com.assist.drupal;

/**
 * 
 * @author Pactera-NEN
 * @date 2016年3月10日-下午7:27:56
 */
public enum Terminal {
	
	TV("TV.ftl","即时"),
	CLIENT("ClientEnd.ftl","全文"),
	WEB("CBNWeb.ftl","一财网"),
	CBN724("CBN724.ftl","7*24频道"),
	NEWS("CBNNews.ftl","第一财讯"),
	EN("En.ftl","英文");
	
	private String defFileNameSuf;
	private String terminalName;
	private Terminal(String defFileNameSuf,String terminalName){
		this.defFileNameSuf=defFileNameSuf;
		this.terminalName=terminalName;
	}
	
	
	public String getDefFileNameSuf() {
		return defFileNameSuf;
	}
	public void setDefFileNameSuf(String defFileNameSuf) {
		this.defFileNameSuf = defFileNameSuf;
	}
	
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

}
