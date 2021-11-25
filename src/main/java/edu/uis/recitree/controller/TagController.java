package edu.uis.recitree.controller;

import edu.uis.recitree.App;
import edu.uis.recitree.exception.TagException;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;
import edu.uis.recitree.service.TagService;
import edu.uis.recitree.service.TagServiceImpl;
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

public class TagController implements Initializable {

    @FXML
    private Button returnButton;

    @FXML
    private ListView<String> tagsListView;

    @FXML
    private ListView<Recipe> recipeListView;

    @FXML
    private ListView<RecipeIngredient> recipeIngredientsListView;

    @FXML
    private Label recipeNameLabel;

    @FXML
    private Label recipeServingsLabel;

    @FXML
    private TextArea recipeDirectionsTextArea;

    private TagService tagService;
    private Recipe selectedRecipe;
    private ObservableList tags;
    private ObservableList recipes;
    private ObservableList ingredients;

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
        Stage sourceStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sourceStage.setScene(scene);
    }


    /**
     * Initializes the controller
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tagService = new TagServiceImpl();
        tags = FXCollections.observableArrayList();
        recipes = FXCollections.observableArrayList();
        ingredients = FXCollections.observableArrayList();

        tagsListView.setOnMouseClicked(mouseEvent -> {
            String selectedTag = tagsListView.getSelectionModel().getSelectedItem();

            if(selectedTag == null) return;

            fetchRecipesWithTag(selectedTag);
        });

        recipeListView.setOnMouseClicked(mouseEvent -> {
            Recipe selectedRecipe = recipeListView.getSelectionModel().getSelectedItem();

            if (selectedRecipe == null) return;

            updateDetails(selectedRecipe);
        });

        tagsListView.setItems(tags);
        recipeListView.setItems(recipes);
        recipeIngredientsListView.setItems(ingredients);

        fetchTags();
    }


    /**
     * Retrieves all tags and populates tagListView
     * with said tags
     *
     * Executed on initialize
     *
     * (requirement 5.1.0)
     */
    private void fetchTags(){
        try {
            ArrayList<String> allTags = tagService.getAllTags();
            tags.clear();
            tags.addAll(allTags);
        } catch (TagException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There was a problem fetching all tags");
            alert.showAndWait();
        }
    }

    /**
     * Retrieves all recipes with given tag and populates
     * recipesListView with said recipes
     *
     * (requirement 5.2.0)
     *
     * @param tag
     */
    private void fetchRecipesWithTag(String tag){
        try {
            ArrayList<Recipe> taggedRecipes = tagService.getAllRecipesWithTag(tag);
            recipes.clear();
            recipes.addAll(taggedRecipes);
        } catch (TagException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There was a problem fetching recipes with tag: " + tag);
            alert.showAndWait();
        }
    }


    /**
     * Displays the details of a recipe. Recipe is selected from recipesListView
     * which is filtered by the current selected tag
     *
     * (requirement 5.3.0)
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
}
