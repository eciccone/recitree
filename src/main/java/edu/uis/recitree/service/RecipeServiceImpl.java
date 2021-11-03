package edu.uis.recitree.service;

import edu.uis.recitree.dao.IngredientDAO;
import edu.uis.recitree.dao.RecipeDAO;
import edu.uis.recitree.exception.*;
import edu.uis.recitree.model.Ingredient;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;

import java.util.ArrayList;

public class RecipeServiceImpl implements RecipeService {

    private IngredientDAO ingredientDAO;
    private RecipeDAO recipeDAO;
    private IngredientService ingredientService;

    public RecipeServiceImpl() {
        ingredientService = new IngredientServiceImpl();
    }

    /**
     * Creates a recipes and inserts it into the database. Before constructing the recipe it reuses previously created
     * ingredients in the database for each ingredient in the recipe being created.
     *
     * (requirement 4.3.1)
     *
     * @param name The name of the recipe being created
     * @param servings The amount of servings for the recipe being created
     * @param ingredients The ingredients of the recipe being created
     * @param instructions The instructions of the recipe being created
     * @return The recipe that was created
     * @throws CreateRecipeException Thrown if there was a problem creating the recipe
     */
    @Override
    public Recipe createRecipe(String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions) throws CreateRecipeException {

        for (RecipeIngredient recIng : ingredients) {
            Ingredient ing = ingredientService.getOrCreateIngredient(recIng.getIngredient().getName());

            // if ingredient could not be selected by name or created the operation cannot be completed
            if (ing == null) {
                throw new CreateRecipeException("error reusing ingredients");
            }

            recIng.setIngredient(ing);
        }

        Recipe recipe = recipeDAO.insertRecipe(new Recipe(name, servings, ingredients, instructions));

        if (recipe == null) {
            throw new CreateRecipeException("error inserting recipe");
        }

        return recipe;
    }

    @Override
    public Recipe readRecipe(int id) {
        if (id <= 0)
            return null;

        return recipeDAO.selectRecipe(id);
    }

    /**
     * Reads all the user created recipes.
     *
     * (requirement 4.1.1)
     *
     * @return An ArrayList of all the user created recipes
     * @throws ReadAllRecipesException Thrown if there is a problem selecting all the user created recipes from the database
     */
    @Override
    public ArrayList<Recipe> readAllRecipes() throws ReadAllRecipesException {
        ArrayList<Recipe> recipes = recipeDAO.selectAllRecipes();

        if (recipes == null) {
            throw new ReadAllRecipesException("error selecting all recipes from the database");
        }

        return recipes;
    }

    /**
     * Read all the favorited recipes.
     *
     * (requirement 4.7.1)
     *
     * @return An ArrayList of all the favorited recipes
     * @throws ReadAllFavoritesException Thrown if there is a problem selecting all the favorited recipes from the database
     */
    @Override
    public ArrayList<Recipe> readAllFavoriteRecipes() throws ReadAllFavoritesException {
        ArrayList<Recipe> recipes = recipeDAO.selectAllFavoriteRecipes();

        if (recipes == null) {
            throw new ReadAllFavoritesException("error selecting all favorites from the database");
        }

        return recipes;
    }

    /**
     * Searches for a recipe by name given a specific text.
     *
     * (requirement 4.8.1)
     *
     * @param text The search text
     * @return An ArrayList of recipes that contain the search text in their name
     * @throws SearchRecipeException Thrown if there is a problem selecting the recipes from the database
     */
    @Override
    public ArrayList<Recipe> searchRecipesByName(String text) throws SearchRecipeException {

        text = text.trim();

        ArrayList<Recipe> recipes;

        // search with no text, so just get all recipes
        if (text == null || text.equals("")) {
            recipes = recipeDAO.selectAllRecipes();
        } else {
            recipes = recipeDAO.selectAllRecipesWhereNameContains(text);
        }

        if (recipes == null) {
            throw new SearchRecipeException("error searching recipes with text: " + text);
        }

        return recipes;
    }

    /**
     * Updates a recipe and updates it in the database. Before reconstructing the recipe it reuses previously created
     * ingredients in the database for each ingredient in the recipe being updated.
     *
     * (requirement 4.5.1)
     *
     * @param id The id of the recipe being updated
     * @param name The name of the recipe
     * @param servings The recipe servings
     * @param ingredients The recipe ingredients
     * @param instructions The recipe instructions
     * @param favorite The recipe's favorite status
     * @return The recipe that was updated
     * @throws UpdateRecipeException Thrown if there was a problem updating the recipe
     */
    @Override
    public Recipe updateRecipe(int id, String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions, boolean favorite) throws UpdateRecipeException {

        for (RecipeIngredient recIng : ingredients) {
            Ingredient ing = ingredientService.getOrCreateIngredient(recIng.getIngredient().getName());

            if (ing == null) {
                throw new UpdateRecipeException("error reusing ingredients");
            }

            recIng.setIngredient(ing);
        }
        Recipe recipe = recipeDAO.updateRecipe(new Recipe(id, name, servings, ingredients, instructions, favorite));

        if (recipe == null) {
            throw new UpdateRecipeException("error updating recipe");
        }

        return recipe;
    }

    /**
     * Deletes a recipe by id.
     *
     * (requirement 4.4.1)
     *
     * @param id The id of the recipe to be deleted
     * @return Always returns true
     * @throws InvalidIDException Thrown if an id is 0 or less
     * @throws DeleteRecipeException Thrown if a recipe could not be deleted with the given id
     */
    @Override
    public boolean deleteRecipe(int id) throws InvalidIDException, DeleteRecipeException {
        // id is invalid if its 0 or less
        if (id <= 0) {
            throw new InvalidIDException("id must be greater than zero");
        }

        // attempt to delete recipe from database, if unsuccessful throw a error
        if (!recipeDAO.deleteRecipe(id)) {
            throw new DeleteRecipeException("error deleting recipe from database");
        }

        return true;
    }

    /**
     * Toggles a recipes favorite status.
     *
     * (requirement 4.6.1)
     *
     * @param id The id of the recipes favorite status to be toggled
     * @return Always returns true
     * @throws InvalidIDException Thrown if an id is 0 or less
     * @throws ReadRecipeException Thrown if a recipe could not be selected with the given id
     * @throws ToggleFavoriteStatusException Thrown if a recipes favorite status could not be updated
     */
    @Override
    public boolean toggleFavoriteStatus(int id) throws InvalidIDException, ReadRecipeException, ToggleFavoriteStatusException {
        // id is invalid if its 0 or less
        if (id <= 0) {
            throw new InvalidIDException("id must be greater than zero");
        }

        // make sure recipe exists
        Recipe recipe = recipeDAO.selectRecipe(id);
        if (recipe == null) {
            throw new ReadRecipeException("error selecting recipe from the database");
        }

        // toggle favorite status and return if operation was successful
        boolean success;
        if (recipe.isFavorite()) {
            success = recipeDAO.deleteRecipeFavorite(recipe.getId());
        } else {
            success = recipeDAO.insertRecipeFavorite(recipe.getId());
        }

        if (!success) {
            throw new ToggleFavoriteStatusException("error updating favorite status in the database");
        }

        return true;
    }
}
