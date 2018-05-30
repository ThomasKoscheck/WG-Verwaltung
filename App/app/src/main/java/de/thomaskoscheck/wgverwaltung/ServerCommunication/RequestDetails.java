package de.thomaskoscheck.wgverwaltung.ServerCommunication;

public class RequestDetails {
    private String product;
    private String requester;
    private double price;
    private int done;


    public RequestDetails(String product, String requester, double price) {
        this.product = product;
        this.requester = requester;
        this.price = price;
        done = 0;
    }

    public RequestDetails(String product, String requester, double price, int done) {
        this.product = product;
        this.requester = requester;
        this.price = price;
        this.done = done;
    }

    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public String getRequester() {
        return requester;
    }

    public int getDone() {
        return done;
    }
}
