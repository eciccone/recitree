package edu.uis.recitree.dao;

import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

public interface IngredientDAO {
    // Create
    Ingredient insertIngredient(String name);

    // Read
    ArrayList<Ingredient> selectAllIngredients();

    // Read All
    Ingredient selectIngredientByName(String name);

    // Delete
    boolean deleteIngredient(int id);
}
