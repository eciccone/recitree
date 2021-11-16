package edu.uis.recitree.controller;

import edu.uis.recitree.App;
import edu.uis.recitree.exception.*;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;
import edu.uis.recitree.service.RecipeServiceImpl;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * The recipe-view.fxml controller.
 *
 * @author Mahmoud Radwan
 * @author (edited) Edward Ciccone
 */
public class RecipeController implements Initializable {

    @FXML
    private ListView<Recipe> recipesListView;

    @FXML
    private ListView<RecipeIngredient> recipeIngredientsListView;

    @FXML
    private Label recipeNameLabel;

    @FXML
    private Label recipeServingsLabel;

    @FXML
    private TextArea recipeDirectionsTextArea;

    @FXML
    private Button returnButton;

    @FXML
    private Button createRecipeButton;

    @FXML
    private Button removeRecipeButton;

    @FXML
    private Button editRecipeButton;

    @FXML
    private Button favoriteButton;

    private ObservableList<Recipe> recipes;
    private ObservableList<RecipeIngredient> ingredients;

    private RecipeServiceImpl recipeService;

    /**
     * Loads the view for creating a recipe when createRecipeButton is clicked.
     *
     * @param event the ActionEvent that took place
     * @throws IOException Thrown if there is an error loading create-recipe-view.fxml
     */
    @FXML
    void createRecipeButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader createRecipeLoader = new FXMLLoader(App.class.getResource("create-recipe-view.fxml"));
        Scene scene = new Scene(createRecipeLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Create a new recipe");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        // remember what recipe was selected, if any
        int i = recipesListView.getSelectionModel().getSelectedIndex();

        // update the recipe list view
        fetchRecipes();

        // reselect the recipe
        recipesListView.getSelectionModel().select(i);
    }

    /**
     * Toggles a recipes favorite status when the favorite button is clicked. If no recipe is selected when the favorite
     * button is clicked, an alert is displayed to the user. If there is an error when toggling the recipes favorite
     * status, an alert is displayed to the user with the error message.
     *
     * (requirement 4.6.0)
     *
     * @param event The ActionEvent that took place
     */
    @FXML
    void favoriteButtonClicked(ActionEvent event) {
        Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();

        if(selectedRecipe == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You must first select a recipe before favoriting");
            alert.showAndWait();
            return;
        }

        try {
            // toggle favorite
            recipeService.toggleFavoriteStatus(selectedRecipe.getId());

            // save the index of the selected item before updating recipe list
            int i = recipesListView.getSelectionModel().getSelectedIndex();

            // update the recipe list
            fetchRecipes();

            // select the same recipe that was previously selected
            recipesListView.getSelectionModel().select(i);

            // update the detail view (changes the favorite button color)
            selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();
            updateDetails(selectedRecipe);
        } catch (InvalidIDException | ReadRecipeException | ToggleFavoriteStatusException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("error favoriting recipe \n" + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Loads the view for updating a recipe when editRecipeButton is clicked.
     *
     * @param event the ActionEvent that took place
     * @throws IOException Thrown if there is an error loading update-recipe-view.fxml
     */
    @FXML
    void editRecipeButtonClicked(ActionEvent event) throws IOException {
        Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();
        int i = recipesListView.getSelectionModel().getSelectedIndex();

        if(selectedRecipe == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You must first select a recipe to edit");
            alert.showAndWait();
            return;
        }

        FXMLLoader updateRecipeLoader = new FXMLLoader(App.class.getResource("update-recipe-view.fxml"));
        Scene scene = new Scene(updateRecipeLoader.load());
        Stage stage = new Stage();

        UpdateRecipeController updateRecipeController = updateRecipeLoader.getController();
        updateRecipeController.setRecipe(selectedRecipe);

        stage.setTitle("Edit Recipe");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        fetchRecipes();

        recipesListView.getSelectionModel().select(i);
        selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();
        updateDetails(selectedRecipe);
    }

    /**
     * Deletes the recipe that is selected when the delete button is clicked. If a recipe is not selected, or an error
     * occurs, the user is displayed an alert with the error message.
     *
     * (requirement 4.4.0)
     *
     * @param event The ActionEvent that took place
     */
    @FXML
    void removeRecipeButtonClicked(ActionEvent event) {
        Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();

        if(selectedRecipe == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You must first select a recipe to delete");
            alert.showAndWait();
            return;
        }

        try {
            recipeService.deleteRecipe(selectedRecipe.getId());
            fetchRecipes();
            // Since the item was selected and is not selected anymore, we must clear the detail view
            clearDetails();
        } catch (InvalidIDException | DeleteRecipeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("error removing recipe: \n " + e.getMessage());
            alert.showAndWait();
        }
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

        fetchRecipes();
    }


    /**
     * Populates the list view with recipes. If there is an error fetching the recipes an alert is displayed to the user
     * with the error message.
     *
     * (requirement 4.1.0)
     */
    private void fetchRecipes() {
        try {
            ArrayList<Recipe> recipesArray = recipeService.readAllRecipes();
            recipes.clear();
            recipes.addAll(recipesArray);
        } catch (ReadAllRecipesException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There was a problem fetching the recipes: \n" + e.getMessage());
            alert.showAndWait();
        }
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

        if (recipe.isFavorite()) {
            favoriteButton.setStyle("-fx-background-color: #ff0000; -fx-border-width: 2");
        } else {
            favoriteButton.setStyle("-fx-background-color: transparent; -fx-border-color: #ff0000; -fx-border-width: 2");
        }
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
