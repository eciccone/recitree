package edu.uis.recitree.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnection {

    private static final String DB_URL = "jdbc:sqlite:recitree.db";

    private static final String INGREDIENT_TABLE =
            "CREATE TABLE IF NOT EXISTS ingredient ( " +
                    "id INTEGER NOT NULL UNIQUE, " +
                    "name TEXT NOT NULL UNIQUE, " +
                    "PRIMARY KEY( id AUTOINCREMENT) " +
                    ");";

    private static final String RECIPE_TABLE =
            "CREATE TABLE IF NOT EXISTS recipe ( " +
                    "id	INTEGER NOT NULL UNIQUE, " +
                    "name TEXT NOT NULL, " +
                    "servings REAL NOT NULL, " +
                    "PRIMARY KEY(id AUTOINCREMENT) " +
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

    public SQLiteConnection() {}

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void buildTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            stmt.execute(INGREDIENT_TABLE);
            stmt.execute(RECIPE_TABLE);
            stmt.execute(RECIPE_INGREDIENT_TABLE);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
