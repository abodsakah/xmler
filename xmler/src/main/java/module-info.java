module com.xmler {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.xmler to javafx.fxml;
    exports com.xmler;
}
