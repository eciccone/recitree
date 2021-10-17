package edu.uis.recitree.dao;

import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;

import java.util.ArrayList;

public interface RecipeDAO {
    // create
    Recipe insertRecipe(Recipe recipe);

    // read
    Recipe selectRecipe(int id);

    // read all
    ArrayList<Recipe> selectAllRecipes();

    // update
    Recipe updateRecipe(Recipe recipe);

    // delete
    boolean deleteRecipe(int id);
}
