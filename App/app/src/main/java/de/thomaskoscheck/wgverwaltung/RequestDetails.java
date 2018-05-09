package de.thomaskoscheck.wgverwaltung;

public class RequestDetails {
    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    String product;
    double price;

    public RequestDetails(String product, double price) {
        this.product = product;
        this.price = price;
    }
}
