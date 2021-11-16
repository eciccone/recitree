package edu.uis.recitree.controller;

import edu.uis.recitree.exception.CreateRecipeException;
import edu.uis.recitree.exception.ReadAllIngredientsException;
import edu.uis.recitree.model.Ingredient;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;
import edu.uis.recitree.service.IngredientServiceImpl;
import edu.uis.recitree.service.RecipeService;
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

/**
 * The create-recipe-view.fxml controller.
 *
 * @author Mahmoud Radwan
 * @author (further edited) Edward Ciccone
 */
public class CreateRecipeController implements Initializable {

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

    /**
     * Adds a recipe ingredient to the recipe ingredient list view.
     *
     * @param event The add recipe ingredient button clicked ActionEvent
     */
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

    /**
     * Cancels creating a new recipe and closes the window.
     *
     * @param event The cancel button clicked ActionEvent
     */
    @FXML
    void cancelRecipeButtonClicked(ActionEvent event) {
        Button button = (Button) event.getSource();
        Stage window = (Stage) button.getScene().getWindow();
        window.close();
    }

    /**
     * Creates a recipe when the create button is clicked. Name must be included, must contain ingredients, and the
     * serving size must be greater then zero. The recipe instructions are optional. If there is an error creating the
     * recipe, an alert is shown to the user with the error message.
     *
     * (requirement 4.3.0)
     *
     * @param event The ActionEvent that took place
     */
    @FXML
    void createRecipeButtonClicked(ActionEvent event) {

        // name must exist, ingredients must exist, serving size must exist
        if (recipeNameTextField.getText().equals("") ||
                recipeServingsTextField.getText().equals("") ||
                recipeIngredients.size() == 0) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please make sure you entered a \nrecipe name, servings, and ingredients.");
            alert.showAndWait();

            return;
        }

        String recipeName = recipeNameTextField.getText();
        double recipeServings = Double.valueOf(recipeServingsTextField.getText());
        String recipeDirections = recipeDirectionsTextArea.getText();
        ArrayList<RecipeIngredient> ingredients = new ArrayList<RecipeIngredient>(recipeIngredients);

        // serving size must be greater than zero
        if (recipeServings <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Serving size must be greater then zero.");
            alert.showAndWait();
            return;
        }

        try {
            Recipe recipe = recipeService.createRecipe(recipeName, recipeServings, ingredients, recipeDirections);
            Button button = (Button) event.getSource();
            Stage window = (Stage) button.getScene().getWindow();
            window.close();
        } catch (CreateRecipeException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Error creating recipe \n" + e.getMessage());
            alert.showAndWait();
            return;
        }

    }

    /**
     * Removes an ingredient from the recipe ingredients list view when the remove ingredient button is clicked.
     *
     * @param event The remove ingredient button clicked ActionEvent
     */
    @FXML
    void removeIngredientButtonClicked(ActionEvent event) {
        RecipeIngredient selectedIngredient = ingredientsListView.getSelectionModel().getSelectedItem();

        if (selectedIngredient == null)
            return;

        recipeIngredients.remove(selectedIngredient);
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