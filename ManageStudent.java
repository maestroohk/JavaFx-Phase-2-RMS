package kimbugwe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManageStudent {

    Connection conn = ConnectDb.establishConnection();
    PreparedStatement pst;
    ResultSet rs;

    Button addStudent, editStudent, logout;
    TextField registrationNo, fulltuit, paidamount, bal;

    Label status;
    String sessionFultution = "", sessionPaid = "";

    TableView<Student> table = new TableView<>();
    final ObservableList<Student> data = FXCollections.observableArrayList();

    public Scene manageStudent() {
        BorderPane root = new BorderPane();
        Scene manageStudent = new Scene(root, 1100, 600);
        manageStudent.getStylesheets().add(getClass().getResource("ManageStudent.css").toExternalForm());

        root.setTop(menuBar());

        VBox middle = new VBox(10);
        HBox middleHorizontal = new HBox(100);
        middleHorizontal.getChildren().addAll(infor(), controls());
        middle.getChildren().addAll(generateStudentTable(), middleHorizontal);

        root.setCenter(middle);
        return manageStudent;
    }

    private MenuBar menuBar() {
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("_File");
        file.setMnemonicParsing(true);
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> Platform.exit());
        file.getItems().addAll(exit);

        Menu profile = new Menu("_Profile");
        profile.setMnemonicParsing(true);
        MenuItem logout = new MenuItem("Logout");
        logout.setOnAction(e -> Main.window.setScene(new Login().login()));
        MenuItem restApp = new MenuItem("Restart");
        restApp.setOnAction(__ -> {
            Main.window.close();
            Platform.runLater( () -> new Main().start(new Stage()) );
        });  
        profile.getItems().addAll(logout, restApp);

        menuBar.getMenus().addAll(file, profile);
        return menuBar;
    }

    private VBox generateStudentTable() {
        VBox generateStudentTable = new VBox();

        TableColumn col1 = new TableColumn("Id");
        col1.setMinWidth(100);
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn col2 = new TableColumn("Firstname");
        col2.setMinWidth(120);
        col2.setCellValueFactory(new PropertyValueFactory<>("fname"));

        TableColumn col3 = new TableColumn("Lastname");
        col3.setMinWidth(100);
        col3.setCellValueFactory(new PropertyValueFactory<>("lname"));

        TableColumn col4 = new TableColumn("Course");
        col4.setMinWidth(300);
        col4.setCellValueFactory(new PropertyValueFactory<>("course"));

        TableColumn col5 = new TableColumn("Code");
        col5.setMinWidth(100);
        col5.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn col6 = new TableColumn("Intake");
        col6.setMinWidth(100);
        col6.setCellValueFactory(new PropertyValueFactory<>("intake"));

        TableColumn col7 = new TableColumn("Registration number");
        col7.setMinWidth(150);
        col7.setCellValueFactory(new PropertyValueFactory<>("regno"));

        TableColumn col8 = new TableColumn("Registration date");
        col8.setMinWidth(150);
        col8.setCellValueFactory(new PropertyValueFactory<>("regdate"));

        table.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8);

        generateStudentTable.getChildren().addAll(table);

        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                data.clear();
                try {
                    String qry = "select * from student";
                    pst = conn.prepareStatement(qry);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        data.add(new Student(
                                rs.getInt("id"),
                                rs.getString("fname"),
                                rs.getString("lname"),
                                rs.getString("course"),
                                rs.getString("code"),
                                rs.getString("intake"),
                                rs.getString("regno"),
                                rs.getString("regdate")
                        ));
                        table.setItems(data);
                        table.setEditable(true);
                    }
                    pst.close();
                    rs.close();

                } catch (Exception x) {
                    x.printStackTrace();
                }

