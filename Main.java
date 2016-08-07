package kimbugwe;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application{
    public static Stage window, wrongLogin, regWindow;
    @Override
    public void start(Stage stage){
        Image img = new Image(getClass().getResourceAsStream("lib.png"));
                
        Main.window = stage;
        Main.window.setResizable(false);
        Main.window.getIcons().add(img);
        Main.window.setTitle("Records System");
        Main.window.setScene(new Login().login());
        Main.window.show();
        
        Main.wrongLogin = new Stage();
        Main.wrongLogin.initModality(Modality.APPLICATION_MODAL);
//        Main.wrongLogin.initStyle(StageStyle.TRANSPARENT);
        
        Main.regWindow = new Stage();
//        Main.regWindow.initStyle(StageStyle.UNDECORATED);
        Main.regWindow.initModality(Modality.APPLICATION_MODAL);
        Main.regWindow.setTitle("Add New Student");
    }
   
    public static void main(String args[]){
        launch(args);
    }
}
