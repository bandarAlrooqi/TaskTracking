package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class RWFile {
    static File toDo = new File("toDo");
    static File inP = new File("inProgress");
    static File done = new File("done");

    public static Data toDo(Data data) {
        return getData(data, toDo);
    }

    public static Data inProgress(Data data) {
        data.startWorkingDate = LocalDateTime.now();
        return getData(data, inP);
    }

    public static String done(Data data) {
        return getData(data);
    }

    private static Data getData(Data data, File file) {
        try {
            var write = new FileWriter(file, true);
            write.write(data.name + "\n" + data.description + "\n" + data.price + "\n" + data.date + "\n" + data.receiveDate + "\n\n");
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static String getData(Data data) {
        try {
            var write = new FileWriter(done, true);
            data.endDate = LocalDateTime.now();
            write.write(data.toString());
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
    private static void getData(String data){
        try {
            var write = new FileWriter(done, true);
            write.write(data+"\n");
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void update(TableView<Data>a , TableView<Data>b, ListView<String>c){
        try {
            new PrintWriter(done).close();
            new PrintWriter(toDo).close();
            new PrintWriter(inP).close();
            a.getItems().forEach(i->getData(i,toDo));
            b.getItems().forEach(i->getData(i,inP));
            c.getItems().forEach(RWFile::getData);
            c.getItems().forEach(System.out::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static ObservableList<Data> readToDo(){return readTable(toDo);}
    public static ObservableList<Data> readInProgress(){return readTable(inP);}
    private static ObservableList<Data> readTable(File file){
        ObservableList<Data> i = FXCollections.observableArrayList();
        if(!file.exists())return FXCollections.observableArrayList();
        try {
            Scanner read = new Scanner(file);
            while (read.hasNext()){
                var name = read.nextLine();
                if(name.length() == 0)continue;
                var des = read.nextLine();
                var price = read.nextLine();
                var date =read.nextLine();
                var rec = read.nextLine();
                i.add(new Data(name,des,Integer.parseInt(price),LocalDateTime.parse(date),LocalDateTime.parse(rec)));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return i;
    }
    public static ObservableList<String> readDone(){
        ObservableList<String> i = FXCollections.observableArrayList();
        if(!done.exists())return FXCollections.observableArrayList();
        try {
            Scanner read = new Scanner(done);
            StringBuilder oneString = new StringBuilder();
            while (read.hasNext()) {
                String r = read.nextLine();
                if(!r.isBlank())
                    oneString.append(r).append("\n");
                else{

                    if(!oneString.toString().isBlank())
                         i.add(oneString.toString());
                oneString = new StringBuilder();
                }
            }
            if(!oneString.toString().isBlank())
                i.add(oneString.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return i;
    }

}
