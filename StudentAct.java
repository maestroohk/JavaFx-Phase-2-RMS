package kimbugwe;

import javafx.beans.property.SimpleStringProperty;

public class StudentAct {

    private final SimpleStringProperty lname, intake, code, regno;

    public StudentAct(String lname, String code, String intake, String regno) {
        this.lname = new SimpleStringProperty(lname);
        this.intake = new SimpleStringProperty(intake);
        this.code = new SimpleStringProperty(code);
        this.regno = new SimpleStringProperty(regno);
    }
//  Getter methods

    public String getLname() {
        return lname.get();
    }

    public String getIntake() {
        return intake.get();
    }

    public String getCode() {
        return code.get();
    }

    public String getRegno() {
        return regno.get();
    }

//  Setter methods
    public void setLname(String lname) {
        this.lname.set(lname);
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public void setIntake(String intake) {
        this.intake.set(intake);
    }

    public void setRegno(String regno) {
        this.regno.set(regno);
    }

}
