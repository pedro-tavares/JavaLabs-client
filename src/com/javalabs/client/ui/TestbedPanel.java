package com.javalabs.client.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javalabs.dto.User;

import com.javalabs.IKonstants;
import com.javalabs.client.service.ServiceFactory;

public class TestbedPanel extends VerticalPanel {
	
	private Button saveButton, dialogCloseButton, searchButton, loadAllButton;
	private TextBox nameField, searchField;
	private Label textToServerLabel;
	private HTML serverResponseLabel;
	private DialogBox dialogBox;

	public TestbedPanel() {
		nameField = new TextBox();
		saveButton = new Button("Save");

		searchField = new TextBox();
		searchButton = new Button("Search");
		loadAllButton = new Button("Load All");
		
		saveButton.addStyleName("sendButton");
		searchButton.addStyleName("sendButton");

		nameField.setFocus(true);
		nameField.selectAll();

		createDialogBox();

		saveButton.addClickHandler(event -> {
			textToServerLabel.setText(nameField.getText());
			callSaveService();
		});

		loadAllButton.addClickHandler(event -> callLoadService());
		
		this.setStyleName("vPanel");
		this.setSize("100", "100");
		this.add(nameField);
		this.add(saveButton);
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

	private void callLoadService() {
		ServiceFactory.USER_SERVICE.getAllUsers(new MethodCallback<List<User>>() {

			@Override
			public void onSuccess(Method method, List<User> response) {
				serverResponseLabel.removeStyleName("serverResponseLabelError");
				String names = response.stream()
						.map(e -> "<li>" + "Username:" + e.getUserName() + ", ID: " + e.getId() + "</li>")
						.collect(Collectors.joining(""));
				showDialogBox("REST endpoint call", "<ul>" + names + "<ul>");
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				serverResponseLabel.addStyleName("serverResponseLabelError");
				showDialogBox("REST endpoint call - Failure", IKonstants.SERVER_ERROR);
			}
		});
	}
	
	private void callSaveService() {
		String usernmae = nameField.getText();
		ServiceFactory.USER_SERVICE.saveUser(usernmae, new MethodCallback<User>() {

			@Override
			public void onSuccess(Method method, User response) {
				serverResponseLabel.removeStyleName("serverResponseLabelError");
				showDialogBox("REST endpoint call", "Username: " + response.getUserName() + " ID: " + response.getId());
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				serverResponseLabel.addStyleName("serverResponseLabelError");
				showDialogBox("REST endpoint call - Failure", IKonstants.SERVER_ERROR);
			}
		});
	}
	
}
