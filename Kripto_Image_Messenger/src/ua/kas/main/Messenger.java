package ua.kas.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.stage.Stage;

public class Messenger extends Application {

	static boolean server;

	static String address;

	@Override
	public void start(Stage arg0) throws Exception {
		if (server) {
			ServerSocket serverSocket = new ServerSocket(13085);
			Socket socket = serverSocket.accept();
			InputStream inputStream = socket.getInputStream();

			System.out.println("Reading: " + System.currentTimeMillis());

			byte[] sizeAr = new byte[4];
			inputStream.read(sizeAr);
			int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

			byte[] imageAr = new byte[size];
			inputStream.read(imageAr);

			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

			System.out.println(
					"Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());
			ImageIO.write(image, "png", new File("3.png"));

			serverSocket.close();
		} else {
			Socket socket = new Socket("localhost", 13085);
			OutputStream outputStream = socket.getOutputStream();

			BufferedImage image = ImageIO.read(new File("2.png"));

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", byteArrayOutputStream);

			byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
			outputStream.write(size);
			outputStream.write(byteArrayOutputStream.toByteArray());
			outputStream.flush();
			System.out.println("Flushed: " + System.currentTimeMillis());

			// Thread.sleep(120000);
			System.out.println("Closing: " + System.currentTimeMillis());
			socket.close();
		}
	}
}
