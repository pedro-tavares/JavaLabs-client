package com.javalabs.client.service;

import com.google.gwt.core.client.GWT;

public class ServiceFactory {

	public static final IUserService USER_SERVICE = GWT.create(IUserService.class);

}
