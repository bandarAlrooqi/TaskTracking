package sample;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Data {
    String name, description;
    LocalDateTime date;
    LocalDateTime receiveDate;
    LocalDateTime endDate;
    int price;

    public Data(String name, String description, int price, LocalDateTime date) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.date = date;
        receiveDate = LocalDateTime.now();
    }
    public Data(String name, String description, int price, LocalDateTime date,LocalDateTime receiveDate) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.date = date;
        this.receiveDate = receiveDate;
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
        var d = (getDurationAsString(Duration.between(receiveDate, endDate)).isBlank()) ? "No Time" : getDurationAsString(Duration.between(receiveDate, endDate));
        return "Name: " + name + "\nDescription: " + description + "\nPrice: " + price + "\nCompleted in : " + d + "\n\n";
    }
}
