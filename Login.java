package kimbugwe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Login {

    Connection conn = ConnectDb.establishConnection();
    PreparedStatement pst;
    ResultSet rs;

    Label title, feedback;
    TextField username;
    PasswordField password;
    Button signIn;

    public static String sessionUsername = "";
    private static final int MAX_TRIALS = 1;
    private static int trials = 0;
    public static int i;
    public static int j;

    public Scene login() {
        BorderPane root = new BorderPane();
        Scene login = new Scene(root, 280, 300);
        login.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
        title = new Label();
        title.setText("Records Management System");
        title.setId("title");

        username = new TextField();
        username.setId("username");
        username.setPromptText("Username");
        username.setOnKeyPressed(e -> feedback.setText(""));
        Tooltip tp = new Tooltip();
        tp.setText("Required Field");
        username.setTooltip(tp);

        password = new PasswordField();
        password.setId("password");
        password.setPromptText("Password");
        password.setTooltip(tp);
        password.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                authenticateLogin();
            }
        });

        signIn = new Button("Login");
        signIn.setId("signIn");
        signIn.setOnAction(e -> authenticateLogin());

        feedback = new Label();

        VBox middle = new VBox(20);
        middle.setPadding(new Insets(10, 10, 10, 10));
        middle.getChildren().addAll(title, username, password, signIn, feedback);

        root.setCenter(middle);

        return login;
    }

    private void authenticateLogin() {
        try {
            String qry = "SELECT * FROM admin WHERE username = ? AND password = ?";
            pst = conn.prepareStatement(qry);
            pst.setString(1, username.getText());
            pst.setString(2, password.getText());

            rs = pst.executeQuery();
            if (rs.next()) {
                trials = 0;
                sessionUsername = rs.getString("username");
                if ("admin".equals(rs.getString("actType"))) {
                    Main.window.setScene(new ManageStudent().manageStudent());
                } else {
                    Main.window.setScene(new Accountant().accountant());
                }
                feedback.setText("Login successful");
            } else {
                trials++;
                String style = "-fx-text-fill: red; -fx-font-size: 14px";
                feedback.setText("Login failed");
                feedback.setStyle(style);
                clearLoginFields();
                if (trials == MAX_TRIALS) {
                    Platform.exit();
                    /** 
                     * instead of the Platform.exit() function,
                     * deactivate the input fields for sometime
                     * add a timer to update the feedback label with a value in seconds.
                     * re-activate the fields
                     **/
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void deactivateFields() {
        username.setEditable(false);
        password.setEditable(false);
    }

    private void activateFields() {
        username.setEditable(true);
        password.setEditable(true);
    }

    private void clearLoginFields() {
        username.clear();
        password.clear();
    }

}
