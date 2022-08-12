module based.larper {
    requires javafx.controls;
    requires javafx.fxml;
    requires jmusic;
    requires java.sql;


    opens based.larper to javafx.fxml;
    exports based.larper;
}