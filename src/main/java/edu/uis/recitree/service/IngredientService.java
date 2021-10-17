package edu.uis.recitree.service;

import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

public interface IngredientService {
    Ingredient createIngredient(String name);
    Ingredient readIngredient(String name);
    ArrayList<Ingredient> readAllUnusedIngredients();
    boolean deleteIngredient(int id);
}
