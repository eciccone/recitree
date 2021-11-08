package edu.uis.recitree.dao;

import edu.uis.recitree.database.SQLiteConnection;
import edu.uis.recitree.model.Ingredient;
import edu.uis.recitree.model.Recipe;
import edu.uis.recitree.model.RecipeIngredient;

import java.sql.*;
import java.util.ArrayList;

public class SQLiteRecipeDAO implements RecipeDAO {

    private SQLiteConnection sqlite;

    public SQLiteRecipeDAO() {
        sqlite = new SQLiteConnection();
    }

    /**
     * Inserts a recipe and its ingredients into the database.
     *
     * (requirement 4.3.2)
     *
     * @param recipe The recipe being inserted into the database
     * @return The recipe with a set id that was inserted into the database, null if a problem occured
     */
    @Override
    public Recipe insertRecipe(Recipe recipe) {
        String sql1 = "INSERT INTO recipe (name, servings, instructions) VALUES (?, ?, ?)";
        String sql2 = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_type, unit_amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = sqlite.connect();
             PreparedStatement stmt1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmt2 = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS)) {

            stmt1.setString(1, recipe.getName());
            stmt1.setDouble(2, recipe.getServings());
            stmt1.setString(3, recipe.getInstructions());

            int affectedRows = stmt1.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("creating recipe failed, no rows affected");
            }

            try (ResultSet generatedKeys = stmt1.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    recipe.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("creating recipe failed, no ID obtained");
                }
            }


            for (RecipeIngredient ingredient : recipe.getIngredients()) {
                stmt2.setInt(1, recipe.getId());
                stmt2.setInt(2, ingredient.getIngredient().getId());
                stmt2.setString(3, ingredient.getUnitType());
                stmt2.setDouble(4, ingredient.getUnitAmount());

                affectedRows = stmt2.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("creating recipe_ingredient failed, no rows affected");
                }

                try (ResultSet generatedKeys = stmt2.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ingredient.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("creating recipe_ingredient failed, no ID obtained");
                    }
                }
            }

            return recipe;
        } catch (SQLException e) {
            System.out.println("SQLiteRecipeDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Selects a recipe by id from the database.
     *
     * @param id The id of the recipe to be selected
     * @return The recipe, false if a problem occured selecting recipe
     */
    @Override
    public Recipe selectRecipe(int id) {
        String sql1 =
                "SELECT id, name, servings, instructions, " +
                        "(SELECT EXISTS (SELECT 1 FROM favorite WHERE favorite.recipe_id = recipe.id)) as favorite " +
                        "FROM recipe WHERE id = ?";

        String sql2 =
                "SELECT recipe_ingredient.id, recipe_ingredient.unit_type, recipe_ingredient.unit_amount, " +
                        "Ingredient.id as ingredient_id, ingredient.name  FROM recipe_ingredient, ingredient WHERE " +
                        "ingredient.id = recipe_ingredient.ingredient_id AND recipe_ingredient.recipe_id = ?";

        try(Connection conn = sqlite.connect();
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2)) {

            stmt1.setInt(1, id);
            ResultSet rs1 = stmt1.executeQuery();

            Recipe recipe = null;

           if (rs1.next()) {
                int recipeId = rs1.getInt("id");
                String recipeName = rs1.getString("name");
                double recipeServings = rs1.getDouble("servings");
                String instructions = rs1.getString("instructions");
                boolean favorite = rs1.getBoolean("favorite");

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

                recipe = new Recipe(recipeId, recipeName, recipeServings, ingredients, instructions, favorite);

            }

            return recipe;

        } catch (SQLException e) {
            System.out.println("SQLiteRecipeDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Selects all the recipes from the database.
     *
     * (requirement 4.1.2)
     *
     * @return An ArrayList of all the recipes in the database, null if something went wrong
     */
    @Override
    public ArrayList<Recipe> selectAllRecipes() {
        String sql1 =
                "SELECT id, name, servings, instructions, " +
                "(SELECT EXISTS (SELECT 1 FROM favorite WHERE favorite.recipe_id = recipe.id)) as favorite " +
                "FROM recipe";

        String sql2 =
                "SELECT recipe_ingredient.id, recipe_ingredient.unit_type, recipe_ingredient.unit_amount, " +
                "Ingredient.id as ingredient_id, ingredient.name  FROM recipe_ingredient, ingredient WHERE " +
                "ingredient.id = recipe_ingredient.ingredient_id AND recipe_ingredient.recipe_id = ?";

        try(Connection conn = sqlite.connect();
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2)) {

            ResultSet rs1 = stmt1.executeQuery();
            ArrayList<Recipe> recipes = new ArrayList<>();

            while (rs1.next()) {
                int recipeId = rs1.getInt("id");
                String recipeName = rs1.getString("name");
                double recipeServings = rs1.getDouble("servings");
                String instructions = rs1.getString("instructions");
                boolean favorite = rs1.getBoolean("favorite");

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

                Recipe recipe = new Recipe(recipeId, recipeName, recipeServings, ingredients, instructions, favorite);
                recipes.add(recipe);
            }

            return recipes;

        } catch (SQLException e) {
            System.out.println("SQLiteRecipeDAO error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Recipe> selectAllRecipesWhereNameContains(String text) {
        return null;
    }

    /**
     * Selects all the favorited recipes from the database.
     *
     * (requirement 4.7.1)
     *
     * @return An ArrayList of all the favorited recipes, null if a problem occured while getting all the favorite recipes
     */
    @Override
    public ArrayList<Recipe> selectAllFavoriteRecipes() {
        String sql1 =
                "SELECT recipe.id, recipe.name, recipe.servings, recipe.instructions FROM recipe, favorite " +
                        "WHERE favorite.recipe_id = recipe.id";

        String sql2 =
                "SELECT recipe_ingredient.id, recipe_ingredient.unit_type, recipe_ingredient.unit_amount, " +
                        "Ingredient.id as ingredient_id, ingredient.name  FROM recipe_ingredient, ingredient WHERE " +
                        "ingredient.id = recipe_ingredient.ingredient_id AND recipe_ingredient.recipe_id = ?";

        try(Connection conn = sqlite.connect();
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2)) {

            ResultSet rs1 = stmt1.executeQuery();
            ArrayList<Recipe> recipes = new ArrayList<>();

            while (rs1.next()) {
                int recipeId = rs1.getInt("id");
                String recipeName = rs1.getString("name");
                double recipeServings = rs1.getDouble("servings");
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

                Recipe recipe = new Recipe(recipeId, recipeName, recipeServings, ingredients, instructions, true);
                recipes.add(recipe);
            }

            return recipes;

        } catch (SQLException e) {
            System.out.println("SQLiteRecipeDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates the details of recipe in the database.
     *
     * (requirement 4.5.2)
     *
     * Updates the recipe ingredient relationships by first deleting the old relationships and inserting the new
     * relationships.
     *
     * (requirement 4.5.3)
     *
     * @param recipe The recipe to be updated, includes the recipe details and recipe ingredient relationships
     * @return The updated recipe, null if a problem occured while attempting to updated the recipe
     */
    @Override
    public Recipe updateRecipe(Recipe recipe) {
        String sql1 = "UPDATE recipe SET name = ?, servings = ?, instructions = ? WHERE id = ?";
        String sql2 = "DELETE FROM recipe_ingredient WHERE recipe_ingredient.recipe_id = ?";
        String sql3 = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_type, unit_amount ) VALUES (?, ?, ?, ?)";

        try(Connection conn = sqlite.connect();
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            PreparedStatement stmt3 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS)) {

            stmt1.setString(1, recipe.getName());
            stmt1.setDouble(2, recipe.getServings());
            stmt1.setString(3, recipe.getInstructions());
            stmt1.setInt(4, recipe.getId());

            int affectedRows = stmt1.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("error updating recipe, no rows affected");
            }

            stmt2.setInt(1, recipe.getId());
            stmt2.executeUpdate();

            for (RecipeIngredient ri : recipe.getIngredients()) {
                stmt3.setInt(1, recipe.getId());
                stmt3.setInt(2, ri.getIngredient().getId());
                stmt3.setString(3, ri.getUnitType());
                stmt3.setDouble(4, ri.getUnitAmount());

                stmt3.executeUpdate();

                try (ResultSet generatedKeys = stmt3.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ri.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("creating recipe ingredient failed, no ID obtained");
                    }
                }
            }

            return recipe;
        } catch (SQLException e) {
            System.out.println("SQLiteRecipeDAO error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a recipe by id from the database.
     *
     * (requirement 4.4.2)
     *
     * @param id The id of the recipe being deleted
     * @return True if the recipe is deleted successfully, false if a recipe could not be deleted
     */
    @Override
    public boolean deleteRecipe(int id) {
        String sql1 = "DELETE FROM recipe WHERE id = ?";
        String sql2 = "DELETE FROM recipe_ingredient WHERE recipe_id = ?";

        try (Connection conn = sqlite.connect();
             PreparedStatement stmt1 = conn.prepareStatement(sql1);
             PreparedStatement stmt2 = conn.prepareStatement(sql2)) {

            stmt1.setInt(1, id);

            int affectedRows = stmt1.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("deleting recipe failed, no rows affected");
            }

            stmt2.setInt(1, id);
            stmt2.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("SQLiteRecipeDAO error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserts a recipe into the favorite table in the database.
     *
     * (requirement 4.6.2)
     *
     * @param id The id of the recipe to be inserted into the favorite table
     * @return True if recipe is inserted into the favorite table, false if a problem occured while inserting recipe
     */
    @Override
    public boolean insertRecipeFavorite(int id) {
        String sql = "INSERT INTO favorite (recipe_id) values (?)";

        try (Connection conn = sqlite.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectRows = stmt.executeUpdate();

            if (affectRows == 0) {
                throw new SQLException("error inserting recipe into favorite table, no rows affected");
            }

            return true;
        } catch (SQLException e) {
            System.out.println("SQLiteConecction error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a recipe from the favorite table in the database.
     *
     * (requirement 4.6.3)
     *
     * @param id The id of the recipe being deleted from the favorite table
     * @return True if recipe is deleted from the favorite table, false if a problem occured while deleting recipe
     */
    @Override
    public boolean deleteRecipeFavorite(int id) {
        String sql = "DELETE FROM favorite WHERE recipe_id = ?";

        try (Connection conn = sqlite.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectRows = stmt.executeUpdate();

            if (affectRows == 0) {
                throw new SQLException("error deleting recipe from favorite table, no rows affected");
            }

            return true;
        } catch (SQLException e) {
            System.out.println("SQLiteConecction error: " + e.getMessage());
            return false;
        }
    }
}
