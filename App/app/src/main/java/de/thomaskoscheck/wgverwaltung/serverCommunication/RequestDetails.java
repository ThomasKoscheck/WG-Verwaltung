package de.thomaskoscheck.wgverwaltung.serverCommunication;

public class RequestDetails {
    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    private String product;
    private double price;

    public RequestDetails(String product, double price) {
        this.product = product;
        this.price = price;
    }
}
