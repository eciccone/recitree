package edu.uis.recitree;

import edu.uis.recitree.database.SQLiteConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    /**
     * The start of our application. This method is called from the Application launch method.
     *
     * @param stage The stage of our application
     * @throws IOException Thrown if there is an error loading loading-screen.fxml
     */
    @Override
    public void start(Stage stage) throws IOException {
        // load and render the loading-screen.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("loading-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setTitle("Welcome to ReciTree!");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

        // when any key is pressed load the master-view.fxml file
        scene.setOnKeyPressed(keyEvent-> {
            String codeString = keyEvent.getCode().toString();
            if(codeString != null){
                try {
                    changeToMasterScene(scene, stage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Loads and renders the master-view.fxml file.
     *
     * @param scene The scene of the application
     * @param stage The stage of the application
     * @throws IOException Thrown if there is an error loading master-view.fxml
     */
    public void changeToMasterScene(Scene scene, Stage stage) throws IOException {
        FXMLLoader masterViewLoader = new FXMLLoader(App.class.getResource("master-view.fxml"));
        scene = new Scene(masterViewLoader.load());
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setTitle("ReciTree");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Builds the necessary database tables and then launches the JavaFX application.
     *
     * @param args
     */
    public static void main(String[] args) {
        SQLiteConnection.buildTables();
        launch();
    }
}