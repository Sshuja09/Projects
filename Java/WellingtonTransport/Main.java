import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Main class is the entry point for the JavaFX application. It sets up the stage and loads the GUI
 * using FXML. It also handles language localization using resource bundles.
 */
public class Main extends Application {

    public static Stage stage; // The primary stage for the application.
    static ResourceBundle bundle; // The resource bundle for language localization.
    static Locale locale; // The locale representing the selected language and region.

    /**
     * The start method is called when the application is launched. it initializes the primary
     * stage and loads the GUI.
     * @param primaryStage The primary stage for the application.
     * @throws Exception If an error occurs during application startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        // load the strings for language support
        // currently en_NZ and mi_NZ are supported
        locale = new Locale("en", "NZ");
        bundle = ResourceBundle.getBundle("resources/strings", locale);

        // load the fxml file to set up the GUI
        reload();
    }

    /**
     * Sets the locale for language localization.
     * @param locale The new locale to set.
     */
    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("resources/strings", locale);
    }

    /**
     * Reloads the GUI by loading the FXML file, setting the stage properties, and showing the stage.
     * @throws IOException If an error occurs during the loading of the FXML file.
     */
    public void reload() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MapView.fxml"), bundle);
        Parent root = loader.load();
        stage.setTitle(bundle.getString("title")); // Set the title of the window from the bundle.
        stage.setScene(new Scene(root, 800, 700)); // Sets the scene with the loaded GUI.
        stage.show(); // Shows the stage
        stage.setOnCloseRequest(e -> { // Handle window close event by exiting the application.
            System.exit(0);
        });
    }

    /**
     * The main method is the entry point of the application.
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
