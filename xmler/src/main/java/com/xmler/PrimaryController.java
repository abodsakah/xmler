package com.xmler;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class PrimaryController {

    @FXML private TextField textField;

    @FXML
    private void openFileBrowser() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(null);

        // set the file path to the text field with fx:id fileInput
        textField.setText(fileChooser.getInitialDirectory().toString());
    }
}
