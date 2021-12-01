package edu.uis.recitree;

public class Launcher {

    /**
     * Starter point of code. This redundant main method is necessary because of a bug in JavaFX. If your main method
     * is in a class that extends the application class, and you build the code into a jar file, an error will be thrown
     * when ran. By providing a main method in a class that does not extend the application class the jar file will
     * run.
     *
     * @param args
     */
    public static void main(String[] args) {
        App.main(args);
    }
}
