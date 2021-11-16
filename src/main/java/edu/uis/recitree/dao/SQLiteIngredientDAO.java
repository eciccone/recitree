package edu.uis.recitree.dao;

import edu.uis.recitree.database.SQLiteConnection;
import edu.uis.recitree.model.Ingredient;

import java.sql.*;
import java.util.ArrayList;

/**
 * Handles all queries for ingredients.
 *
 * @author Edward Ciccone
 */
public class SQLiteIngredientDAO implements IngredientDAO {

    private SQLiteConnection sqlite;

    public SQLiteIngredientDAO() {
        sqlite = new SQLiteConnection();
    }

    /**
     * Inserts an ingredient into the database.
     *
     * (requirement 3.3.1)
     *
     * @param ingredient The ingredient to be inserted into the database
     * @return The ingredient with an id that was inserted into the database, null if something went wrong
     */
    @Override
    public Ingredient insertIngredient(Ingredient ingredient) {
        String sql = "INSERT INTO ingredient (name) VALUES (?)";

        try (Connection conn = sqlite.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ingredient.getName());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("creating ingredient failed, no rows affected");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ingredient.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("creating ingredient failed, no ID obtained");
                }
            }

            return ingredient;

        } catch (SQLException e) {
            System.out.println("SQLiteIngredientDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Select all the ingredients from the database
     *
     * @return An ArrayList of all the ingredients, null if something went wrong
     */
    @Override
    public ArrayList<Ingredient> selectAllIngredients() {
        String sql = "SELECT ingredient.id, ingredient.name FROM ingredient";

        try (Connection conn = sqlite.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ArrayList<Ingredient> ingredients = new ArrayList<>();

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                ingredients.add(new Ingredient(id, name));
            }

            return ingredients;
        } catch (SQLException e) {
            System.out.println("SQLiteIngredientDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Selects an ingredient by its name in the database.
     *
     * @param name The name of the ingredient to select
     * @return The ingredient, null if a problem occured or an ingredient did not exist with the name
     */
    @Override
    public Ingredient selectIngredientByName(String name) {
        String sql = "SELECT id, name FROM ingredient WHERE name = ?";

        try (Connection conn = sqlite.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            Ingredient ingredient = null;
            if (rs.next()) {
                int id = rs.getInt("id");
                String ingredientName = rs.getString("name");
                ingredient = new Ingredient(id, ingredientName);
            }

            return ingredient;
        } catch (SQLException e) {
            System.out.println("SQLiteIngredientDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Selects an ingredient by id in the database.
     *
     * @param id The id of the ingredient to select
     * @return The ingredient, null if a problem occured or an ingredient did not exist with the id
     */
    @Override
    public Ingredient selectIngredientById(int id) {
        String sql = "SELECT id, name FROM ingredient WHERE id = ?";

        try (Connection conn = sqlite.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            Ingredient ingredient = null;
            if (rs.next()) {
                String ingredientName = rs.getString("name");
                ingredient = new Ingredient(id, ingredientName);
            }

            return ingredient;
        } catch (SQLException e) {
            System.out.println("SQLiteIngredientDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Selects all the inactive ingredients from the database.
     *
     * (requirement 3.1.2)
     *
     * @return An ArrayList of all the inactive ingredients, null if something went wrong
     */
    @Override
    public ArrayList<Ingredient> selectAllUnusedIngredients() {
        String sql = "SELECT ingredient.id, ingredient.name FROM ingredient LEFT JOIN recipe_ingredient " +
                "ON ingredient.id = recipe_ingredient.ingredient_id WHERE recipe_ingredient.ingredient_id IS NULL";

        try (Connection conn = sqlite.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ArrayList<Ingredient> ingredients = new ArrayList<>();

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                ingredients.add(new Ingredient(id, name));
            }

            return ingredients;
        } catch (SQLException e) {
            System.out.println("SQLiteIngredientDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes an ingredient from the database.
     *
     * @param id The id of the ingredient being deleted
     * @return True if the ingredient is deleted successfully from the database, otherwise false
     */
    @Override
    public boolean deleteIngredient(int id) {
        String sql = "DELETE FROM ingredient WHERE id = ?";

        try (Connection conn = sqlite.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("deleting ingredient failed, no rows affected");
            }

            return true;
        } catch (SQLException e) {
            System.out.println("SQLiteIngredientDAO error: " + e.getMessage());
            return false;
        }
    }
}
