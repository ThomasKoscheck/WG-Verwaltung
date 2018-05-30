package de.thomaskoscheck.wgverwaltung.ServerCommunication;

public class RequestDetails {
    private String product;
    private String requester;
    private double price;
    private int done;
    private int id;


    public RequestDetails(String product, String requester, double price) {
        this.product = product;
        this.requester = requester;
        this.price = price;
        this.done = 0;
        this.id = -1;
    }

    public RequestDetails(String product, String requester, double price, int done, int id) {
        this.product = product;
        this.requester = requester;
        this.price = price;
        this.done = done;
        this.id = id;
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

    public int getId() {
        return id;
    }
}