//                fetch accounts details
                String qry = "select account.regno, fultuition, paid, balance from account, student where account.regno = student.regno and student.regno = ?";

                table.getSelectionModel().selectedItemProperty().addListener((obs, olv, nvl) -> {
                    Student student = table.getSelectionModel().getSelectedItem();
                    if (table.getSelectionModel().getSelectedItem() != null) {
                        try {
                            pst = conn.prepareStatement(qry);
                            pst.setString(1, student.getRegno());
                            rs = pst.executeQuery();
                            while (rs.next()) {
                                registrationNo.setText(rs.getString("regno"));
                                fulltuit.setText(rs.getString("fultuition"));
                                paidamount.setText(rs.getString("paid"));
                                bal.setText(rs.getString("balance"));

                                sessionFultution = rs.getString("fultuition");
                                sessionPaid = rs.getString("paid");

                            }
                            pst.close();
                            rs.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    evaluateTuition();
                });

            }
        });

        return generateStudentTable;
    }

    private VBox controls() {

        VBox controls = new VBox(15);

//      search engine
        TextField search = new TextField();
        search.setPrefWidth(400);
        search.setPromptText("Search The Table, according to firstname, lastname etc...");

        FilteredList<Student> filteredData = new FilteredList<>(data, e -> true);
        search.setOnKeyPressed(e -> {
            search.textProperty().addListener((obsVl, ov, nv) -> {
                filteredData.setPredicate((Predicate<? super Student>) student -> {
                    if ((nv == null) || (nv.isEmpty())) {
                        return true;
                    }
                    String lowerCaseFilter = nv.toLowerCase();

                    if (student.getFname().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (student.getLname().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (student.getCourse().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (student.getCode().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (student.getIntake().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (student.getRegno().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (student.getRegdate().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });

            SortedList<Student> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);
        });

        HBox hboxHorizontal = new HBox(300);
        addStudent = new Button("Add Student");
//        addStudent.setOnAction(e -> Main.window.setScene(new AddNewStudent().addNewStudent()));
        addStudent.setOnAction(e -> {
            Main.regWindow.setScene(new AddNewStudent().addNewStudent());
            Main.regWindow.showAndWait();
        });

        editStudent = new Button("Edit Student");

        hboxHorizontal.getChildren().addAll(addStudent);

        logout = new Button("Logout");
        logout.setOnAction(e -> Main.window.setScene(new Login().login()));
        controls.getChildren().addAll(search, hboxHorizontal);
        return controls;
    }

    private VBox infor() {
        Label reg, totalAmount, paidAmt, baln;
        VBox infor = new VBox(10);
        infor.setPadding(new Insets(10, 10, 10, 10));

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 0));

        Label title = new Label("Accounts Info");
        title.setId("title");

        reg = new Label("Registration #");
        reg.setId("reg");
        GridPane.setConstraints(reg, 0, 0);
        registrationNo = new TextField();
        registrationNo.setEditable(false);
        registrationNo.setMaxWidth(300);
        registrationNo.setPromptText("Registration #");
        GridPane.setConstraints(registrationNo, 1, 0);

        totalAmount = new Label("Full tuition");
        totalAmount.setId("totalAmount");
        GridPane.setConstraints(totalAmount, 0, 1);
        fulltuit = new TextField();
        fulltuit.setEditable(false);
        fulltuit.setMaxWidth(300);
        fulltuit.setPromptText("Full tuition");
        GridPane.setConstraints(fulltuit, 1, 1);

        paidAmt = new Label("Paid amount");
        paidAmt.setId("paidAmt");
        GridPane.setConstraints(paidAmt, 0, 2);
        paidamount = new TextField();
        paidamount.setEditable(false);
        paidamount.setMaxWidth(300);
        paidamount.setPromptText("Paid amount");
        GridPane.setConstraints(paidamount, 1, 2);

        baln = new Label("Balance");
        baln.setId("baln");
        GridPane.setConstraints(baln, 0, 3);
        bal = new TextField();
        bal.setEditable(false);
        bal.setMaxWidth(300);
        bal.setPromptText("Balance");
        GridPane.setConstraints(bal, 1, 3);

        HBox hboxStatus = new HBox();
        status = new Label();

        hboxStatus.getChildren().add(status);
//        evaluateTuition();

        pane.getChildren().addAll(reg, registrationNo, totalAmount, fulltuit, paidAmt, paidamount, baln, bal);
        infor.getChildren().addAll(title, pane, hboxStatus);
        return infor;
    }

    private void evaluateTuition() {
        double evaluate = ((Double.parseDouble(sessionPaid)) / (Double.parseDouble(sessionFultution)) * 100);
        if (evaluate == 100) {
            status.setText("Permitted to sit for all exams");
            status.setStyle("-fx-text-fill: green");
        }else if(evaluate >=80 && evaluate <=99){
            status.setStyle("-fx-text-fill: #800000");
            status.setText("Allowed to sit both tests");
        }else if(evaluate >=60 && evaluate <=79){
            status.setStyle("-fx-text-fill: #800000");
            status.setText("Permitted to sit for only test 1");
        }else if(evaluate >=30 && evaluate <= 59){
            status.setStyle("-fx-text-fill: linear-gradient(#FF0000,#800000)");
            status.setText("Eligable to register");
        }else{
            status.setStyle("-fx-text-fill: #FF0000");
            status.setText("Please clear all dues to avoid inconviencies");
        }
    }

}
