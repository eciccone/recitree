module edu.uis.recitree {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;

    opens edu.uis.recitree to javafx.fxml;
    exports edu.uis.recitree;
    exports edu.uis.recitree.controller;
    opens edu.uis.recitree.controller to javafx.fxml;
}