package edu.uis.recitree.dao;

import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

public interface IngredientDAO {
    // Create
    Ingredient insertIngredient(Ingredient ingredient);

    // Read
    ArrayList<Ingredient> selectAllIngredients();

    // Read All
    Ingredient selectIngredientByName(Ingredient ingredient);

    // Delete
    boolean deleteIngredient(int id);
}
