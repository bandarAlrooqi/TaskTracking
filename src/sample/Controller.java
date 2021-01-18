package sample;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
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
    public Button deleteB = new Button();
    //Text Filed
    public TextField name = new TextField();
    public TextField description = new TextField();
    public TextField price = new TextField();
    public DatePicker dueDate = new DatePicker();
    //Slider
    public Slider incomeSlider;
    //Label
    public Label incomeL;

    int edit = 0;
    Data data;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addToList.setVisible(false);
        moveTaskB.setVisible(false);
        deleteB.setVisible(false);
        dueDate.setEditable(false);
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
        incomeL.setText(RWFile.calculateIncome(1));
        //rgx for Texts
        name.setTextFormatter(new TextFormatter<>(change -> {
            switchStyle();
            if(edit == 0)
            addToList.setVisible(!(price.getText().length() <= 0 || name.getText().length() <= 0 || description.getText().length() <= 0 || dueDate.getValue() == null));
            Pattern p = Pattern.compile("[A-za-z ]+");
            return (p.matcher(change.getControlNewText()).matches()) || (change.getControlNewText().length() == 0) ? change : null;
        }));
        price.setTextFormatter(new TextFormatter<>(change -> {
            switchStyle();
            if(edit == 0)
            addToList.setVisible(!(price.getText().length() <= 0 || name.getText().length() <= 0 || description.getText().length() <= 0 || dueDate.getValue() == null));
            Pattern p = Pattern.compile("\\d+");
            return (p.matcher(change.getControlNewText()).matches()) || (change.getControlNewText().length() == 0) ? change : null;
        }));

        tableToDo.getSelectionModel().selectedItemProperty().addListener((observableValue, data, t1) -> {
            moveTaskB.setVisible(tableToDo.getSelectionModel().selectedIndexProperty().getValue() != -1);
            moveTaskB.setText("Move");
        });
        tableInProgress.getSelectionModel().selectedItemProperty().addListener((observableValue, data, t1) -> {
            tableToDo.getSelectionModel().clearSelection();
            boolean check = tableInProgress.getSelectionModel().selectedIndexProperty().getValue() != -1;
            moveTaskB.setText(check ? "Done" : "Move");
            moveTaskB.setVisible(check);
        });

    }


    public void clickAddToList() {
        System.out.println("CLICk "+ edit);
        switchStyle();
        if(edit != 0){
            edit();
            return;
        }
        tableToDo.getItems().add(RWFile.toDo(new Data(name.getText(), description.getText(), Integer.parseInt(price.getText()), dueDate.getValue().atStartOfDay())));
        clear();
    }

    public void clickMoveB() {
        edit = 0;
        moveTaskB.setText("Move");
        int indexToDo = tableToDo.getSelectionModel().selectedIndexProperty().getValue();
        int indexInProgress = tableInProgress.getSelectionModel().selectedIndexProperty().getValue();

        if (indexToDo != -1)
            tableInProgress.getItems().add(RWFile.inProgress(tableToDo.getItems().remove(indexToDo)));

        else if (indexInProgress != -1)
            doneList.getItems().add(RWFile.done(tableInProgress.getItems().remove(indexInProgress)));
        RWFile.update(tableToDo, tableInProgress, doneList);
        checkIncome();
        moveTaskB.setVisible(false);
        deleteB.setVisible(false);
        addToList.setVisible(false);
        tableToDo.getSelectionModel().clearSelection();
        tableInProgress.getSelectionModel().clearSelection();

    }

    public void changeDate() {
        if(edit == 0)
        addToList.setVisible(!(price.getText().length() <= 0 || name.getText().length() <= 0 || description.getText().length() <= 0 || dueDate.getValue() == null));
    }

    public void checkTodo() {
        moveTaskB.setText("Move");
        if(tableToDo.getSelectionModel().selectedIndexProperty().getValue() != -1) {
            switchStyle();
            edit =1;
        }else edit =0;
        switcher(tableInProgress, tableToDo);
    }

    public void checkInProgress() {
        if (tableInProgress.getSelectionModel().selectedIndexProperty().getValue() != -1) {
            moveTaskB.setText("Done");
            edit=1;
        }else
            edit = 0;
        switcher(tableToDo, tableInProgress);
        switchStyle();
    }

    private void switcher(TableView<Data> tableToDo, TableView<Data> tableInProgress) {
        addToList.setVisible(false);
        moveTaskB.setVisible(false);
        deleteB.setVisible(false);
        if (tableToDo.getSelectionModel().selectedIndexProperty().getValue() != -1)
            tableToDo.getSelectionModel().clearSelection();
        if (doneList.getSelectionModel().getSelectedIndex() != -1)
            doneList.getSelectionModel().clearSelection();
        if (tableInProgress.getSelectionModel().selectedIndexProperty().getValue() == -1) return;
        deleteB.setVisible(true);
        moveTaskB.setVisible(true);
        switchStyle();
        addToList.setVisible(true);
    }

    public void checkDone() {
        moveTaskB.setVisible(false);
        addToList.setVisible(false);
        deleteB.setVisible(true);
        if (tableToDo.getSelectionModel().selectedIndexProperty().getValue() != -1)
            tableToDo.getSelectionModel().clearSelection();
        if (tableInProgress.getSelectionModel().selectedIndexProperty().getValue() != -1)
            tableInProgress.getSelectionModel().clearSelection();
    }

    public void clickDelete() {
        moveTaskB.setVisible(false);
        deleteB.setVisible(false);
        addToList.setVisible(false);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.getButtonTypes().set(0,ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);
        alert.setContentText("Are you sure you want to delete ? ");
        Optional<ButtonType> a =alert.showAndWait();
        if((a.isPresent())&&(!a.get().getText().equals("Yes")))return;

        int indexDoneList, indexTodoList, indexInP;
        if ((indexDoneList = doneList.getSelectionModel().selectedIndexProperty().get()) != -1)
            doneList.getItems().remove(indexDoneList);
        else if ((indexTodoList = tableToDo.getSelectionModel().selectedIndexProperty().getValue()) != -1)
            tableToDo.getItems().remove(indexTodoList);
        else if ((indexInP = tableInProgress.getSelectionModel().selectedIndexProperty().getValue()) != -1)
            tableInProgress.getItems().remove(indexInP);

        RWFile.update(tableToDo, tableInProgress, doneList);
        checkIncome();
        edit =0;
    }
    public void edit(){
        addToList.setVisible(true);
        switchStyle();
        var table = (tableToDo.getSelectionModel().selectedIndexProperty().getValue() != -1)?tableToDo:tableInProgress;
        System.out.println(edit);
        if(edit==1){
            data = table.getItems().remove(table.getSelectionModel().getFocusedIndex());
            name.setText(data.name);
            description.setText(data.description);
            price.setText(String.valueOf(data.price));
            dueDate.setValue(data.date.toLocalDate());
            deleteB.setVisible(false);
            edit++;
        }
        else if(edit == 2) {
            data.name = name.getText();
            data.description=description.getText();
            data.price=Integer.parseInt(price.getText());
            data.date = dueDate.getValue().atStartOfDay();
            switchButton();
            moveTaskB.setVisible(false);
            deleteB.setVisible(false);
            table.getItems().add(data);
            RWFile.update(tableToDo,tableInProgress,doneList);
            clear();
            edit=0;
        }
    }
    public void switchStyle(){
        if(edit == 0) {
            addToList.setStyle("-fx-background-color: #6dc283; -fx-text-fill: white");
            addToList.setText("Add");
            return;
        }
        addToList.setText("Edit");
        addToList.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: white");

    }
    public void clear(){
        name.clear();
        description.clear();
        price.clear();
        dueDate.setValue(null);
    }
    public void switchButton(){
        addToList.setVisible(!addToList.isVisible());
        deleteB.setVisible(!addToList.isVisible());
        moveTaskB.setVisible(!addToList.isVisible());
    }

    public void checkIncome() {
       incomeL.setText(RWFile.calculateIncome((int) incomeSlider.getValue()));
    }
}
