package edu.uis.recitree.controller;

import edu.uis.recitree.App;
import edu.uis.recitree.exception.*;
import edu.uis.recitree.model.Ingredient;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FavoritesController implements Initializable {

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
    private Button removeRecipeButton;

    @FXML
    private Button favoriteButton;

    @FXML
    private Button editRecipeButton;

    private ObservableList<Recipe> recipes;
    private ObservableList<RecipeIngredient> ingredients;

    private RecipeServiceImpl recipeService;

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
            int i = recipesListView.getSelectionModel().selectedIndexProperty().get();

            // get the updated recipe list
            fetchRecipes();

            if (recipes.size() > 0) {
                // select the first recipe
                recipesListView.getSelectionModel().select(0);

                // update the detail view (changes the favorite button color)
                selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();
                updateDetails(selectedRecipe);
            } else {
                clearDetails();
            }

        } catch (InvalidIDException | ReadRecipeException | ToggleFavoriteStatusException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("error favoriting recipe");
            alert.showAndWait();
        }
    }

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

    @FXML
    void returnButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("master-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage sourceStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sourceStage.setScene(scene);
    }

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
     * Populates the listview with the favorite recipes. If an error occurs when fetching the recipe favorites
     * an alert is displayed to the user with the error message.
     *
     * (requirement 4.7.0)
     */
    private void fetchRecipes() {
        try {
            ArrayList<Recipe> recipesArray = recipeService.readAllFavoriteRecipes();
            recipes.clear();
            recipes.addAll(recipesArray);
        } catch (ReadAllFavoritesException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There was a problem fetching favorites: \n" + e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateDetails(Recipe recipe) {
        recipeNameLabel.setText(recipe.getName());
        recipeServingsLabel.setText("Servings: " + recipe.getServings());
        recipeDirectionsTextArea.setText(recipe.getInstructions());
        ingredients.clear();
        ingredients.addAll(recipe.getIngredients());

        if (recipe.isFavorite()) {
            favoriteButton.setStyle("-fx-background-color: #00ff00");
        } else {
            favoriteButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-radius: 90;");
        }
    }

    private void clearDetails() {
        recipeNameLabel.setText("");
        recipeServingsLabel.setText("");
        recipeDirectionsTextArea.setText("");
        ingredients.clear();
        favoriteButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-radius: 90;");
    }
}
