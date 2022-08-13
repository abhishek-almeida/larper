module based.larper {
    requires javafx.controls;
    requires javafx.fxml;
    requires jmusic;
    requires java.sql;
    requires java.desktop;


    opens based.larper to javafx.fxml;
    exports based.larper;
}