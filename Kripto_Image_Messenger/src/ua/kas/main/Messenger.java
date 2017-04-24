package ua.kas.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Messenger extends Application {

	static boolean server;

	static String address;

	private static OutputStream out;

	private Parent root;

	private static BufferedImage image;

	static BufferedImage encryptionImage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		if (server) {
			root = FXMLLoader.load(getClass().getResource("Server.fxml"));
		} else {
			root = FXMLLoader.load(getClass().getResource("User.fxml"));
		}
		Scene scene = new Scene(root, 400 - 5, 200 - 5);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle("KriptoImageMessenger");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void work() throws IOException {
		if (server) {
			ServerSocket serverSocket = new ServerSocket(8888);
			Socket socket = serverSocket.accept();
			InputStream inputStream = socket.getInputStream();

			byte[] sizeAr = new byte[4];
			inputStream.read(sizeAr);
			int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
			byte[] imageAr = new byte[size];
			inputStream.read(imageAr);

			image = ImageIO.read(new ByteArrayInputStream(imageAr));

			MessengerServer.decodingImage = image;

			socket.close();
			serverSocket.close();

		} else {
			Socket socket = new Socket(address, 8888);
			out = socket.getOutputStream();

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(encryptionImage, "png", byteArrayOutputStream);

			byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
			out.write(size);
			out.write(byteArrayOutputStream.toByteArray());
			out.flush();
			socket.close();
		}
	}

}
