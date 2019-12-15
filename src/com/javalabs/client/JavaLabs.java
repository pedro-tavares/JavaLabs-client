package com.javalabs.client;

import java.io.UnsupportedEncodingException;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.dispatcher.DefaultFilterawareDispatcher;
import org.fusesource.restygwt.client.dispatcher.DispatcherFilter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.gwt.crypto.bouncycastle.util.encoders.Base64;
import com.javalabs.client.ui.CenterPanel;
import com.javalabs.client.ui.TestbedPanel;

public class JavaLabs implements EntryPoint {
	
	private Image javaImg = new Image("images/Java_1.png");
	private HorizontalPanel bottomPanel = new HorizontalPanel();
	
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
		
		VerticalPanel presentationPanel = new VerticalPanel();
		presentationPanel.setStyleName("presentationPanel");
		presentationPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		presentationPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
		Label presentation1Lbl = new Label("The Best Java Skills Assessment Platform");
		presentation1Lbl.setStyleName("presentation1Lbl");
		presentationPanel.add(presentation1Lbl);
		Label presentation2Lbl = new Label("1000+ carefully curated questions");
		presentation2Lbl.setStyleName("presentation2Lbl");
		presentationPanel.add(presentation2Lbl);
		RootPanel.get().add(presentationPanel, 0, 66);
		
		bottomPanel.setStyleName("bottomPanel");
		bottomPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		bottomPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Anchor indiegogoLnk = new Anchor(
			"JavaLabs is a product by CloudLabs. Support our Indiegogo Campaign", 
			"https://www.indiegogo.com/projects/java-knowledge-base/x/7465742#/", "_blank"
		);
		bottomPanel.add(indiegogoLnk);
		RootPanel.get().add(bottomPanel, 0, Window.getClientHeight() - 25);
		
		javaImg.setPixelSize(100, 100);
		resize();
		
		Window.addResizeHandler(new ResizeHandler() {
			
			  Timer resizeTimer = new Timer() {  
			    @Override
			    public void run() {
			      resize();
			    }
			  };

			  @Override
			  public void onResize(ResizeEvent event) {
			    resizeTimer.cancel();
			    resizeTimer.schedule(10);
			  }
			});
		
		createCenterPanel();
	}
	
	private void createCenterPanel() {
		CenterPanel centerPanel = new CenterPanel();
		RootPanel.get().add(centerPanel, 0, 150);
		
		TestbedPanel testbedPanel = new TestbedPanel();
		centerPanel.add(testbedPanel);
		
	}
	
	private void resize() {
		RootPanel.get().add(javaImg, Window.getClientWidth() - 100, Window.getClientHeight() - 125);
		RootPanel.get().add(bottomPanel, 0, Window.getClientHeight() - 25);
	}
}
