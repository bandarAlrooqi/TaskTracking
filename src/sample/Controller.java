package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Controller implements Initializable {
    @FXML
    // Table One
    public TableView<Data> tableToDo = new TableView<>();
    public TableColumn<Data, String> nameColToDo = new TableColumn<>();
    public TableColumn<Data, String> descriptionToDo = new TableColumn<>();
    public TableColumn<Data, Integer> priceToDo = new TableColumn<>();
    public TableColumn<Data, String> dueDateToDo = new TableColumn<>();
    // Table two
    public TableView<Data> tableInProgress = new TableView<>();
    public TableColumn<Data, String> nameIn = new TableColumn<>();
    public TableColumn<Data, String> descriptionIn = new TableColumn<>();
    public TableColumn<Data, Integer> priceIn = new TableColumn<>();
    public TableColumn<Data, String> dueDateIn = new TableColumn<>();
    // Done List
    public ListView<String> doneList;
    //Button
    public Button addToList = new Button();
    public Button moveTaskB = new Button();
    //Text Filed
    public TextField name = new TextField();
    public TextField description = new TextField();
    public TextField price = new TextField();
    public DatePicker dueDate = new DatePicker();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        moveTaskB.setDisable(true);
        nameColToDo.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionToDo.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceToDo.setCellValueFactory(new PropertyValueFactory<>("price"));
        dueDateToDo.setCellValueFactory(new PropertyValueFactory<>("date"));

        nameIn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionIn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceIn.setCellValueFactory(new PropertyValueFactory<>("price"));
        dueDateIn.setCellValueFactory(new PropertyValueFactory<>("date"));
        //initial values
        tableToDo.setItems(RWFile.readToDo());
        tableInProgress.setItems(RWFile.readInProgress());
        doneList.setItems(RWFile.readDone());
        //rgx for Texts
        name.setTextFormatter(new TextFormatter<>(change -> {
            addToList.setDisable(price.getText().length() <= 0 || name.getText().length() <= 0 || description.getText().length() <= 0);
            Pattern p = Pattern.compile("[A-za-z[ ]]+");
            return (p.matcher(change.getControlNewText()).matches()) || (change.getControlNewText().length() == 0) ? change : null;
        }));
        price.setTextFormatter(new TextFormatter<Object>(change -> {
            addToList.setDisable(price.getText().length() <= 0 || name.getText().length() <= 0 || description.getText().length() <= 0);
            Pattern p = Pattern.compile("\\d+");
            return (p.matcher(change.getControlNewText()).matches()) || (change.getControlNewText().length() == 0) ? change : null;
        }));
        tableToDo.getSelectionModel().selectedItemProperty().addListener((observableValue, data, t1) -> moveTaskB.setDisable(tableToDo.getSelectionModel().selectedIndexProperty().getValue() == -1));
        tableInProgress.getSelectionModel().selectedItemProperty().addListener((observableValue, data, t1) -> {
            boolean check = tableInProgress.getSelectionModel().selectedIndexProperty().getValue() != -1;
            if (check) {
                moveTaskB.setText("Done");
                moveTaskB.setDisable(false);
            }
        });
    }


    public void clickAddToList(ActionEvent actionEvent) {
        tableToDo.getItems().add(RWFile.toDo(new Data(name.getText(), description.getText(), Integer.parseInt(price.getText()), dueDate.getValue().atStartOfDay())));
    }

    public void clickMoveB(ActionEvent actionEvent) {
        moveTaskB.setText("Move");
        int indexToDo = tableToDo.getSelectionModel().selectedIndexProperty().getValue();
        int indexInProgress = tableInProgress.getSelectionModel().selectedIndexProperty().getValue();

        if (indexToDo != -1)
            tableInProgress.getItems().add(RWFile.inProgress(tableToDo.getItems().remove(indexToDo)));

        else if (indexInProgress != -1)
            doneList.getItems().add(RWFile.done(tableInProgress.getItems().remove(indexInProgress)));

        moveTaskB.setDisable(true);
        tableToDo.getSelectionModel().clearSelection();
        tableInProgress.getSelectionModel().clearSelection();
        RWFile.update(tableToDo,tableInProgress,doneList);
    }
}
