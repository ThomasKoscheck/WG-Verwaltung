package de.thomaskoscheck.wgverwaltung;

class Expense {
    private String requester;
    private String product;
    private double price;
    private boolean done;

    Expense(String requester, String product, double price, boolean done) {
        this.requester = requester;
        this.product = product;
        this.price = price;
        this.done = done;
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

    public boolean isDone() {
        return done;
    }
}
