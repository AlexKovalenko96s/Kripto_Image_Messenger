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
		decoding = decoding.substring(0, decoding.indexOf("11001010110111001100100"));

		String out = new String(new BigInteger(decoding, 2).toByteArray());
		tf_outText.setText(out);
		iv_decoding.setImage(SwingFXUtils.toFXImage(image, null));
		System.out.println("end");

		Thread.currentThread();
	}
}
