package ua.kas.main;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Controller {

	@FXML
	TextField address;

	public void host() {
		address.setDisable(false);
		Messenger.address = address.getText();
		Messenger.server = true;
		Messenger messenger = new Messenger();

		try {
			messenger.start(Main.primaryStage);
			Main.primaryStage.centerOnScreen();
		} catch (Exception ex) {
		}
	}

	public void play() {
		address.setDisable(true);
		Messenger.address = address.getText();
		Messenger.server = false;
		Messenger messenger = new Messenger();

		try {
			messenger.start(Main.primaryStage);
			Main.primaryStage.centerOnScreen();
		} catch (Exception ex) {
		}
	}
}
