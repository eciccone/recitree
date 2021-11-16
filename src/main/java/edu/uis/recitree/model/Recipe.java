package edu.uis.recitree.model;

import java.util.ArrayList;

/**
 * Encapsulates a recipe.
 *
 * @author Edward Ciccone
 */
public class Recipe {

    private int id;
    private String name;
    private double servings;
    private ArrayList<RecipeIngredient> ingredients;
    private String instructions;
    private boolean favorite;

    public Recipe(String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions) {
        this(0, name, servings, ingredients, instructions, false);
    }

    public Recipe(int id, String name, double servings, ArrayList<RecipeIngredient> ingredients, String instructions, boolean favorite) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getServings() {
        return servings;
    }

    public void setServings(double servings) {
        this.servings = servings;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return name;
    }
}
