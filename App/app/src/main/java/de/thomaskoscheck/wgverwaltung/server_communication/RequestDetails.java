package de.thomaskoscheck.wgverwaltung.server_communication;

public class RequestDetails {
    private final String product;
    private final String requester;
    private final double price;
    private final int done;
    private final int id;

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

    public int getDone() {
        return done;
    }

    public int getId() {
        return id;
    }
}
