package edu.uis.recitree.controller;

import edu.uis.recitree.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MasterViewController {

    @FXML
    private Button recipesButton;

    @FXML
    private Button favoritesButton;

    @FXML
    private Button createRecipeButton;

    @FXML
    private Button manageIngredientsButton;

    @FXML
    void createRecipeButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader createRecipeLoader = new FXMLLoader(App.class.getResource("create-recipe-view.fxml"));
        Scene scene = new Scene(createRecipeLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Create a new recipe");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    void manageIngredientsButtonClicked(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ingredients-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage sourceStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        sourceStage.setMinWidth(300);
        sourceStage.setScene(scene);
    }

    @FXML
    void favoritesButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("favorites-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage sourceStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        sourceStage.setScene(scene);
    }

    @FXML
    void recipesButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("recipe-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage sourceStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        sourceStage.setScene(scene);
    }

}
