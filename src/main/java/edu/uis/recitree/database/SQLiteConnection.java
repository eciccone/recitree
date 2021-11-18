package edu.uis.recitree.database;

import java.sql.*;

/**
 * This class is responsible for establishing a connection to a SQLite database and building the required tables
 *
 * @author Edward Ciccone
 */
public class SQLiteConnection {

    private static final String DB_URL = "jdbc:sqlite:recitree.db";

    private static final String RECIPE_TABLE =
            "CREATE TABLE IF NOT EXISTS recipe (" +
                "id INTEGER NOT NULL UNIQUE," +
                "name TEXT NOT NULL," +
                "servings REAL NOT NULL," +
                "instructions TEXT," +
                "PRIMARY KEY (id AUTOINCREMENT)" +
            ");";

    private static final String INGREDIENT_TABLE =
            "CREATE TABLE IF NOT EXISTS ingredient ( " +
                "id INTEGER NOT NULL UNIQUE, " +
                "name TEXT NOT NULL UNIQUE, " +
                "PRIMARY KEY( id AUTOINCREMENT) " +
            ");";

    private static final String RECIPE_INGREDIENT_TABLE =
            "CREATE TABLE IF NOT EXISTS recipe_ingredient ( " +
                "id INTEGER NOT NULL UNIQUE, " +
                "recipe_id INTEGER NOT NULL, " +
                "ingredient_id INTEGER NOT NULL, " +
                "unit_type TEXT NOT NULL, " +
                "unit_amount REAL NOT NULL, " +
                "FOREIGN KEY(recipe_id) REFERENCES recipe(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE, " +
                "PRIMARY KEY(id AUTOINCREMENT)" +
            ");";

    private static final String FAVORITE_TABLE =
            "CREATE TABLE IF NOT EXISTS favorite (" +
                "id INTEGER NOT NULL UNIQUE," +
                "recipe_id INTEGER NOT NULL," +
                "FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE," +
                "PRIMARY KEY (id AUTOINCREMENT)" +
            ");";

    private static final String TAG_TABLE =
            "CREATE TABLE tag (" +
                "tag_name TEXT NOT NULL," +
                "recipe_id INTEGER NOT NULL," +
                "FOREIGN KEY(recipe_id) REFERENCES recipe(id) ON DELETE CASCADE," +
                "CONSTRAINT unq UNIQUE (tag_name, recipe_id)" +
            ");";

    /**
     * Establishes a connection to the database
     *
     * @return The connection to the database
     * @throws SQLException Thrown if there is a problem connecting to the database
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * In order for the sqlite jdbc to conform to foreign key requirements, foreign keys must be turned on with every
     * new connection. This is required for foreign key constraints and cascade deletes.
     *
     * @param conn The opened connection
     * @throws SQLException Thrown if there is an error executing update to turn on foreign keys
     */
    public void enableForeignKey(Connection conn) throws SQLException {
        String sql = "PRAGMA foreign_keys = ON";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throw new SQLException("enabling foreign keys failed");
        }
    }

    /**
     * Builds the recipe, ingredient, recipe_ingredient, and favorite tables
     */
    public static void buildTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            buildRecipeTable(stmt);
            buildIngredientTable(stmt);
            buildRecipeIngredientTable(stmt);
            buildFavoriteTable(stmt);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Builds the recipe table if it does not already exist with the following columns:
     * id, name, servings, instructions
     *
     * (requirement 1.1.0)
     *
     * @param stmt The object used to execute the SQL statement for building the recipe table in the database
     * @throws SQLException Thrown if there is a problem creating the recipe table
     */
    private static void buildRecipeTable(Statement stmt) throws SQLException {
        stmt.execute(RECIPE_TABLE);
    }

    /**
     * Builds the ingredient table if it does not already exist with the following columns:
     * id, name
     *
     * (requirement 1.2.0)
     *
     * @param stmt The object used to execute the SQL statement for building the ingredient table in the database
     * @throws SQLException Thrown if there is a problem creating the ingredient table
     */
    private static void buildIngredientTable(Statement stmt) throws SQLException {
        stmt.execute(INGREDIENT_TABLE);
    }

    /**
     * Builds the recipe ingredient relationship table if it does not already exist with the following columns:
     * id, recipe_id, ingredient_id, unit_type, unit_amount
     *
     * (requirement 1.3.0)
     *
     * @param stmt The object used to execute the SQL statement for building the recipe_ingredient table in the database
     * @throws SQLException Thrown if there is a problem creating the recipe_ingredient table
     */
    private static void buildRecipeIngredientTable(Statement stmt) throws SQLException {
        stmt.execute(RECIPE_INGREDIENT_TABLE);
    }

    /**
     * Builds the favorite table if it does not already exist with the following columns:
     * id, recipe_id
     *
     * (requirement 1.4.0)
     *
     * @param stmt The object used to execute the SQL statement for building the favorite table in the database
     * @throws SQLException Thrown if there is a problem creating the favorite table
     */
    private static void buildFavoriteTable(Statement stmt) throws SQLException {
        stmt.execute(FAVORITE_TABLE);
    }
}
