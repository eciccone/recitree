package edu.uis.recitree.dao;

import edu.uis.recitree.database.SQLiteConnection;
import edu.uis.recitree.model.Ingredient;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;

import java.sql.*;
import java.util.ArrayList;

public class SQLiteTagDAO implements TagDAO {

    private SQLiteConnection sqlite;

    public SQLiteTagDAO() {
        sqlite = new SQLiteConnection();
    }

    /**
     * Selects all the distinct tags from the database.
     *
     * (requirement 5.1.2)
     *
     * @return The ArrayList of tags
     */
    @Override
    public ArrayList<String> selectAllTags() {
        String sql = "SELECT DISTINCT tag_name FROM tag";

        try (Connection conn = sqlite.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            ArrayList<String> tags = new ArrayList<>();

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tag = rs.getString("tag_name");
                tags.add(tag);
            }

            return tags;
        } catch (SQLException e) {
            System.out.println("SQLiteTagDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Selects all the tags for a specific recipe from the database.
     *
     * (requirement 5.6.2)
     *
     * @param recipeId The id of the recipe to get the tags for
     * @return The ArrayList of tags
     */
    @Override
    public ArrayList<String> selectRecipeTags(int recipeId) {
        String sql = "SELECT tag_name FROM tag WHERE recipe_id = ?";

        try (Connection conn = sqlite.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ArrayList<String> tags = new ArrayList<>();

            stmt.setInt(1, recipeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tag = rs.getString("tag_name");
                tags.add(tag);
            }

            return tags;
        } catch (SQLException e) {
            System.out.println("SQLiteTagDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Selects all the recipes which belong to a specific tag from the database.
     *
     * (requirement 5.2.2)
     *
     * @param tag The tag that the recipes belong to
     * @return The ArrayList of recipes
     */
    @Override
    public ArrayList<Recipe> selectRecipesByTag(String tag) {
        String sql1 =
                "SELECT recipe.id, recipe.name, recipe.servings, recipe.instructions FROM recipe, tag " +
                "WHERE tag.recipe_id = recipe.id AND tag.tag_name = ?";

        String sql2 =
                "SELECT recipe_ingredient.id, recipe_ingredient.unit_type, recipe_ingredient.unit_amount, " +
                "Ingredient.id as ingredient_id, ingredient.name  FROM recipe_ingredient, ingredient WHERE " +
                "ingredient.id = recipe_ingredient.ingredient_id AND recipe_ingredient.recipe_id = ?";


        try (Connection conn = sqlite.connect();
             PreparedStatement stmt1 = conn.prepareStatement(sql1);
             PreparedStatement stmt2 = conn.prepareStatement(sql2)) {

            stmt1.setString(1, tag);
            ResultSet rs1 = stmt1.executeQuery();

            ArrayList<Recipe> recipes = new ArrayList<>();

            while (rs1.next()) {
                int recipeId = rs1.getInt("id");
                String recipeName = rs1.getString("name");
                double servings = rs1.getDouble("servings");
                String instructions = rs1.getString("instructions");

                stmt2.setInt(1, recipeId);
                ResultSet rs2 = stmt2.executeQuery();
                ArrayList<RecipeIngredient> ingredients = new ArrayList<>();

                while (rs2.next()) {
                    int recipeIngredientId = rs2.getInt("id");
                    String unitType = rs2.getString("unit_type");
                    double unitAmount = rs2.getDouble("unit_amount");
                    int ingredientId = rs2.getInt("ingredient_id");
                    String ingredientName = rs2.getString("name");

                    Ingredient ingredient = new Ingredient(ingredientId, ingredientName);
                    RecipeIngredient recipeIngredient = new RecipeIngredient(recipeIngredientId, ingredient, unitType, unitAmount);
                    ingredients.add(recipeIngredient);
                }
                Recipe recipe = new Recipe(recipeId, recipeName, servings, ingredients, instructions, false);
                recipes.add(recipe);
            }

            return recipes;

        } catch (SQLException e) {
            System.out.println("SQLiteTagDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Inserts a new tag for a specific recipe into the database.
     *
     * (requirement 5.4.2)
     *
     * @param tag The new tag
     * @param recipeId The id of the recipe it belongs to
     * @return True if the tag is successfully inserted, otherwise false
     */
    @Override
    public boolean insertRecipeTag(String tag, int recipeId) {
        String sql = "INSERT INTO tag (tag_name, recipe_id) VALUES (?, ?)";

        try (Connection conn = sqlite.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            sqlite.enableForeignKey(conn);

            stmt.setString(1, tag);
            stmt.setInt(2, recipeId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("creating recipe tag failed, no rows affected");
            }

            return true;
        } catch (SQLException e) {
            System.out.println("SQLiteTagDAO error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a tag for a specific recipe from the database.
     *
     * (requirement 5.5.2)
     *
     * @param tag The tag to be deleted
     * @param recipeId The id of the recipe to which it belongs
     * @return
     */
    @Override
    public boolean deleteRecipeTag(String tag, int recipeId) {
        String sql = "DELETE FROM tag WHERE tag_name = ? AND recipe_id = ?";

        try (Connection conn = sqlite.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tag);
            stmt.setInt(2, recipeId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("deleting recipe tag failed, no rows affected");
            }

            return true;

        } catch (SQLException e) {
            System.out.println("SQLiteTagDAO error: " + e.getMessage());
            return false;
        }
    }

}
