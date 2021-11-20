package edu.uis.recitree.service;

import edu.uis.recitree.dao.SQLiteTagDAO;
import edu.uis.recitree.dao.TagDAO;
import edu.uis.recitree.exception.TagException;
import edu.uis.recitree.model.Recipe;

import java.util.ArrayList;

public class TagServiceImpl implements TagService {

    private TagDAO tagDAO;

    public TagServiceImpl() {
        tagDAO = new SQLiteTagDAO();
    }

    @Override
    public ArrayList<String> getAllTags() throws TagException {
        ArrayList<String> tags = tagDAO.selectAllTags();

        if (tags == null) {
            throw new TagException("error fetching list of tags");
        }

        return tags;
    }

    @Override
    public ArrayList<String> getAllTagsForRecipe(Recipe recipe) throws TagException {
        ArrayList<String> tags = tagDAO.selectRecipeTags(recipe.getId());

        if (tags == null) {
            throw new TagException("error fetching list of tags for recipe");
        }

        return tags;
    }

    @Override
    public ArrayList<Recipe> getAllRecipesWithTag(String tag) throws TagException {
        ArrayList<Recipe> recipes = tagDAO.selectRecipesByTag(tag);

        if (recipes == null) {
            throw new TagException("error fetching list of recipes by tag");
        }

        return recipes;
    }

    @Override
    public void createRecipeTag(String tag, Recipe recipe) throws TagException {
        // check if an empty tag was passed as a parameter
        if (tag == null || tag.equals("")) {
            throw new TagException("error creating tag, tag must contain text");
        }

        boolean success = tagDAO.insertRecipeTag(tag, recipe.getId());

        if (!success) {
            throw new TagException("error creating recipe tag, problem inserting row");
        }
    }

    @Override
    public void removeRecipeTag(String tag, Recipe recipe) throws TagException {
        // check if an empty tag was passed as a parameter
        if (tag == null || tag.equals("")) {
            throw new TagException("error deleting tag, tag must contain text");
        }

        boolean success = tagDAO.deleteRecipeTag(tag, recipe.getId());

        if (!success) {
            throw new TagException("error deleting recipe tag, problem removing row");
        }
    }
}
