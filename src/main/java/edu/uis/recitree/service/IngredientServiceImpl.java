package edu.uis.recitree.service;

import edu.uis.recitree.dao.IngredientDAO;
import edu.uis.recitree.exception.ReadAllIngredientsException;
import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

public class IngredientServiceImpl implements IngredientService {

    private IngredientDAO ingredientDAO;

    public IngredientServiceImpl() {

    }

    /**
     * Gets all the inactive ingredients.
     *
     * (requirement 3.1.1)
     *
     * @return The list of inactive ingredients
     * @exception ReadAllIngredientsException Thrown if there is a problem selecting ingredients from the database
     */
    @Override
    public ArrayList<Ingredient> readAllUnusedIngredients() throws ReadAllIngredientsException {
        ArrayList<Ingredient> ingredients = ingredientDAO.selectAllUnusedIngredients();

        if (ingredients == null) {
            throw new ReadAllIngredientsException("error selecting all the unused ingredients from the database");
        }

        return ingredients;
    }

    /**
     * Attempts to get an ingredient by name, if it fails a new ingredient is created instead
     *
     * (requirement 3.3.0)
     *
     * @param name The name of the ingredient to select or create
     * @return The selected or created ingredient
     */
    @Override
    public Ingredient getOrCreateIngredient(String name) {
        Ingredient ingredient = ingredientDAO.selectIngredientByName(name);

        // if the ingredient does not already exist, create it
        if (ingredient == null) {
            Ingredient temp = new Ingredient(name);
            ingredient = ingredientDAO.insertIngredient(temp);

            // if there was a problem creating ingredient, return null
            if (ingredient == null) {
                return null;
            }
        }

        return ingredient;
    }

    /**
     * Deletes an ingredient by id
     *
     * (requirement 3.2.1)
     *
     * @param id The id of the ingredient to be deleted
     * @return If the ingredient is deleted successfully returns true, otherwise false
     */
    @Override
    public boolean deleteIngredient(int id) {
        // if the id is invalid the operation cannot be completed
        if (id <= 0) {
            return false;
        }

        // if the ingredient is null something went wrong
        Ingredient ingredient = ingredientDAO.selectIngredientById(id);
        if (ingredient == null) {
            return false;
        }

        return ingredientDAO.deleteIngredient(id);
    }
}
