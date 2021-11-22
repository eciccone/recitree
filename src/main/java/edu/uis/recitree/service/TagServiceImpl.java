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

    /**
     * Gets a ArrayList of all the distinct tags.
     *
     * (requirement 5.1.1)
     *
     * @return The ArrayList of all the distinct tags
     * @throws TagException Thrown if there is a problem getting the tags from the database
     */
    @Override
    public ArrayList<String> getAllTags() throws TagException {
        ArrayList<String> tags = tagDAO.selectAllTags();

        if (tags == null) {
            throw new TagException("error fetching list of tags");
        }

        return tags;
    }

    /**
     * Gets a ArrayList of all the tags for a specific recipe
     *
     * (requirement 5.6.1)
     *
     * @param recipe The recipe for which the tags belong to
     * @return The ArrayList of tags
     * @throws TagException Thrown if there is a problem getting the tags from the database
     */
    @Override
    public ArrayList<String> getAllTagsForRecipe(Recipe recipe) throws TagException {
        ArrayList<String> tags = tagDAO.selectRecipeTags(recipe.getId());

        if (tags == null) {
            throw new TagException("error fetching list of tags for recipe");
        }

        return tags;
    }

    /**
     * Gets a ArrayList of recipes which all belong to a specific tag.
     *
     * (requirement 5.2.1)
     *
     * @param tag The tag that the recipes belong to
     * @return The ArrayList of all the recipes
     * @throws TagException Thrown if there is a problem getting the recipes from the database
     */
    @Override
    public ArrayList<Recipe> getAllRecipesWithTag(String tag) throws TagException {
        ArrayList<Recipe> recipes = tagDAO.selectRecipesByTag(tag);

        if (recipes == null) {
            throw new TagException("error fetching list of recipes by tag");
        }

        return recipes;
    }

    /**
     * Creates a new tag for a specific recipe.
     *
     * (requirement 5.4.1)
     *
     * @param tag The new tag
     * @param recipe The recipe to tag
     * @throws TagException Thrown if an empty string is passed as the tag or if there is a problem inserting the tag into the database
     */
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

    /**
     * Deletes a tag for a specific recipe.
     *
     * (requirement 5.5.1)
     *
     * @param tag The tag to be deleted
     * @param recipe The recipe to which the tag belongs
     * @throws TagException Thrown if an empty string is passed as the tag or if there is a problem deleting the tag from the database
     */
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
