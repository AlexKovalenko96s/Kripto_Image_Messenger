package ua.kas.main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.imageio.ImageIO;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class MessengerUser {

	@FXML
	TextField tf_enterText;
	@FXML
	TextField tf_enterKey;

	@FXML
	ImageView iv_encryption;

	private String pathEncryptionImage;

	public void selectEcryptionImage() throws IOException {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showOpenDialog(null);

		try {
			pathEncryptionImage = file.getAbsolutePath();

			File file1 = new File(pathEncryptionImage);
			Image image = new Image(file1.toURI().toString());
			iv_encryption.setImage(image);
		} catch (Exception e) {
		}
	}

	public void go() throws IOException {
		String text = tf_enterText.getText();
		String binary = new BigInteger(text.getBytes("UTF-8")).toString(2)
				+ new BigInteger("end".getBytes("UTF-8")).toString(2);

		BufferedImage image = ImageIO.read(new File(pathEncryptionImage));
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		int pixel, red, green, blue, newColor;
		String temp = "";

		try {
			for (int width = 0; width < image.getWidth(); width++) {
				for (int height = 0; height < image.getHeight(); height++) {
					pixel = image.getRGB(width, height);

					red = (pixel >> 16) & 0xff;
					green = (pixel >> 8) & 0xff;
					blue = (pixel >> 0) & 0xff;

					if (binary.length() > 0) {
						temp = new BigInteger(Integer.toString(green).getBytes("UTF-8")).toString(2);

						temp = temp.substring(0, temp.length() - 1) + binary.substring(0, 1);
						binary = binary.substring(1);

						newColor = Integer.parseInt(new String(new BigInteger(temp, 2).toByteArray()));

						newImage.setRGB(width, height, new Color(red, newColor, blue).getRGB());
					} else {
						newImage.setRGB(width, height, new Color(red, green, blue).getRGB());
					}
				}
			}
			Messenger.encryptionImage = newImage;
			Messenger.work();
		} catch (Exception e) {
		}
	}
}
