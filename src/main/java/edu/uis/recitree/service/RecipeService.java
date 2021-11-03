package edu.uis.recitree.service;

import edu.uis.recitree.exception.*;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;

import java.util.ArrayList;

public interface RecipeService {
    Recipe createRecipe(String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions) throws CreateRecipeException;
    Recipe readRecipe(int id);
    ArrayList<Recipe> readAllRecipes() throws ReadAllRecipesException;
    ArrayList<Recipe> readAllFavoriteRecipes() throws ReadAllFavoritesException;
    ArrayList<Recipe> searchRecipesByName(String text) throws SearchRecipeException;
    Recipe updateRecipe(int id, String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions, boolean favorite) throws UpdateRecipeException;
    boolean deleteRecipe(int id) throws InvalidIDException, DeleteRecipeException;
    boolean toggleFavoriteStatus(int id) throws InvalidIDException, ReadRecipeException, ToggleFavoriteStatusException;
}
