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

/**
 * The master-view.fxml controller.
 *
 * @author Mahmoud Radwan
 */
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
    private Button searchRecipesButton;

    /**
     * Determines which view to render when an option is chosen from the choice of navigation buttons.
     *
     * @param event The button click ActionEvent
     * @throws IOException Thrown if there is an error when rendering a fxml file
     */
    @FXML
    void navButtonClicked(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();

        switch (clickedButton.getId()) {
            case "recipesButton":
                renderRecipeView(event);
                break;
            case "favoritesButton":
                renderFavoriteView(event);
                break;
            case "createRecipeButton":
                renderCreateRecipeView(event);
                break;
            case "manageIngredientsButton":
                renderIngredientManagementView(event);
                break;
            case "searchRecipesButton":
                renderSearchRecipeView(event);
                break;
        }
    }

    /**
     * Renders the recipe-view.fxml file.
     *
     * @param event The button click ActionEvent
     * @throws IOException Thrown if there is an error when rendering recipe-view.fxml
     */
    private void renderRecipeView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("recipe-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage sourceStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        sourceStage.setScene(scene);
    }

    /**
     * Renders the favorites-view.fxml file.
     *
     * @param event The button click ActionEvent
     * @throws IOException Thrown if there is an error when rendering favorites-view.fxml
     */
    private void renderFavoriteView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("favorites-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage sourceStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        sourceStage.setScene(scene);
    }

    /**
     * Renders the create-recipe-view.fxml file.
     *
     * @param event The button click ActionEvent
     * @throws IOException Thrown if there is an error when rendering create-recipe-view.fxml
     */
    private void renderCreateRecipeView(ActionEvent event) throws IOException {
        FXMLLoader createRecipeLoader = new FXMLLoader(App.class.getResource("create-recipe-view.fxml"));
        Scene scene = new Scene(createRecipeLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Create a new recipe");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * Renders the ingredients-view.fxml file.
     *
     * @param event The button click ActionEvent
     * @throws IOException Thrown if there is an error when rendering ingredients-view.fxml
     */
    private void renderIngredientManagementView(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ingredients-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage sourceStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        sourceStage.setMinWidth(300);
        sourceStage.setScene(scene);
    }

    /**
     * Renders the search-view.fxml file.
     *
     * @param event The button click ActionEvent
     * @throws IOException Thrown if there is an error when rendering search-view.fxml
     */
    private void renderSearchRecipeView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("search-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage sourceStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        sourceStage.setScene(scene);
    }

}
