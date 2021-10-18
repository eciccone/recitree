package edu.uis.recitree.dao;

import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;

import java.util.ArrayList;

public interface RecipeDAO {
    Recipe insertRecipe(Recipe recipe);
    Recipe selectRecipe(int id);
    ArrayList<Recipe> selectAllRecipes();
    ArrayList<Recipe> selectAllFavoriteRecipes();
    Recipe updateRecipe(Recipe recipe);
    boolean deleteRecipe(int id);
}
