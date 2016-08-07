package kimbugwe;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty fname, lname, course, regdate, intake, code, regno;

    public Student(int id, String fname, String lname, String course, String code, String intake, String regno, String regdate) {
        this.id = new SimpleIntegerProperty(id);
        this.fname = new SimpleStringProperty(fname);
        this.lname = new SimpleStringProperty(lname);
        this.course = new SimpleStringProperty(course);
        this.regdate = new SimpleStringProperty(regdate);
        this.intake = new SimpleStringProperty(intake);
        this.code = new SimpleStringProperty(code);
        this.regno = new SimpleStringProperty(regno);
    }
//  Getter methods

    public int getId() {
        return id.get();
    }

    public String getFname() {
        return fname.get();
    }

    public String getLname() {
        return lname.get();
    }

    public String getCourse() {
        return course.get();
    }

    public String getRegdate() {
        return regdate.get();
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
    public void setId(int id) {
        this.id.set(id);
    }

    public void setFname(String fname) {
        this.fname.set(fname);
    }

    public void setLname(String lname) {
        this.lname.set(lname);
    }

    public void setCourse(String course) {
        this.course.set(course);
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

    public void setRegdate(String regdate) {
        this.regdate.set(regdate);
    }
}
