package edu.uis.recitree.service;

import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

public interface IngredientService {
    ArrayList<Ingredient> readAllUnusedIngredients();
    boolean deleteIngredient(int id);
}
