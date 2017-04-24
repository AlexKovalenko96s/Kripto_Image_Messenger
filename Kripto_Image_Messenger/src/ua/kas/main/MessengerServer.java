package ua.kas.main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigInteger;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class MessengerServer {

	@FXML
	TextField tf_outText;
	@FXML
	TextField tf_outKey;

	@FXML
	ImageView iv_decoding;

	static BufferedImage decodingImage;

	public void go() throws IOException, InterruptedException {
		Messenger.work();

		int pixel, green;
		String temp = "", decoding = "";
		BufferedImage image = decodingImage;
		for (int width = 0; width < image.getWidth(); width++) {
			for (int height = 0; height < image.getHeight(); height++) {
				pixel = image.getRGB(width, height);

				green = (pixel >> 8) & 0xff;

				temp = new BigInteger(Integer.toString(green).getBytes("UTF-8")).toString(2);
				decoding += temp.substring(temp.length() - 1);
			}
		}

		String lenghtText = "", lenghtD = "", lenghtN = "", textString = "", dString = "", nString = "";
		int textInt = 0, dInt = 0, nInt = 0;

		for (int i = 0; i < 3; i++) {
			lenghtText += toString(decoding.substring(i * 7, i * 7 + 7));
		}

		for (int i = 3; i < 6; i++) {
			lenghtD += toString(decoding.substring(i * 7, i * 7 + 7));
		}

		for (int i = 6; i < 9; i++) {
			lenghtN += toString(decoding.substring(i * 7, i * 7 + 7));
		}

		textInt = Integer.parseInt(lenghtText);
		dInt = Integer.parseInt(lenghtD);
		nInt = Integer.parseInt(lenghtN);

		decoding = decoding.substring(9 * 7);

		for (int i = 0; i < textInt; i++) {
			textString += toString(decoding.substring(i * 7, i * 7 + 7));
		}

		decoding = decoding.substring(textInt * 7);

		for (int i = 0; i < dInt; i++) {
			dString += toString(decoding.substring(i * 7, i * 7 + 7));
		}

		decoding = decoding.substring(dInt * 7);

		for (int i = 0; i < nInt; i++) {
			nString += toString(decoding.substring(i * 7, i * 7 + 7));
		}

		System.out.println(textInt + " " + dInt + " " + nInt);
		System.out.println(textString);
		System.out.println(dString);
		System.out.println(nString);

		decoding = RSA.decryptText(dString, nString, textString);

		tf_outText.setText(decoding);
		iv_decoding.setImage(SwingFXUtils.toFXImage(image, null));
	}

	public String toString(String s) {
		if (s.equals("1100000")) {
			return "0";
		} else if (s.equals("1100010")) {
			return "1";
		} else if (s.equals("1100100")) {
			return "2";
		} else if (s.equals("1100110")) {
			return "3";
		} else if (s.equals("1101000")) {
			return "4";
		} else if (s.equals("1101010")) {
			return "5";
		} else if (s.equals("1101100")) {
			return "6";
		} else if (s.equals("1101110")) {
			return "7";
		} else if (s.equals("1110000")) {
			return "8";
		} else if (s.equals("1110001")) {
			return "9";
		}
		return "69";
	}
}
