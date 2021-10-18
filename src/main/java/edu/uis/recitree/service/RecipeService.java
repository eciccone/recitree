package edu.uis.recitree.service;

import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;

import java.util.ArrayList;

public interface RecipeService {
    Recipe createRecipe(String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions);
    Recipe readRecipe(int id);
    ArrayList<Recipe> readAllRecipes();
    ArrayList<Recipe> readAllFavoriteRecipes();
    ArrayList<Recipe> searchRecipesByName(String text);
    ArrayList<Recipe> searchRecipesByIngredients(String text);
    Recipe updateRecipe(int id, String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions);
    boolean deleteRecipe(int id);
    boolean toggleFavoriteStatus(int id);
}
