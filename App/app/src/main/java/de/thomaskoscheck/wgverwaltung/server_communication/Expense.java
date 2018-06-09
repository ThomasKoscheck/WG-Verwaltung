package de.thomaskoscheck.wgverwaltung.server_communication;

import java.util.Objects;

public class Expense {
    private final String requester;
    private final String product;
    private final double price;
    private final int id;

    public Expense(String requester, String product, double price, int id) {
        this.requester = requester;
        this.product = product;
        this.price = price;
        this.id = id;
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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "requester='" + requester + '\'' +
                ", product='" + product + '\'' +
                ", price=" + price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Expense expense = (Expense) o;
        return Double.compare(expense.price, price) == 0 &&
                id == expense.id &&
                Objects.equals(requester, expense.requester) &&
                Objects.equals(product, expense.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requester, product, price, id);
    }
}