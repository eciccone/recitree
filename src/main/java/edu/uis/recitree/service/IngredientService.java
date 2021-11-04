package edu.uis.recitree.service;

import edu.uis.recitree.exception.DeleteIngredientException;
import edu.uis.recitree.exception.InvalidIDException;
import edu.uis.recitree.exception.ReadAllIngredientsException;
import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

public interface IngredientService {
    ArrayList<Ingredient> readAllUnusedIngredients() throws ReadAllIngredientsException;
    Ingredient getOrCreateIngredient(String name);
    boolean deleteIngredient(int id) throws InvalidIDException, DeleteIngredientException;
}
