package team_10.client.account;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Vector;

/**
 * Loan Account Type. Add transactions of absolute interest
 * rate or change in principal. Interest compounds daily.
 */
public class Loan extends Account {

    public Loan() {}

    /**
     * Overloaded addTransaction method for Loan.
     * @param d Date of transaction.
     * @param value Value of transaction, aka change of principal value. (negative or positive)
     * @param interestRate Interest rate at the time of the transaction.
     */
    public void addTransaction(LocalDate d, double value, double interestRate)
    {
        Transaction t = new Transaction(value, interestRate);
        transactions.put(d, t);
    }

    /**
     * Get the value of this Loan at a specified date.
     * @param d LocalDate which to calculate value.
     * @return Value at LocalDate <code>d</code>
     */
    public double getValue(LocalDate d) {
        Vector<LocalDate> transaction_dates = new Vector<LocalDate>(transactions.keySet());

        double total = 0;

        if (transaction_dates.size() <= 0) {
            throw new IllegalStateException("No transactions for this account.");
        } else {
            for (int i = 0; i < transaction_dates.size(); i++) {

                LocalDate fromDate = transaction_dates.get(i);
                LocalDate toDate;

                if ((i+1) >= transaction_dates.size() || transaction_dates.get(i + 1).isAfter(d)) {
                    toDate = d;
                    i = Integer.MAX_VALUE - 1; // Stop calculating if date d is before last transaction
                } else {
                    toDate = transaction_dates.get(i + 1);
                }

                //A = P(1 + r/n)^nt -> Daily Compound Interest
                double principle = total + ((Transaction) transactions.get(fromDate)).getValue();
                double rate = ((Transaction) transactions.get(fromDate)).getInterestRate();
                long n = fromDate.until(toDate, ChronoUnit.DAYS); //number of compounding periods (DAYS) per unit t
                double t = n / (double) fromDate.lengthOfYear();

                total = principle * Math.pow(1 + (rate / n), (n * t));
            }
        }

        return (double)Math.round(total * 100d) / 100d; // round to nearest cent
    }






    /**
     * Loan specific Transaction object.
     */
    private class Transaction extends team_10.client.account.Transaction {

        double interestRate;

        Transaction(double value, double interestRate)
        {
            this.value = value;
            this.interestRate = interestRate;
        }

        public double getInterestRate() { return this.interestRate; }
        public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    }
}