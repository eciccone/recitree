package edu.uis.recitree.dao;

import edu.uis.recitree.model.Recipe;

import java.util.ArrayList;

public interface TagDAO {
    ArrayList<String> selectAllTags();
    ArrayList<String> selectRecipeTags(int recipeId);
    ArrayList<Recipe> selectRecipesByTag(String tag);
    boolean insertRecipeTag(String tag, int recipeId);
    boolean deleteRecipeTag(String tag, int recipeId);
}
