module com.emsi.subtracker {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.emsi.subtracker to javafx.fxml;
    opens com.emsi.subtracker.views to javafx.fxml;

    exports com.emsi.subtracker;
}
