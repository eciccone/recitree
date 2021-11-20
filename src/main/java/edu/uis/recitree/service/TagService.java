package edu.uis.recitree.service;

import edu.uis.recitree.exception.TagException;
import edu.uis.recitree.model.Recipe;

import java.util.ArrayList;

public interface TagService {
    ArrayList<String> getAllTags() throws TagException;
    ArrayList<String> getAllTagsForRecipe(Recipe recipe) throws TagException;
    ArrayList<Recipe> getAllRecipesWithTag(String tag) throws TagException;
    void createRecipeTag(String tag, Recipe recipe) throws TagException;
    void removeRecipeTag(String tag, Recipe recipe) throws TagException;
}
