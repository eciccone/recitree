package edu.uis.recitree.dao;

import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

public interface IngredientDAO {
    // Create
    Ingredient insertIngredient(Ingredient ingredient);

    // Read all
    ArrayList<Ingredient> selectAllIngredients();

    // Read
    Ingredient selectIngredientByName(Ingredient ingredient);

    // read all unused ingredients
    ArrayList<Ingredient> selectAllUnusedIngredients();

    // Delete
    boolean deleteIngredient(int id);
}
