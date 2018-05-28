package de.thomaskoscheck.wgverwaltung.server_communication;

public class ServerResponse {
    private String credit;
    private Expense[] expenses;

    public ServerResponse(String credit, Expense[] expenses) {
        this.credit = credit;
        this.expenses = expenses;
    }

    public String getCredit() {
        return credit;
    }
}