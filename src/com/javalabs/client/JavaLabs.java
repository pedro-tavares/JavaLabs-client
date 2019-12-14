package com.javalabs.client;

import java.io.UnsupportedEncodingException;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.dispatcher.DefaultFilterawareDispatcher;
import org.fusesource.restygwt.client.dispatcher.DispatcherFilter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.crypto.bouncycastle.util.encoders.Base64;
import com.javalabs.client.service.UserService;
import com.javalabs.client.ui.TestbedPanel;

public class JavaLabs implements EntryPoint {
	
	public static final UserService SERVICE = GWT.create(UserService.class);

	@Override
	public void onModuleLoad() {
		addAuthHeaders();
		createUI();
	}

	private void addAuthHeaders() {
		Defaults.setDispatcher(new DefaultFilterawareDispatcher(new DispatcherFilter() {

			@Override
			public boolean filter(Method method, RequestBuilder builder) {
				try {
					String basicAuthHeaderValue = createBasicAuthHeader("user", "user");
					builder.setHeader("Authorization", basicAuthHeaderValue);
				} catch (UnsupportedEncodingException e) {
					return false;
				}
				return true;
			}

			private String createBasicAuthHeader(String userName, String password) throws UnsupportedEncodingException {
				String credentials = userName + ":" + password;
				String encodedCredentials = new String(Base64.encode(credentials.getBytes()), "UTF-8");
				return "Basic " + encodedCredentials;
			}
		}));

	}
	
	private void createUI() {
				
		Image centerImg = new Image("images/background.jpg");
		centerImg.setStyleName("centerImg");
		RootPanel.get().add(centerImg, 0, 0);
		
		HorizontalPanel topPanel = new HorizontalPanel();
		topPanel.setStyleName("topPanel");
		Image logoImg = new Image("images/JavaLabs_Logo.jpg");
		logoImg.setSize("200", "40");
		topPanel.add(logoImg);
		RootPanel.get().add(topPanel, 0, 0);
		
		TestbedPanel testbedPanel = new TestbedPanel();
		RootPanel.get().add(testbedPanel, 100, 100);
		
	}
}
