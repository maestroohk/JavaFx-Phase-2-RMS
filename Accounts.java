package kimbugwe;

import javafx.beans.property.SimpleStringProperty;

public class Accounts {
    private final SimpleStringProperty fname, lname, regno, fultuition, paid, balance;

    public Accounts(String fname, String lname, String regno, String fultuition, String paid, String balance) {
        this.fname = new SimpleStringProperty(fname);
        this.lname = new SimpleStringProperty(lname);
        this.regno = new SimpleStringProperty(regno);
        this.fultuition = new SimpleStringProperty(fultuition);
        this.paid = new SimpleStringProperty(paid);
        this.balance = new SimpleStringProperty(balance);
    }    
    

    public SimpleStringProperty getFname() {
        return this.fname;
    }

    public void setFname(String fname) {
        this.fname.setValue(fname);
    }

    public SimpleStringProperty getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname.setValue(lname);
    }

    public SimpleStringProperty getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno.setValue(regno);
    }

    public SimpleStringProperty getFultuition() {
        return fultuition;
    }

    public void setFultuition(String fultuition) {
        this.fultuition.setValue(fultuition);
    }

    public SimpleStringProperty getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid.setValue(paid);
    }

    public SimpleStringProperty getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance.setValue(balance);
    }
    
    
}
