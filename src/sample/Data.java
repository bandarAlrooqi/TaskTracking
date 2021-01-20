package sample;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Data {
    String name, description;
    int price;
    ObservableBooleanValue paid = new SimpleBooleanProperty(false);
    LocalDateTime date;
    LocalDateTime receiveDate;
    LocalDateTime startWorkingDate = LocalDateTime.now();
    LocalDateTime endDate;

    public Data(String name, String description, int price, LocalDateTime date) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.date = date;
        receiveDate = LocalDateTime.now();
    }
    public Data(String name, String description, int price, LocalDateTime date, LocalDateTime receiveDate, LocalDateTime startWorkingDate, boolean paid) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.date = date;
        this.receiveDate = receiveDate;
        this.startWorkingDate = startWorkingDate;
        this.paid = new SimpleBooleanProperty(paid);
    }
    public ObservableBooleanValue isPaid(TableView<Data> t1, TableView<Data> t2, ListView<String> l) {
        RWFile.update(t1,t2,l);
        return paid;
    }

    public int getPrice() {
        return price;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public LocalDate getDate() {
        return date.toLocalDate();
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    private String getDurationAsString(Duration duration) {
        StringBuilder durationAsStringBuilder = new StringBuilder();
        if (duration.toDays() > 0) {
            String postfix = duration.toDays() == 1 ? "" : "s";
            durationAsStringBuilder.append(duration.toDays()).append(" day");
            durationAsStringBuilder.append(postfix);
        }

        duration = duration.minusDays(duration.toDays());
        long hours = duration.toHours();
        String s = durationAsStringBuilder.toString().isBlank() ? "" : ", ";
        if (hours > 0) {
            String postfix = hours == 1 ? "" : "s";
            durationAsStringBuilder.append(s);
            durationAsStringBuilder.append(hours).append(" hour");
            durationAsStringBuilder.append(postfix);
        }

        duration = duration.minusHours(duration.toHours());
        long minutes = duration.toMinutes();
        if (minutes > 0) {
            String postfix = minutes == 1 ? "" : "s";
            durationAsStringBuilder.append(s);
            durationAsStringBuilder.append(minutes).append(" minute");
            durationAsStringBuilder.append(postfix);
        }

        return durationAsStringBuilder.toString();
    }

    @Override
    public String toString() {
        var fromReceiveTillNow = (getDurationAsString(Duration.between(receiveDate, endDate)).isBlank()) ?
                "No Time" : getDurationAsString(Duration.between(receiveDate, endDate));
        var fromWorking = (getDurationAsString(Duration.between(startWorkingDate, endDate)).isBlank()) ?
                "No Time" : getDurationAsString(Duration.between(startWorkingDate, endDate));
        return "Name: " + name + "\nDescription: " + description + "\nPrice: " + price +"\nPaid: "+(paid.getValue()?"YES":"NO")+"\nFinish Date: "
                +endDate.toLocalDate()+ "\nTime taken from received date: " + fromReceiveTillNow+ "\nTime taken in progress: "+fromWorking+"\n\n";
    }
}
