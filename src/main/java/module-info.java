module com.xmler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    
    opens com.xmler to javafx.fxml;
    exports com.xmler;
}
