package edu.uis.recitree.model;

/**
 * Encapsulates a recipe ingredient relationship.
 *
 * @author Edward Ciccone
 */
public class RecipeIngredient {

    private int id;
    private Ingredient ingredient;
    private String unitType;
    private double unitAmount;

    public RecipeIngredient(Ingredient ingredient, String unitType, double unitAmount) {
        this(0, ingredient, unitType, unitAmount);
    }

    public RecipeIngredient(int id, Ingredient ingredient, String unitType, double unitAmount) {
        this.id = id;
        this.ingredient = ingredient;
        this.unitType = unitType;
        this.unitAmount = unitAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public double getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(double unitAmount) {
        this.unitAmount = unitAmount;
    }

    @Override
    public String toString() {
        return unitAmount + " " + unitType + " " + ingredient;
    }
}
