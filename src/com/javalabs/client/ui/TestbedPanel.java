package com.javalabs.client.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javalabs.IConstants;
import com.javalabs.client.service.ServiceFactory;
import com.javalabs.dto.User;

public class TestbedPanel extends VerticalPanel {
	
	private Button saveButton, dialogCloseButton, searchButton, loadAllButton;
	private Label nameLbl, emailLbl, passwordLbl, reenterPasswordLbl, searchNameLbl, textToServerLabel;
	private TextBox nameField, emailField, searchField;
	private PasswordTextBox passwordField, reenterPasswordField;
	private HTML serverResponseLabel;
	private DialogBox dialogBox;

	public TestbedPanel() {
		this.setStyleName("centerPanels");
		this.setSpacing(10);
		this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		nameLbl = new Label("Enter Name:");
		nameField = new TextBox();

		emailLbl = new Label("Enter Email:");
		emailField = new TextBox();

		passwordLbl = new Label("Enter Password:");
		passwordField = new PasswordTextBox();

		reenterPasswordLbl = new Label("Renter Password:");
		reenterPasswordField = new PasswordTextBox();
		saveButton = new Button("Save");

		searchNameLbl = new Label("Enter Search Email:");
		searchField = new TextBox();
		searchButton = new Button("Search");

		loadAllButton = new Button("Load All");

		nameField.setFocus(true);
		nameField.selectAll();

		createDialogBox();

		saveButton.addClickHandler(event -> {
			textToServerLabel.setText(nameField.getText());
			callSaveService();
		});

		loadAllButton.addClickHandler(event -> callLoadService());
		
		//this.setSize("100", "100");
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
	}
	
	private void createDialogBox() {
		dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);

		dialogCloseButton = new Button("Close");
		dialogCloseButton.getElement().setId("closeButton");
		textToServerLabel = new Label();
		serverResponseLabel = new HTML();

		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");

		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);

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
		User user = new User();
		user.setName(nameField.getText());
		user.setEmail(emailField.getText());
		
		ServiceFactory.USER_SERVICE.saveUser(user, new MethodCallback<User>() {

			@Override
			public void onSuccess(Method method, User response) {
				serverResponseLabel.removeStyleName("serverResponseLabelError");
				showDialogBox("REST endpoint call", 
					"User name: " + response.getName() + 
					"User email: " + response.getEmail() + 
					" ID: " + response.getId());
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				serverResponseLabel.addStyleName("serverResponseLabelError");
				showDialogBox("REST endpoint call - Failure", IConstants.SERVER_ERROR);
			}
		});
	}
	
	private void callLoadService() {
		ServiceFactory.USER_SERVICE.getAllUsers(new MethodCallback<List<User>>() {

			@Override
			public void onSuccess(Method method, List<User> response) {
				serverResponseLabel.removeStyleName("serverResponseLabelError");
				String names = response.stream()
					.map(e -> "<li>" + 
						"User name:" + e.getName() +
						"User email:" + e.getEmail() +
						", ID: " + e.getId() + 
						"</li>")
					.collect(Collectors.joining(""));
				showDialogBox("REST endpoint call", "<ul>" + names + "<ul>");
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				serverResponseLabel.addStyleName("serverResponseLabelError");
				showDialogBox("REST endpoint call - Failure", IConstants.SERVER_ERROR);
			}
		});
	}
	
}
