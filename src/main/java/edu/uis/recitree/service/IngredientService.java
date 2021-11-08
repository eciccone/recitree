package edu.uis.recitree.service;

import edu.uis.recitree.exception.CreateIngredientException;
import edu.uis.recitree.exception.DeleteIngredientException;
import edu.uis.recitree.exception.InvalidIDException;
import edu.uis.recitree.exception.ReadAllIngredientsException;
import edu.uis.recitree.model.Ingredient;

import java.util.ArrayList;

public interface IngredientService {
    ArrayList<Ingredient> readAllUnusedIngredients() throws ReadAllIngredientsException;
    ArrayList<Ingredient> readAllIngredients() throws ReadAllIngredientsException;
    Ingredient getOrCreateIngredient(String name) throws CreateIngredientException;
    boolean deleteIngredient(int id) throws InvalidIDException, DeleteIngredientException;
}
