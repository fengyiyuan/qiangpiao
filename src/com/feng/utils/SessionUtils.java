package com.feng.utils;

import com.feng.domain.HttpTicketDO;


public class SessionUtils{

	private static final ThreadLocal<HttpTicketDO> threadLocal = new ThreadLocal<HttpTicketDO>();
	
	public static void setHttpTicket(HttpTicketDO httpTicketDO) {

		threadLocal.set(httpTicketDO);

	}
	
    public static HttpTicketDO getHttpTicket() {
			return threadLocal.get();
	}


}
