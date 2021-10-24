package edu.uis.recitree.service;

import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

public interface IngredientService {
    ArrayList<Ingredient> readAllUnusedIngredients();
    Ingredient getOrCreateIngredient(String name);
    boolean deleteIngredient(int id);
}
