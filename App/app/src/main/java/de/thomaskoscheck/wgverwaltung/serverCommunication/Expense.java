package de.thomaskoscheck.wgverwaltung.serverCommunication;

public class Expense {
    private String requester;
    private String product;
    private double price;

    public Expense(String requester, String product, double price) {
        this.requester = requester;
        this.product = product;
        this.price = price;
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

}
