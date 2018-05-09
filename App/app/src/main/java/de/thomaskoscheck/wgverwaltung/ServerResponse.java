package de.thomaskoscheck.wgverwaltung;

public class ServerResponse {
    private String credit;
    private String newestAppVersion;
    private Expense[] expenses;

    public ServerResponse(String credit, String newestAppVersion, Expense[] expenses) {
        this.credit = credit;
        this.newestAppVersion = newestAppVersion;
        this.expenses = expenses;
    }

    public String getCredit() {
        return credit;
    }

    public String getNewestAppVersion() {
        return newestAppVersion;
    }

    public Expense[] getExpenses() {
        return expenses;
    }
}