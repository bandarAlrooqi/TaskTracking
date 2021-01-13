package sample;

import com.sun.javafx.collections.ImmutableObservableList;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public ListView<String> toDoList;
    public Button addToList = new Button();
    @FXML
    ObservableList<String> list = FXCollections.observableArrayList("Bandar");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toDoList.setItems(list);
        toDoList.getItems().add("Bandar2");

    }

    public void clickAddToList(ActionEvent actionEvent) {
        toDoList.getItems().add("Clicked!!");
    }
}
