package de.thomaskoscheck.wgverwaltung.server_communication;

public class RequestDetails {
    private String product;
    private double price;

    public RequestDetails(String product, double price) {
        this.product = product;
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

}
