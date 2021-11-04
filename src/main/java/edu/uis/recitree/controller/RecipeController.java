package edu.uis.recitree.controller;

import edu.uis.recitree.App;
import edu.uis.recitree.exception.DeleteRecipeException;
import edu.uis.recitree.exception.InvalidIDException;
import edu.uis.recitree.exception.ReadAllRecipesException;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RecipeController {

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
    void favoriteButtonClicked(ActionEvent event) {

        // Test Stuff
        recipes = FXCollections.observableArrayList();
        ingredients = FXCollections.observableArrayList();

        Ingredient testIngredient = new Ingredient(0,"testIngredient");
        RecipeIngredient testRecipeIngredient = new RecipeIngredient(testIngredient, "oz.", 2);
        ArrayList<RecipeIngredient> testArray = new ArrayList<>();
        testArray.add(testRecipeIngredient);

        Ingredient testIngredient1 = new Ingredient(0,"testIngredient1");
        RecipeIngredient testRecipeIngredient1 = new RecipeIngredient(testIngredient, "lbs.", 3);
        ArrayList<RecipeIngredient> testArray1 = new ArrayList<>();
        testArray1.add(testRecipeIngredient1);

        Recipe test = new Recipe(1, "test", 2.0 , testArray, "do stuff", false);
        Recipe test1 = new Recipe(2, "test1", 1.5 , testArray1, "do stuff", false);
        recipes.add(test);
        recipes.add(test1);

        recipesListView.setOnMouseClicked(mouseEvent -> {
            Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();

            if (selectedRecipe == null) return;

            recipeNameLabel.setText(selectedRecipe.getName());
            recipeServingsLabel.setText("Servings: " + selectedRecipe.getServings());
            recipeDirectionsTextArea.setText(selectedRecipe.getInstructions());
            ingredients.clear();
            ingredients.addAll(selectedRecipe.getIngredients());
        });

        recipesListView.setItems(recipes);
        recipeIngredientsListView.setItems(ingredients);
        // END OF TEST STUFF

        Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();

        if(selectedRecipe == null){
            return;
        }

        boolean currFavStatus = selectedRecipe.isFavorite();
        selectedRecipe.setFavorite(!currFavStatus);

    }

    @FXML
    void editRecipeButtonClicked(ActionEvent event) throws IOException {
        Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();

        if(selectedRecipe == null){
            return;
        }

        FXMLLoader updateRecipeLoader = new FXMLLoader(App.class.getResource("update-recipe-view.fxml"));
        Scene scene = new Scene(updateRecipeLoader.load());
        Stage stage = new Stage();

        UpdateRecipeController updateRecipeController = updateRecipeLoader.getController();
        updateRecipeController.getRecipeInfo(selectedRecipe);

        stage.setTitle("Edit Recipe");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    }

    @FXML
    void removeRecipeButtonClicked(ActionEvent event) {
        Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();

        if(selectedRecipe == null){
            return;
        }

        try {
            recipeService.deleteRecipe(selectedRecipe.getId());
        } catch (InvalidIDException e) {
            e.printStackTrace();
        } catch (DeleteRecipeException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void returnButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("master-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage sourceStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        sourceStage.setScene(scene);
    }

    /*@Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        recipeService = new RecipeServiceImpl();
        recipes = FXCollections.observableArrayList();
        ingredients = FXCollections.observableArrayList();

        recipesListView.setOnMouseClicked(mouseEvent -> {
            Recipe selectedRecipe = recipesListView.getSelectionModel().getSelectedItem();

            if (selectedRecipe == null) return;

            recipeNameLabel.setText(selectedRecipe.getName());
            recipeServingsLabel.setText("Servings: " + selectedRecipe.getServings());
            recipeDirectionsTextArea.setText(selectedRecipe.getInstructions());
            ingredients.clear();
            ingredients.addAll(selectedRecipe.getIngredients());
        });

        recipesListView.setItems(recipes);
        recipeIngredientsListView.setItems(ingredients);

        try {
            fetchRecipes();
        } catch (ReadAllRecipesException e) {
            e.printStackTrace();
        }
    }

     */


    private void fetchRecipes() {
        try {
            ArrayList<Recipe> recipesArray = recipeService.readAllRecipes();
            recipes.clear();
            recipes.addAll(recipesArray);
        } catch (ReadAllRecipesException e) {
            e.printStackTrace();
        }
    }
}
