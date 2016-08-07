package kimbugwe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AddNewStudent {

    Connection conn = ConnectDb.establishConnection();
    PreparedStatement pst;
    ResultSet rs;

    TextField fname, lname, intake, regno;
    ComboBox course, code;
    DatePicker regdate;

    Button save, back;
    String sessionType = "";

    Label feedback;

    final ObservableList courseOptions = FXCollections.observableArrayList();
    final ObservableList courseCodeOptions = FXCollections.observableArrayList();

    public Scene addNewStudent() {
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        Scene addNewStudent = new Scene(vBox, 420, 500);
        addNewStudent.getStylesheets().add(getClass().getResource("addNewStudent.css").toExternalForm());

        System.out.println(sessionType);
        
        fname = new TextField();
        fname.setPromptText("Firstname");
        fname.setId("fname");
        fname.setMaxWidth(350);

        lname = new TextField();
        lname.setPromptText("Lastname");
        lname.setId("lname");
        lname.setMaxWidth(350);

        fillCourseComboBox();
        course = new ComboBox(courseOptions);
        course.setMaxWidth(350);
        course.setPromptText("Course");

        fillCourseCodeComboBox();
        code = new ComboBox(courseCodeOptions);
        code.setMaxWidth(350);
        code.setPromptText("Code");

        intake = new TextField();
        intake.setPromptText("Intake");
        intake.setId("intake");
        intake.setMaxWidth(350);

        regno = new TextField();
        regno.setPromptText("Registration #");
        regno.setId("regno");
        regno.setMaxWidth(350);

        regdate = new DatePicker();
        regdate.setPromptText("Registration date");
        regdate.setMaxWidth(350);

        HBox bottom = new HBox(270);
        save = new Button("Save");
        save.setOnAction(e -> checkFields());

        back = new Button("Back");
        back.setOnAction(e -> Main.window.setScene(new ManageStudent().manageStudent()));

        bottom.getChildren().addAll(save);

        feedback = new Label();

        vBox.getChildren().addAll(fname, lname, course, code, intake, regno, regdate, bottom, feedback);
        return addNewStudent;
    }

    private void fillCourseComboBox() {
        try {
            String qry = "select cname from courses";
            pst = conn.prepareStatement(qry);
            rs = pst.executeQuery();
            while (rs.next()) {
                courseOptions.add(rs.getString("cname"));
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
        }

    }

    private void fillCourseCodeComboBox() {
        try {
            String qry = "select code, type from courses";
            pst = conn.prepareStatement(qry);
            rs = pst.executeQuery();
            while (rs.next()) {
                courseCodeOptions.add(rs.getString("code"));
                sessionType = rs.getString("type");
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
        }

    }

    private void saveIntoDb() {
        try {
            String qry3 = "insert into student (fname, lname, course, code, intake, regno, regdate) "
                    + "values (?,?,?,?,?,?,?); "
                    + "insert into account (regno, fultuition, paid, balance) "
                    + "values (?,?,?,?)";
            pst = conn.prepareStatement(qry3);
            pst.setString(1, fname.getText());
            pst.setString(2, lname.getText());
            pst.setString(3, ((String) course.getSelectionModel().getSelectedItem()));
            pst.setString(4, ((String) code.getSelectionModel().getSelectedItem()));
            pst.setString(5, intake.getText().toLowerCase());
            pst.setString(6, regno.getText());
            pst.setString(7, ((TextField) regdate.getEditor()).getText());
            pst.setString(8, regno.getText());
            evaluateTuition();

            pst.execute();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearInsertFields();
        feedback.setText("Student Added Successfully");
        String style = "-fx-text-fill: green";
        feedback.setStyle(style);
    }

    private void evaluateTuition() {
        try {
            if ("bsc.cs".equals((String) code.getSelectionModel().getSelectedItem())
                    || "bist".equals(((String) code.getSelectionModel().getSelectedItem()))
                    || "bogm".equals(((String) code.getSelectionModel().getSelectedItem()))
                    || "bsc.qe".equals(((String) code.getSelectionModel().getSelectedItem()))) {
                pst.setString(9, "1450000");
                pst.setString(10, "0");
                pst.setString(11, "0");
            }else{
                
            }
        } catch (Exception e) {
        }
    }

    private void clearInsertFields() {
        fname.clear();
        lname.clear();
        course.setValue(null);
        code.setValue(null);
        intake.clear();
        regno.clear();
        regdate.setValue(null);
    }

    private void checkFields() {
        if (fname.getText().isEmpty()
                || lname.getText().isEmpty()
                || intake.getText().isEmpty()
                || regno.getText().isEmpty()
                || ((String) course.getSelectionModel().getSelectedItem()).isEmpty()
                || ((String) code.getSelectionModel().getSelectedItem()).isEmpty()
                || (((TextField) regdate.getEditor()).getText()).isEmpty()) {
            feedback.setText("Some Fields were left unattended to");
            String style = "-fx-text-fill: red";
            feedback.setStyle(style);
        } else {
            saveIntoDb();
        }
    }
}
