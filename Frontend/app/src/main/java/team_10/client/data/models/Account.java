package team_10.client.data.models;

import android.content.Context;
import android.view.View;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.TreeMap;

import team_10.client.data.UserInputField;

/**
 * Abstract Account class for all account types. All transactions for all types that extend
 * this class are put in the transactions TreeMap.
 */
public abstract class Account implements Serializable {
    @UserInputField(
            priority = 0,
            name = "Label",
            inputType = String.class
    )
    protected String label;

    protected String accountID;
    protected int isActive;
    protected TreeMap<LocalDate, team_10.client.data.models.Transaction> transactions;

    public Account() {
        this.transactions = new TreeMap<>();
    }
    
    public void addTransaction(LocalDate d, double value, int recurring) {
        addTransaction(d, value, transactions.size(), recurring);
    }

    public void addTransaction(LocalDate d, double value, int transactionID, int recurring) {
        Transaction t = new Transaction(value, transactionID, recurring, d);
        t.setAccount(this);
        transactions.put(d, t);
    }

    public Transaction removeTransaction(LocalDate d) {
        return (Transaction) transactions.remove(d);
    }

    public void setTransactions(TreeMap tempTransactions) { this.transactions = tempTransactions; }

    public TreeMap getTransactions() { return this.transactions; }

    public String getID() {
        return this.accountID;
    }

    public void setID(String accountID) {
        this.accountID = accountID;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int isActive() { return this.isActive; }

    public void setIsActive(int isActive) { this.isActive = isActive; }

    public abstract double getValue(LocalDate d);

    public abstract View getView(Context context);


    private class Transaction extends team_10.client.data.models.Transaction {
        private Transaction(double value, int transactionID, int recurring, LocalDate date) {
            super(value, transactionID, recurring, date);
        }
    }
}

