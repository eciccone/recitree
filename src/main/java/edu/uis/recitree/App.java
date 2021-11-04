package edu.uis.recitree;

import edu.uis.recitree.database.SQLiteConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("loading-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setTitle("Welcome to ReciTree!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
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



    public static void main(String[] args) {
        SQLiteConnection.buildTables();
        launch();
    }
}