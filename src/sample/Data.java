package sample;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {
    String name,description,date;
    int price;

    public int getPrice() {
        return price;
    }

    public Data(String name, String description, int price) {
        this.name = name;
        this.price=price;
        this.description = description;
        this.date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
    public String toString(){
        return name+"\n"+price+"\n"+new SimpleDateFormat("dd-MM-yyyy").format(new Date()) +"\n-----------------------------\n";
    }
}
