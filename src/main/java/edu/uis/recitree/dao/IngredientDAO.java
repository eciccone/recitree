package edu.uis.recitree.dao;

import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

public interface IngredientDAO {
    Ingredient insertIngredient(Ingredient ingredient);
    ArrayList<Ingredient> selectAllIngredients();
    Ingredient selectIngredientByName(String name);
    ArrayList<Ingredient> selectAllUnusedIngredients();
    boolean deleteIngredient(int id);
}
