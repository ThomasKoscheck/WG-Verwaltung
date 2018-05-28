package de.thomaskoscheck.wgverwaltung.ServerCommunication;

public class RequestDetails {
    private String product;
    private String requester;
    private double price;
    private boolean done;


    public RequestDetails(String product, String requester, double price) {
        this.product = product;
        this.requester = requester;
        this.price = price;
        done = false;
    }

    public RequestDetails(String product, String requester, double price, boolean done) {
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

    public boolean isDone() {
        return done;
    }

    public String getRequester() {
        return requester;
    }
}
