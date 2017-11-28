package userinterface;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Simple JavaFX testing
 * 
 * @author Friederike Hanssen
 *
 */
public class JavaFXTest extends Application {

	@Override
	public void start(final Stage stage) throws Exception {

		try {
			// put fxml file in bin/userinterface
			URL url = getClass().getClassLoader().getResource("userinterface/gui.fxml");
			System.out.println(url);
			Parent root = FXMLLoader.load(url);
			Scene scene = new Scene(root);

			stage.setTitle("DACCOR");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}