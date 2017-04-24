package ua.kas.main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

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
		text = RSA.encryptText(text);

		String array[] = new String[text.length()];

		for (int i = 0; i < text.length(); i++) {
			array[i] = text.substring(i, i + 1);
		}

		String binary = "";
		// 1100000 1100010 1100100 1100110 1101000 1101010 1101100 1101110
		// 1110000 1110001
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals("0")) {
				binary += "1100000";
			} else if (array[i].equals("1")) {
				binary += "1100010";
			} else if (array[i].equals("2")) {
				binary += "1100100";
			} else if (array[i].equals("3")) {
				binary += "1100110";
			} else if (array[i].equals("4")) {
				binary += "1101000";
			} else if (array[i].equals("5")) {
				binary += "1101010";
			} else if (array[i].equals("6")) {
				binary += "1101100";
			} else if (array[i].equals("7")) {
				binary += "1101110";
			} else if (array[i].equals("8")) {
				binary += "1110000";
			} else if (array[i].equals("9")) {
				binary += "1110001";
			}
		}

		BufferedImage image = ImageIO.read(new File(pathEncryptionImage));
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		int pixel, red, green, blue, newColor;
		String temp = "";

		if (image.getWidth() * image.getHeight() > binary.length()) {
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
		} else {
			JOptionPane.showMessageDialog(null, "The text is very large for this picture!");
		}
	}
}
