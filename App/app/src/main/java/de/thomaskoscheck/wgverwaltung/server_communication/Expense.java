package de.thomaskoscheck.wgverwaltung.server_communication;

public class Expense {
    private String requester;
    private String product;
    private double price;
    private int id;

    public Expense(String requester, String product, double price, int id) {
        this.requester = requester;
        this.product = product;
        this.price = price;
        this.id = id;
    }

    public String getRequester() {
        return requester;
    }

    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "requester='" + requester + '\'' +
                ", product='" + product + '\'' +
                ", price=" + price;
    }
}