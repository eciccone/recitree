package edu.uis.recitree.controller;

import edu.uis.recitree.exception.TagException;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.service.TagServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RecipeTagController implements Initializable {

    @FXML
    private Label recipeNameLabel;

    @FXML
    private Label recipeIdLabel;

    @FXML
    private ListView<String> tagListView;

    @FXML
    private TextField tagNameTextField;

    @FXML
    private Button addTagButton;

    @FXML
    private Button removeTagButton;

    private Recipe selectedRecipe;
    private TagServiceImpl tagService;
    private ObservableList recipeTags;

    /**
     * Text in tagNameTextField will be used to create a new tag
     * for the selected recipe
     *
     * Catches TagException if error occurs during creation
     *
     * (requirement 5.4.0)
     *
     * @param event
     */
    @FXML
    void addTagButtonClicked(ActionEvent event) {
        if(tagNameTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please make sure you entered a tag name.");
            alert.showAndWait();

            return;
        }

        String newTag = tagNameTextField.getText();
        try {
            tagService.createRecipeTag(newTag, selectedRecipe);
        } catch (TagException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("ERROR ADDING TAG: " + newTag);
            alert.showAndWait();
            e.printStackTrace();
        }

        tagNameTextField.setText("");
        updateTagList();
    }


    /**
     * Selected tag will be deleted from the list of tags of
     * the selected recipe
     *
     * (requirement 5.5.0)
     *
     * @param event
     */
    @FXML
    void removeTagButtonClicked(ActionEvent event) {
        String selectedTag = tagListView.getSelectionModel().getSelectedItem();

        if(selectedTag == null)
            return;

        try {
            tagService.removeRecipeTag(selectedTag, selectedRecipe);
        } catch (TagException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("ERROR REMOVING TAG " + selectedTag);
            alert.showAndWait();
            e.printStackTrace();
        }

        updateTagList();
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
        recipeTags = FXCollections.observableArrayList();

        tagListView.setItems(recipeTags);
    }


    /**
     * Called when loading controller from RecipeController
     * Sets Recipe ID and Name labels appropriately
     *
     * Gets list of tags and adds them to the tagListView
     *
     * @param recipe
     */
    public void setRecipe(Recipe recipe){
        selectedRecipe = recipe;

        int recipeId = selectedRecipe.getId();
        String recipeName = selectedRecipe.getName();

        updateTagList();

        recipeIdLabel.setText("Recipe ID: " + recipeId);
        recipeNameLabel.setText(recipeName);
    }

    /**
     * Called when adding or removing a tag from selected recipe and
     * when controller is initially loaded
     *
     * Updates tagListView accordingly
     *
     * (requirement 5.6.0)
     */
    public void updateTagList(){
        try {
            ArrayList<String> selectedRecipeTags = tagService.getAllTagsForRecipe(selectedRecipe);
            recipeTags.clear();
            recipeTags.addAll(selectedRecipeTags);
        } catch (TagException e) {
            e.printStackTrace();
        }
    }
}
