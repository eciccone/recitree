package edu.uis.recitree.controller;

import edu.uis.recitree.App;
import edu.uis.recitree.exception.DeleteIngredientException;
import edu.uis.recitree.exception.InvalidIDException;
import edu.uis.recitree.exception.ReadAllIngredientsException;
import edu.uis.recitree.model.Ingredient;
import edu.uis.recitree.service.IngredientService;
import edu.uis.recitree.service.IngredientServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * The ingredient-view.fxml controller.
 *
 * @author Mahmoud Radwan
 */
public class IngredientsController implements Initializable {

    @FXML
    private ListView<Ingredient> ingredientsListView;

    @FXML
    private Button removeIngredientButton;

    @FXML
    private Button returnButton;

    private ObservableList<Ingredient> ingredients;
    private IngredientService ingredientService;

    /**
     * When the removeIngredientButton is clicked it will delete the ingredient that is selected. If no ingredient is
     * selected an alert will be displayed to the user telling them to select a ingredient. If an error occurs deleting
     * the ingredient, an alert is displayed to the user with the error message.
     *
     * (requirement 3.2)
     *
     * @param event The event that took place
     */
    @FXML
    void removeIngredientButtonClicked(ActionEvent event) {
        Ingredient selectedIngredient = ingredientsListView.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You must first select an ingredient to delete");
            alert.showAndWait();
            return;
        }

        try {
            ingredientService.deleteIngredient(selectedIngredient.getId());
            fetchIngredients();
        } catch (InvalidIDException | DeleteIngredientException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error Removing Ingredient\n" + e.getMessage());
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
        Stage sourceStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        ingredientService = new IngredientServiceImpl();
        ingredients = FXCollections.observableArrayList();

        ingredientsListView.setItems(ingredients);

        fetchIngredients();
    }

    /**
     * Populates the list view with all the unused ingredients. If an error occurs fetching the ingredients an alert
     * is displayed to the user with an error message.
     *
     * (requirement 3.1.0)
     */
    private void fetchIngredients(){
        try {
            ArrayList<Ingredient> ingredientArray = ingredientService.readAllUnusedIngredients();
            ingredients.clear();
            ingredients.addAll(ingredientArray);
        } catch (ReadAllIngredientsException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ERROR GETTING INGREDIENTS");
            alert.showAndWait();
        }
    }
}
