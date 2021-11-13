package edu.uis.recitree.controller;

import edu.uis.recitree.App;
import edu.uis.recitree.exception.SearchRecipeException;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;
import edu.uis.recitree.service.RecipeServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    @FXML
    private Button returnButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button clearButton;

    @FXML
    private ListView<Recipe> recipesListView;

    @FXML
    private Label recipeNameLabel;

    @FXML
    private Label recipeServingsLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextArea recipeDirectionsTextArea;

    @FXML
    private ListView<RecipeIngredient> recipeIngredientsListView;

    private RecipeServiceImpl recipeService;
    private ObservableList ingredients;
    private ObservableList recipes;


    /**
     * Clears the search bar text field on click
     *
     * @param event
     */
    @FXML
    void clearButtonClicked(ActionEvent event) {
        searchTextField.setText("");
        clearDetails();
        recipes.clear();
    }

    /**
     * Returns to the navigation view (master-view.fxml).
     *
     * @param event The ActionEvent that took place
     * @throws IOException Thrown if the master-view.fxml file cannot be loaded
     */
    @FXML
    void returnButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("master-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage sourceStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        sourceStage.setScene(scene);
    }


    /**
     * Populates listView of recipes based on text in the search textField
     * List is only populated after button is clicked by user
     *
     * (requirement 4.8.0)
     *
     * @param event
     */
    @FXML
    void searchButtonClicked(ActionEvent event) {

        String searchedText = searchTextField.getText();

        try {
            ArrayList<Recipe> matchingRecipes = recipeService.searchRecipesByName(searchedText);
            recipes.clear();
            recipes.addAll(matchingRecipes);
        } catch (SearchRecipeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("SEARCH ERROR\n" + e.getMessage());
            alert.showAndWait();
        }


    }

    /**
     * Initializes the controller.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recipeService = new RecipeServiceImpl();
        recipes = FXCollections.observableArrayList();
        ingredients = FXCollections.observableArrayList();

        recipesListView.setOnMouseClicked(mouseEvent -> {
            Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();

            if (selectedRecipe == null) return;

            updateDetails(selectedRecipe);
        });

        recipesListView.setItems(recipes);
        recipeIngredientsListView.setItems(ingredients);
    }

    /**
     * Displays the details of a recipe. The favorite status is displayed through the background color of the favorite
     * button.
     *
     * (requirement 4.2.0)
     *
     * @param recipe The recipe that's details will be displayed
     */
    private void updateDetails(Recipe recipe) {
        recipeNameLabel.setText(recipe.getName());
        recipeServingsLabel.setText("Servings: " + recipe.getServings());
        recipeDirectionsTextArea.setText(recipe.getInstructions());
        ingredients.clear();
        ingredients.addAll(recipe.getIngredients());
    }

    /**
     * Clears the recipe details area of the view.
     */
    private void clearDetails() {
        recipeNameLabel.setText("");
        recipeServingsLabel.setText("");
        recipeDirectionsTextArea.setText("");
        ingredients.clear();
    }
}
