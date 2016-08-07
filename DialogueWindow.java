package kimbugwe;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class DialogueWindow {

    public Label feedback;

    Scene dialogueWindow() {
        Pane root = new Pane();
        Scene dialogueWindow = new Scene(root, 280, 300);
        feedback = new Label();
        feedback.setText("Start in "+(Login.j));
        root.getChildren().add(feedback);

        return dialogueWindow;
    }
}
