package kimbugwe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Accountant {

    Connection conn = ConnectDb.establishConnection();
    PreparedStatement pst;
    ResultSet rs;

    TableView<StudentAct> table = new TableView<>();
    final ObservableList<StudentAct> data = FXCollections.observableArrayList();

    TextField fultuition, paid;
    TextField lastname, actAmt, paidAmt, baln;
    Button deposit;
    String sessionRegno, sessionTuition, sessionPrevBal;

    Label user, logout;

    public Scene accountant() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20, 20, 20, 20));
        Scene accountant = new Scene(root);

        root.setRight(monetarySection());
        root.setLeft(generateStudentTable());
        root.setBottom(viewMoreDetails());

        return accountant;
    }

    private VBox generateStudentTable() {
        VBox generateStudentTable = new VBox();

        TableColumn col3 = new TableColumn("Lastname");
        col3.setMinWidth(100);
        col3.setCellValueFactory(new PropertyValueFactory<>("lname"));

        TableColumn col5 = new TableColumn("Code");
        col5.setMinWidth(100);
        col5.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn col6 = new TableColumn("Intake");
        col6.setMinWidth(100);
        col6.setCellValueFactory(new PropertyValueFactory<>("intake"));

        TableColumn col7 = new TableColumn("Registration number");
        col7.setMinWidth(150);
        col7.setCellValueFactory(new PropertyValueFactory<>("regno"));
        table.getColumns().addAll(col3, col5, col6, col7);

        generateStudentTable.getChildren().addAll(table);

        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                data.clear();
                try {
                    String qry = "select lname, code, intake,regno from student";
                    pst = conn.prepareStatement(qry);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        data.add(new StudentAct(
                                rs.getString("lname"),
                                rs.getString("code"),
                                rs.getString("intake"),
                                rs.getString("regno")
                        ));
                        table.setItems(data);
                        table.setEditable(true);
                    }
                    pst.close();
                    rs.close();

                } catch (Exception x) {
                    x.printStackTrace();
                }
            }

//            fetch monetary records
            String qry = "select student.regno, lname, fultuition, paid, balance from account, student where account.regno = student.regno and student.regno = ?";
            table.getSelectionModel().selectedItemProperty().addListener((obs, olv, nvl) -> {
                StudentAct student = table.getSelectionModel().getSelectedItem();
                if (table.getSelectionModel().getSelectedItem() != null) {
                    try {
                        pst = conn.prepareStatement(qry);
                        pst.setString(1, student.getRegno());
                        rs = pst.executeQuery();
                        while (rs.next()) {
                            lastname.setText(rs.getString("lname"));
                            actAmt.setText(rs.getString("fultuition"));
                            paidAmt.setText(rs.getString("paid"));
                            baln.setText(rs.getString("balance"));

                            sessionRegno = rs.getString("regno");
                            sessionTuition = rs.getString("fultuition");
                            sessionPrevBal = rs.getString("paid");
                        }
                        pst.close();
                        rs.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

        });

        return generateStudentTable;
    }

    private VBox monetarySection() {
        VBox monetarySection = new VBox(15);
        monetarySection.setPadding(new Insets(20, 10, 10, 10));
        fultuition = new TextField();
        fultuition.setId("fultuition");
        fultuition.setPromptText("Full Tuition");
        fultuition.setEditable(false);

        Label alterTuition = new Label("Insert Tuition");
        alterTuition.setId("alterTuition");

        paid = new TextField();
        paid.setId("paid");
        paid.setPromptText("Paid amount");

        deposit = new Button("Deposit");
        deposit.setOnAction(e -> {
            String qry = "update account set paid = ?, balance = ? where regno = ?";
            try {
                pst = conn.prepareStatement(qry);
                double b = Double.parseDouble(paid.getText());
                double p = Double.parseDouble(sessionPrevBal);
                double sAmt = Double.parseDouble(sessionTuition);
                double amt = 0;
                if (b > sAmt) {
                    System.out.println("Refer to fees structure please");
                } else {
                    p += b;
                    double atBal = sAmt -= p; //gets the balance to be stored into the database
                    String amt2 = "" + amt;
                    String pp = "" + p; // gets the paid amount that it update into the database

                    pst.setString(1, pp); // appending actual paid value to table col in db.
                    pst.setString(2, "" + atBal); // appending actual balance to table col in db.
                    pst.setString(3, sessionRegno); // this regno acts as a reference for storing the paid value and amount
                    pst.executeUpdate();
                    pst.close();
                    paid.clear();
                    clearFields();
                }

            } catch (Exception x) {
                x.printStackTrace();
            }
        });

        user = new Label("Accountant: " + Login.sessionUsername);
        user.setId("user");

        logout = new Label("Logout");
        logout.setId("logout");
        logout.setOnMouseClicked(e -> Main.window.setScene(new Login().login()));

        monetarySection.getChildren().addAll(alterTuition, paid, deposit, user, logout);
        return monetarySection;
    }

    private VBox viewMoreDetails() {
        VBox viewMoreDetails = new VBox(10);

        Label more = new Label("More Details");
        more.setOnMouseClicked(e -> clearFields());

        HBox viewHorizontal = new HBox(10);
        lastname = new TextField();
        lastname.setPromptText("Lastname");
        lastname.setEditable(false);
        lastname.setMaxWidth(300);

        actAmt = new TextField();
        actAmt.setPromptText("Total Amount");
        actAmt.setEditable(false);
        actAmt.setMaxWidth(300);

        paidAmt = new TextField();
        paidAmt.setPromptText("Paid Amount");
        paidAmt.setEditable(false);
        paidAmt.setMaxWidth(300);

        baln = new TextField();
        baln.setPromptText("Balance");
        baln.setEditable(false);
        baln.setMaxWidth(300);

        viewHorizontal.getChildren().addAll(lastname, actAmt, paidAmt, baln);
        viewMoreDetails.getChildren().addAll(more, viewHorizontal);

        return viewMoreDetails;
    }

    private void clearFields() {
        lastname.clear();
        actAmt.clear();
        paidAmt.clear();
        baln.clear();
    }
}
