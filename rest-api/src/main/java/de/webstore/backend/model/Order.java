package de.webstore.backend.model;

import java.sql.Date;

public class Order {
    
    private int orderNumber;
    private Date date;

    // Getter und Setter
    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrdernumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // toString-Methode
    @Override
    public String toString() {
        return "Auftrag{" +
                "Auftragsnummer=" + orderNumber +
                ", Datum=" + date +
                '}';
    }
}
