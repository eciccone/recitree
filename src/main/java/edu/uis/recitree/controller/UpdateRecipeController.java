package edu.uis.recitree.controller;

import edu.uis.recitree.exception.ReadAllIngredientsException;
import edu.uis.recitree.exception.UpdateRecipeException;
import edu.uis.recitree.model.Ingredient;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;
import edu.uis.recitree.service.IngredientServiceImpl;
import edu.uis.recitree.service.RecipeServiceImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

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
    private ChoiceBox<String> ingredientUnitTypeChoiceBox;

    @FXML
    private TextField ingredientUnitAmountTextField;

    private ObservableList<RecipeIngredient> recipeIngredients;
    private RecipeServiceImpl recipeService;
    private IngredientServiceImpl ingredientService;

    private Recipe selectedRecipe;

    @FXML
    void addIngredientButtonClicked(ActionEvent event) {
        if (ingredientNameTextField.getText().equals("") ||
                ingredientUnitTypeChoiceBox.getValue() == null ||
                ingredientUnitAmountTextField.getText().equals("")) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please make sure you entered an \ningredient name, unit type, and unit amount.");
            alert.showAndWait();

            return;
        }

        Ingredient ingredient = new Ingredient(ingredientNameTextField.getText());
        String unitType = ingredientUnitTypeChoiceBox.getValue();
        double unitAmount = Double.valueOf(ingredientUnitAmountTextField.getText());

        RecipeIngredient recipeIngredient = new RecipeIngredient(ingredient, unitType, unitAmount);

        recipeIngredients.add(recipeIngredient);

        ingredientNameTextField.setText("");
        ingredientUnitTypeChoiceBox.setValue(null);
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

    /**
     * Updates a recipe when the update button is clicked. The name must be included, ingredients must be included, and
     * the serving size must be greater then 0. The recipe instructions are optional. If an error occurs when updating
     * a recipe, an alert is displayed to the user with the error message.
     *
     * (requirement 4.5.0)
     *
     * @param event The ActionEvent that took place
     */
    @FXML
    void updateRecipeButtonClicked(ActionEvent event) {
        // Name must exist, ingredients must exist, serving size must exist
        if (recipeNameTextField.getText().equals("") ||
                recipeServingsTextField.getText().equals("") ||
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

        // Serving size must be greater then zero
        if (recipeServings <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Serving size must be greater then zero.");
            alert.showAndWait();
            return;
        }

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
        ingredientService = new IngredientServiceImpl();
        recipeIngredients = FXCollections.observableArrayList();
        ingredientsListView.setItems(recipeIngredients);

        try {
            ArrayList<Ingredient> autoIngredients = ingredientService.readAllIngredients();
            TextFields.bindAutoCompletion(ingredientNameTextField, autoIngredients);
        } catch (ReadAllIngredientsException e) {
            e.printStackTrace();
        }

        // restrict recipeServingsTextfield and ingredientUnitAmountTextField to only accept decimals
        ChangeListener<String> recipeServingsListener = decimalRestrictionListener(recipeServingsTextField);
        ChangeListener<String> ingredientUnitAmountListener = decimalRestrictionListener(ingredientUnitAmountTextField);
        recipeServingsTextField.textProperty().addListener(recipeServingsListener);
        ingredientUnitAmountTextField.textProperty().addListener(ingredientUnitAmountListener);

        buildChoiceBox();
    }

    /**
     * TextField listener to only accept decimal numbers whos values are up to 7 digits long, and decimal point is up
     * to 2 digits long.
     *
     * @param textField The Textfield to apply the listener to
     * @return The listener as a lambda
     */
    private ChangeListener<String> decimalRestrictionListener(TextField textField) {
        return (observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,2})?")) {
                textField.setText(oldValue);
            }
        };
    }

    /**
     * Sets the recipe that is being updated.
     *
     * @param recipe The recipe that is being updated
     */
    public void setRecipe(Recipe recipe){

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

    /**
     * Adds the ingredient unit types to the choice box.
     */
    private void buildChoiceBox() {
        ingredientUnitTypeChoiceBox.getItems().add("qty");
        ingredientUnitTypeChoiceBox.getItems().add("tsp");
        ingredientUnitTypeChoiceBox.getItems().add("tbsp");
        ingredientUnitTypeChoiceBox.getItems().add("cup");
        ingredientUnitTypeChoiceBox.getItems().add("oz");
        ingredientUnitTypeChoiceBox.getItems().add("lb");
    }

}
