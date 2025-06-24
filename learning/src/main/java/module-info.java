module xujiejie {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens xujiejie.controller to javafx.fxml;
    exports xujiejie;
}
