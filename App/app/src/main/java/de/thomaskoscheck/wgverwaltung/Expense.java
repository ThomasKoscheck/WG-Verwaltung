package de.thomaskoscheck.wgverwaltung;

class Expense {
    private String requester;
    private String product;
    private double price;

    Expense(String requester, String product, double price) {
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
