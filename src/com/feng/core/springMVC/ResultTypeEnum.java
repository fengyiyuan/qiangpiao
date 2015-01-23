package com.feng.core.springMVC;

public enum ResultTypeEnum {
	
	SUCCESS("0"),NEED_LOGIN("1"),ERRORS("2");
	public String value;
	private ResultTypeEnum(String value){
		this.value=value;
		
	}

}
