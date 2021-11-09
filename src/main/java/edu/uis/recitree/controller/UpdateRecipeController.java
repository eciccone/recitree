package edu.uis.recitree.controller;

import edu.uis.recitree.exception.UpdateRecipeException;
import edu.uis.recitree.model.Ingredient;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;
import edu.uis.recitree.service.RecipeServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UpdateRecipeController implements Initializable {

    @FXML
    private Label recipeIdLabel;

    @FXML
    private TextField recipeNameTextField;

    @FXML
    private TextField recipeServingsTextField;

    @FXML
    private TextArea recipeDirectionsTextArea;

    @FXML
    private ListView<RecipeIngredient> ingredientsListView;

    @FXML
    private TextField ingredientNameTextField;

    @FXML
    private TextField ingredientUnitTypeTextField;

    @FXML
    private TextField ingredientUnitAmountTextField;

    private ObservableList<RecipeIngredient> recipeIngredients;
    private RecipeServiceImpl recipeService;
    private Recipe selectedRecipe;

    @FXML
    void addIngredientButtonClicked(ActionEvent event) {
        if (ingredientNameTextField.getText().equals("") ||
                ingredientUnitTypeTextField.getText().equals("") ||
                ingredientUnitAmountTextField.getText().equals("")) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please make sure you entered an \ningredient name, unit type, and unit amount.");
            alert.showAndWait();

            return;
        }

        Ingredient ingredient = new Ingredient(ingredientNameTextField.getText());
        String unitType = ingredientUnitTypeTextField.getText();
        double unitAmount = Double.valueOf(ingredientUnitAmountTextField.getText());

        RecipeIngredient recipeIngredient = new RecipeIngredient(ingredient, unitType, unitAmount);

        recipeIngredients.add(recipeIngredient);

        ingredientNameTextField.setText("");
        ingredientUnitTypeTextField.setText("");
        ingredientUnitAmountTextField.setText("");
    }

    @FXML
    void cancelRecipeButtonClicked(ActionEvent event) {
        Button button = (Button) event.getSource();
        Stage window = (Stage) button.getScene().getWindow();
        window.close();
    }

    @FXML
    void removeIngredientButtonClicked(ActionEvent event) {
        RecipeIngredient selectedIngredient = ingredientsListView.getSelectionModel().getSelectedItem();

        if (selectedIngredient == null)
            return;

        recipeIngredients.remove(selectedIngredient);
    }

    @FXML
    void updateRecipeButtonClicked(ActionEvent event) {

        if (recipeNameTextField.getText().equals("") ||
                recipeServingsTextField.getText().equals("") ||
                recipeDirectionsTextArea.getText().equals("") ||
                recipeIngredients.size() == 0) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please make sure you entered a \nrecipe name, servings, ingredients, and directions.");
            alert.showAndWait();

            return;
        }

        String recipeName = recipeNameTextField.getText();
        double recipeServings = Double.valueOf(recipeServingsTextField.getText());
        String recipeDirections = recipeDirectionsTextArea.getText();
        ArrayList<RecipeIngredient> ingredients = new ArrayList<RecipeIngredient>(recipeIngredients);

        try {
            recipeService.updateRecipe(selectedRecipe.getId(), recipeName, recipeServings, ingredients, recipeDirections, selectedRecipe.isFavorite());
        } catch (UpdateRecipeException e) {
            e.printStackTrace();
        }

        Button button = (Button) event.getSource();
        Stage window = (Stage) button.getScene().getWindow();
        window.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        recipeService = new RecipeServiceImpl();
        recipeIngredients = FXCollections.observableArrayList();
        ingredientsListView.setItems(recipeIngredients);
    }

    public void getRecipeInfo(Recipe recipe){

        selectedRecipe = recipe;

        int recipeId = recipe.getId();
        String recipeName = recipe.getName();
        double recipeServings = recipe.getServings();
        ArrayList<RecipeIngredient> selectedRecipeIngredientsList = recipe.getIngredients();
        recipeIngredients.addAll(selectedRecipeIngredientsList);
        String recipeInstructions = recipe.getInstructions();

        recipeIdLabel.setText("ID: " + recipeId);
        recipeNameTextField.setText(recipeName);
        recipeServingsTextField.setText(String.valueOf(recipeServings));
        recipeDirectionsTextArea.setText(recipeInstructions);
        ingredientsListView.setItems(recipeIngredients);

    }

}
