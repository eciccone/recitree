package edu.uis.recitree.service;

import edu.uis.recitree.exception.ReadAllRecipesException;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;

import java.util.ArrayList;

public interface RecipeService {
    Recipe createRecipe(String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions);
    Recipe readRecipe(int id);
    ArrayList<Recipe> readAllRecipes() throws ReadAllRecipesException;
    ArrayList<Recipe> readAllFavoriteRecipes();
    ArrayList<Recipe> searchRecipesByName(String text);
    Recipe updateRecipe(int id, String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions, boolean favorite);
    boolean deleteRecipe(int id);
    boolean toggleFavoriteStatus(int id);
}
