package edu.uis.recitree.service;

import edu.uis.recitree.dao.IngredientDAO;
import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

public class IngredientServiceImpl implements IngredientService {

    private IngredientDAO ingredientDAO;

    public IngredientServiceImpl() {

    }

    @Override
    public ArrayList<Ingredient> readAllUnusedIngredients() {
        return ingredientDAO.selectAllUnusedIngredients();
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

    @Override
    public boolean deleteIngredient(int id) {
        // if the id is invalid the operation cannot be completed
        if (id <= 0) {
            return false;
        }

        return ingredientDAO.deleteIngredient(id);
    }
}
