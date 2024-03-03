package de.webstore.backend.dto;

import java.time.LocalDate;

public class OrderDTO {
    
    private int orderNumber;
    private LocalDate date;

    // Constructors
    public OrderDTO() {}

    public OrderDTO(int orderNumber, LocalDate date) {
        this.orderNumber = orderNumber;
        this.date = date;
    }

    // Getter
    public int getOrderNumber() {
        return orderNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    // Setter
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setDate(LocalDate localDate) {
        this.date = localDate;
    }

    // toString
    @Override
    public String toString() {
        return "AuftragDTO{" +
                "Auftragsnummer=" + orderNumber +
                ", Datum=" + date +
                '}';
    }
}