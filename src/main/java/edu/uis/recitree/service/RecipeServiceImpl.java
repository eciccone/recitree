package edu.uis.recitree.service;

import edu.uis.recitree.dao.IngredientDAO;
import edu.uis.recitree.dao.RecipeDAO;
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
     * Reuses previously created ingredients in the database for each ingredient in the recipe being created. It then
     * constructs a recipe and inserts it into the database.
     *
     * (requirement 4.3.1)
     *
     * @param name The name of the recipe being created
     * @param servings The amount of servings for the recipe being created
     * @param ingredients The ingredients of the recipe being created
     * @param instructions The instructions of the recipe being created
     * @return If the recipe was created successfully returns the recipe, otherwise returns null
     */
    @Override
    public Recipe createRecipe(String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions) {

        for (RecipeIngredient recIng : ingredients) {
            Ingredient ing = ingredientService.getOrCreateIngredient(recIng.getIngredient().getName());

            // if ingredient could not be selected by name or created the operation cannot be completed
            if (ing == null) {
                return null;
            }

            recIng.setIngredient(ing);
        }

        Recipe recipe = new Recipe(name, servings, ingredients, instructions);

        return recipeDAO.insertRecipe(recipe);
    }

    @Override
    public Recipe readRecipe(int id) {
        if (id <= 0)
            return null;

        return recipeDAO.selectRecipe(id);
    }

    @Override
    public ArrayList<Recipe> readAllRecipes() {
        return recipeDAO.selectAllRecipes();
    }

    @Override
    public ArrayList<Recipe> readAllFavoriteRecipes() {
        return recipeDAO.selectAllFavoriteRecipes();
    }

    @Override
    public ArrayList<Recipe> searchRecipesByName(String text) {

        text = text.trim();

        // search with no text, so just get all recipes
        if (text == null || text.equals("")) {
            return recipeDAO.selectAllRecipes();
        }

        return recipeDAO.selectAllRecipesWhereNameContains(text);
    }

    @Override
    public Recipe updateRecipe(int id, String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions, boolean favorite) {

        for (RecipeIngredient recIng : ingredients) {
            Ingredient ing = ingredientDAO.selectIngredientByName(recIng.getIngredient().getName());

            if (ing == null) {
                ing = ingredientDAO.insertIngredient(recIng.getIngredient());
            }

            recIng.setIngredient(ing);
        }

        Recipe recipe = new Recipe(id, name, servings, ingredients, instructions, favorite);
        return recipeDAO.updateRecipe(recipe);
    }

    @Override
    public boolean deleteRecipe(int id) {
        if (id <= 0)
            return false;

        return recipeDAO.deleteRecipe(id);
    }

    @Override
    public boolean toggleFavoriteStatus(int id) {
        // id is invalid if its 0 or less
        if (id <= 0) {
            return false;
        }

        // make sure recipe exists
        Recipe recipe = recipeDAO.selectRecipe(id);
        if (recipe == null) {
            return false;
        }

        // toggle favorite status and return if operation was successful
        boolean success;
        if (recipe.isFavorite()) {
            success = recipeDAO.deleteRecipeFavorite(recipe.getId());
        } else {
            success = recipeDAO.insertRecipeFavorite(recipe.getId());
        }

        return success;
    }
}