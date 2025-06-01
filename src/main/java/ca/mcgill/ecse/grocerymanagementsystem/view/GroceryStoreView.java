package ca.mcgill.ecse.grocerymanagementsystem.view;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GroceryStoreView extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		// https://github.com/openjfx/samples/blob/master/HelloFX/Gradle/hellofx/src/main/java/HelloFX.java
		// creat an URL 
		URL MainPageUrl = getClass().getResource("/views/MainPage.fxml");
		Pane MainPage = FXMLLoader.load(MainPageUrl);
		Scene scene = new Scene(MainPage, 640, 480);
		stage.setScene(scene);
		stage.setTitle("Grocery Management System");
		stage.show();
	}
}
