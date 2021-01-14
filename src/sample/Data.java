package sample;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Data {
    String name,description;
    LocalDate date;
    int price;

    public int getPrice() {
        return price;
    }

    public Data(String name, String description, int price,LocalDate date) {
        this.name = name;
        this.price=price;
        this.description = description;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }
    public String toString(){
        return name+"\n"+price+"\n"+new SimpleDateFormat("dd-MM-yyyy").format(new Date()) +"\n-----------------------------\n";
    }
}
