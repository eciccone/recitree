package edu.uis.recitree.controller;

import edu.uis.recitree.App;
import edu.uis.recitree.exception.DeleteIngredientException;
import edu.uis.recitree.exception.DeleteRecipeException;
import edu.uis.recitree.exception.InvalidIDException;
import edu.uis.recitree.exception.ReadAllIngredientsException;
import edu.uis.recitree.model.Ingredient;
import edu.uis.recitree.service.IngredientServiceImpl;
import edu.uis.recitree.service.RecipeServiceImpl;
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

public class IngredientsController implements Initializable {

    @FXML
    private ListView<Ingredient> ingredientsListView;

    @FXML
    private Button removeIngredientButton;

    @FXML
    private Button returnButton;

    private ObservableList<Ingredient> ingredients;
    private IngredientServiceImpl ingredientService;

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

    @FXML
    void returnButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("master-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage sourceStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sourceStage.setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ingredientService = new IngredientServiceImpl();
        ingredients = FXCollections.observableArrayList();

        ingredientsListView.setItems(ingredients);

        fetchIngredients();
    }

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
