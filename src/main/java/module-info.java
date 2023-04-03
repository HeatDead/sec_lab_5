module com.example.sec_lab_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.desktop;
    requires commons.lang;
    requires org.apache.commons.io;


    opens com.example.sec_lab_5 to javafx.fxml;
    exports com.example.sec_lab_5;
}