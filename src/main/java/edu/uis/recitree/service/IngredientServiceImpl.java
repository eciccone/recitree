package edu.uis.recitree.service;

import edu.uis.recitree.dao.IngredientDAO;
import edu.uis.recitree.dao.SQLiteIngredientDAO;
import edu.uis.recitree.exception.CreateIngredientException;
import edu.uis.recitree.exception.DeleteIngredientException;
import edu.uis.recitree.exception.InvalidIDException;
import edu.uis.recitree.exception.ReadAllIngredientsException;
import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

/**
 * Business logic for Ingredients in ReciTree
 *
 * @author Edward Ciccone
 */
public class IngredientServiceImpl implements IngredientService {

    private IngredientDAO ingredientDAO;

    public IngredientServiceImpl() {
        ingredientDAO = new SQLiteIngredientDAO();
    }

    /**
     * Gets all the inactive ingredients. If ingredients is null a ReadAllIngredientsException is thrown.
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
     * Gets all the ingredients. If ingredients is null a ReadAllIngredientsException is thrown.
     *
     * @return The list of ingredients
     * @throws ReadAllIngredientsException Thrown if there is a problem selecting ingredients from the database
     */
    @Override
    public ArrayList<Ingredient> readAllIngredients() throws ReadAllIngredientsException {
        ArrayList<Ingredient> ingredients = ingredientDAO.selectAllIngredients();

        if (ingredients == null) {
            throw new ReadAllIngredientsException("error selecting all the ingredients from the database");
        }

        return ingredients;
    }

    /**
     * Attempts to get an ingredient by name, if it fails a new ingredient is created instead. If a ingredient cannot
     * be selected or created in the database, a CreateIngredientException is thrown.
     *
     * (requirement 3.3.0)
     *
     * @param name The name of the ingredient to select or create
     * @return The selected or created ingredient
     * @exception CreateIngredientException Thrown if there is a problem inserting a new ingredient into the database
     */
    @Override
    public Ingredient getOrCreateIngredient(String name) throws CreateIngredientException {
        Ingredient ingredient = ingredientDAO.selectIngredientByName(name);

        // if the ingredient does not already exist, create it
        if (ingredient == null) {
            Ingredient temp = new Ingredient(name);
            ingredient = ingredientDAO.insertIngredient(temp);

            // if there was a problem creating ingredient, return null
            if (ingredient == null) {
                throw new CreateIngredientException("problem insert new ingredient into the database");
            }
        }

        return ingredient;
    }

    /**
     * Deletes an ingredient by id. If the id is less than 1 a InvalidIDException is thrown. If the ingredient cannot
     * be deleted from the database, a DeleteIngredientException is thrown.
     *
     * (requirement 3.2.1)
     *
     * @param id The id of the ingredient to be deleted
     * @return If the ingredient is deleted successfully returns true, otherwise false
     * @exception InvalidIDException Thrown if an id is less than or equal to zero, or a ingredient cannot be found with the id
     * @exception DeleteIngredientException Thrown if there is a problem deleting the ingredient from the database
     */
    @Override
    public boolean deleteIngredient(int id) throws InvalidIDException, DeleteIngredientException {
        // if the id is invalid the operation cannot be completed
        if (id <= 0) {
            throw new InvalidIDException("id must be greater than zero");
        }

        // if the ingredient is null something went wrong
        Ingredient ingredient = ingredientDAO.selectIngredientById(id);
        if (ingredient == null) {
            throw new InvalidIDException("ingredient not found with id: " + id);
        }

        boolean success = ingredientDAO.deleteIngredient(id);

        if (!success) {
            throw new DeleteIngredientException("error while deleting ingredient from the database");
        }

        return true;
    }
}
