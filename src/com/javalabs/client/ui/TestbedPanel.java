package com.javalabs.client.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javalabs.IUIConstants;
import com.javalabs.client.service.ServiceFactory;
import com.javalabs.dto.User;

public class TestbedPanel extends VerticalPanel {
	
	private Button saveButton, dialogCloseButton, searchButton, loadAllButton;
	private Label nameLbl, emailLbl, passwordLbl, reenterPasswordLbl, searchNameLbl, textToServerLbl, errorLbl;
	private TextBox nameField, emailField, searchField;
	private PasswordTextBox passwordField, reenterPasswordField;
	private HTML serverResponseLabel;
	private DialogBox dialogBox;

	public TestbedPanel() {
		this.setStyleName("centerPanels");
		this.setSpacing(5);
		this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		nameLbl = new Label("Enter Name:");
		nameField = new TextBox();

		emailLbl = new Label("Enter Email:");
		emailField = new TextBox();

		passwordLbl = new Label("Enter Password:");
		passwordField = new PasswordTextBox();
		passwordField.addFocusHandler(
			new FocusHandler() {
				@Override
			    public void onFocus(FocusEvent event) {
					errorLbl.setText("");
			    }
			});

		reenterPasswordLbl = new Label("Renter Password:");
		reenterPasswordField = new PasswordTextBox();
		reenterPasswordField.addFocusHandler(
			new FocusHandler() {
				@Override
				public void onFocus(FocusEvent event) {
					errorLbl.setText("");
				}
			});
		
		saveButton = new Button("Save");

		searchNameLbl = new Label("Enter Search Email:");
		searchField = new TextBox();
		searchButton = new Button("Search");

		loadAllButton = new Button("Load All");

		nameField.setFocus(true);
		nameField.selectAll();
		
		errorLbl = new Label();
		errorLbl.setStyleName("errorLbl");

		createDialogBox();

		saveButton.addClickHandler(event -> {
			if (nameField.getText().equals("")) {
				errorLbl.setText("Name cannot be empty!");
			}
			else if (emailField.getText().equals("")) {
				errorLbl.setText("Email cannot be empty!");
			}
			else if (passwordField.getText().equals("")) {
				errorLbl.setText("Password cannot be empty!");
			}
			else if (!passwordField.getText().equals(reenterPasswordField.getText())) {
				errorLbl.setText("Passwords must match!");
			} else {
				textToServerLbl.setText(nameField.getText());
				callSaveService();
			}
		});

		loadAllButton.addClickHandler(event -> callLoadService());
		
		this.add(nameLbl);
		this.add(nameField);
		this.add(emailLbl);
		this.add(emailField);
		this.add(passwordLbl);
		this.add(passwordField);
		this.add(reenterPasswordLbl);
		this.add(reenterPasswordField);		
		this.add(saveButton);
		this.add(searchNameLbl);
		this.add(searchField);
		this.add(searchButton);
		this.add(loadAllButton);
		this.add(errorLbl);
	}
	
	private void createDialogBox() {
		dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);

		dialogCloseButton = new Button("Close");
		dialogCloseButton.getElement().setId("closeButton");
		textToServerLbl = new Label();
		serverResponseLabel = new HTML();

		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");

		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLbl);

		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);

		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(dialogCloseButton);
		dialogBox.setWidget(dialogVPanel);

		dialogCloseButton.addClickHandler(event -> {
			dialogBox.hide();
			saveButton.setEnabled(true);
			saveButton.setFocus(true);
		});
	}
	
	private void showDialogBox(String title, String htmlText) {
		dialogBox.setText(title);
		serverResponseLabel.setHTML(htmlText);
		dialogBox.center();
		dialogCloseButton.setFocus(true);
	}

	private void callSaveService() {
		saveButton.getElement().getStyle().setCursor(Cursor.WAIT);
		RootPanel.getBodyElement().getStyle().setCursor(Cursor.WAIT);
		
		User user = new User();
		user.setName(nameField.getText());
		user.setEmail(emailField.getText());
		user.setPassword(passwordField.getText());
		
		ServiceFactory.USER_SERVICE.saveUser(user, new MethodCallback<User>() {

			@Override
			public void onSuccess(Method method, User response) {
				saveButton.getElement().getStyle().setCursor(Cursor.DEFAULT);
				RootPanel.getBodyElement().getStyle().setCursor(Cursor.DEFAULT);
				
				serverResponseLabel.removeStyleName("errorLbl");
				showDialogBox("REST endpoint call", 
					"User name: " + response.getName() + 
					", User email: " + response.getEmail() + 
					", ID: " + response.getId());
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				saveButton.getElement().getStyle().setCursor(Cursor.DEFAULT);
				RootPanel.getBodyElement().getStyle().setCursor(Cursor.DEFAULT);
				
				serverResponseLabel.addStyleName("errorLbl");
				showDialogBox("REST endpoint call - Failure", IUIConstants.SERVER_ERROR);
			}
		});
	}
	
	private void callLoadService() {
		loadAllButton.getElement().getStyle().setCursor(Cursor.WAIT);
		RootPanel.getBodyElement().getStyle().setCursor(Cursor.WAIT);
		
		ServiceFactory.USER_SERVICE.getAllUsers(new MethodCallback<List<User>>() {

			@Override
			public void onSuccess(Method method, List<User> response) {
				loadAllButton.getElement().getStyle().setCursor(Cursor.DEFAULT);
				RootPanel.getBodyElement().getStyle().setCursor(Cursor.DEFAULT);
				
				serverResponseLabel.removeStyleName("errorLbl");
				String names = response.stream()
					.map(e -> "<li>" + 
						"User name:" + e.getName() +
						", User email:" + e.getEmail() +
						", ID: " + e.getId() + 
						"</li>")
					.collect(Collectors.joining(""));
				showDialogBox("REST endpoint call", "<ul>" + names + "<ul>");
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				loadAllButton.getElement().getStyle().setCursor(Cursor.DEFAULT);
				RootPanel.getBodyElement().getStyle().setCursor(Cursor.DEFAULT);
				
				serverResponseLabel.addStyleName("errorLbl");
				showDialogBox("REST endpoint call - Failure", IUIConstants.SERVER_ERROR);
			}
		});
	}
	
}
