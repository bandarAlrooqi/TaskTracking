package sample;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
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
    public TableColumn<Data, Boolean> payToDoCol = new TableColumn<>();
    // Table two
    public TableView<Data> tableInProgress = new TableView<>();
    public TableColumn<Data, String> nameIn = new TableColumn<>();
    public TableColumn<Data, String> descriptionIn = new TableColumn<>();
    public TableColumn<Data, Integer> priceIn = new TableColumn<>();
    public TableColumn<Data, String> dueDateIn = new TableColumn<>();
    public TableColumn<Data, Boolean> payInPCol = new TableColumn<>();
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


    String doneListString = "";
    int edit = 0;
    Data data;
    TableView<Data> table = new TableView<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addToList.setVisible(false);
        moveTaskB.setVisible(false);
        deleteB.setVisible(false);
        dueDate.setEditable(false);

        setTable(nameColToDo, descriptionToDo, priceToDo, dueDateToDo, tableToDo, payToDoCol);
        setTable(nameIn, descriptionIn, priceIn, dueDateIn, tableInProgress, payInPCol);


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

    private void setTable(TableColumn<Data, String> name, TableColumn<Data, String> description, TableColumn<Data, Integer> price, TableColumn<Data, String> dueDate, TableView<Data> table, TableColumn<Data, Boolean> payCol) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        description.setMinWidth(250);
        table.setMinWidth(455);
        name.setMinWidth(name.getWidth()+10);
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        dueDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        table.setEditable(true);
        payCol.setEditable(true);
        payCol.setCellValueFactory(p->p.getValue().isPaid(tableToDo,tableInProgress,doneList));
        payCol.setCellFactory(CheckBoxTableCell.forTableColumn(payCol));

    }


    public void clickAddToList() {
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
        if (tableToDo.getSelectionModel().selectedIndexProperty().getValue() != -1)
            tableToDo.getSelectionModel().clearSelection();
        if (tableInProgress.getSelectionModel().selectedIndexProperty().getValue() != -1)
            tableInProgress.getSelectionModel().clearSelection();
        if(doneList.getSelectionModel().selectedIndexProperty().getValue() != -1){edit =1;switchStyle();addToList.setVisible(true);deleteB.setVisible(true);}
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
         table = (tableToDo.getSelectionModel().selectedIndexProperty().getValue() != -1)?tableToDo:edit!=2?tableInProgress:table;
        if(edit==1){
            if(doneList.getSelectionModel().getSelectedIndex()!=-1){
                doneListString = doneList.getItems().remove(doneList.getSelectionModel().getSelectedIndex());
                var value = doneListString.replaceAll("\\n"," ");
                name.setText(value.substring(6,value.indexOf("Description:")).trim());
                description.setText(value.substring(value.indexOf("Description: ")+13,value.indexOf("Price:")).trim());
                price.setText(value.substring(value.indexOf("Price:")+7,value.indexOf("Paid:")).trim());
                dueDate.setValue(LocalDate.parse(value.substring(value.indexOf("Date: ")+6,value.indexOf("Time taken from")).trim()));
                deleteB.setVisible(false);
                edit++;
                return;
            }
            data = table.getItems().remove(table.getSelectionModel().getFocusedIndex());
            name.setText(data.name);
            description.setText(data.description);
            price.setText(String.valueOf(data.price));
            dueDate.setValue(data.date.toLocalDate());
            deleteB.setVisible(false);
            edit++;
        }
        else {
            if(!doneListString.isBlank()){
                StringBuilder m = new StringBuilder(doneListString);
                m.replace(6,doneListString.indexOf("Description:")-1,name.getText());
                m.replace(m.indexOf("Description:")+"Description: ".length(),m.indexOf("Price: ")-1,description.getText());
                m.replace(m.indexOf("Price:")+"Price: ".length(),m.indexOf("Paid:")-1,price.getText());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.getButtonTypes().set(0,ButtonType.YES);
                alert.getButtonTypes().add(ButtonType.NO);
                alert.getButtonTypes().remove(1);
                alert.setContentText("Is the task paid ? ");
                Optional<ButtonType> a =alert.showAndWait();
                m.replace(m.indexOf("Paid:")+"Paid: ".length(),+m.indexOf("Finish")-1,((a.isPresent())&&a.get().getText().equals("Yes"))?"YES":"NO");
                doneList.getItems().add(m.toString());
                switchButton();
                moveTaskB.setVisible(false);
                deleteB.setVisible(false);
                RWFile.update(tableToDo,tableInProgress,doneList);
                checkIncome();
                clear();
                edit=0;
                doneListString= "";

                return;
            }
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
