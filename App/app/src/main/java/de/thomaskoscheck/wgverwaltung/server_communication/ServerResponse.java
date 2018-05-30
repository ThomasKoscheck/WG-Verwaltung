package de.thomaskoscheck.wgverwaltung.server_communication;

public class ServerResponse {
    private final String credit;
    private final Expense[] expenses;

    public ServerResponse(String credit, Expense[] expenses) {
        this.credit = credit;
        this.expenses = expenses;
    }

    public String getCredit() {
        return credit;
    }

    public Expense[] getExpenses() {
        return expenses;
    }
}