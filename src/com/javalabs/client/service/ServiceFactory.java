package com.javalabs.client.service;

import com.google.gwt.core.client.GWT;

public class ServiceFactory {

	public static final UserService USER_SERVICE = GWT.create(UserService.class);

}
