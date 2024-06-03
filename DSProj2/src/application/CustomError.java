package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Class representing a custom error message window
public class CustomError extends Application {
    private String errorMessage; // Error message to display

    // Setter for error message
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // Getter for error message
    public String getErrorMessage() {
        return errorMessage;
    }

    // Default constructor
    public CustomError() {
        errorMessage = "An Error occurred."; // Default error message
    }

    // Constructor with custom error message
    public CustomError(String string) {
        errorMessage = string;
    }

    // Method to start the JavaFX application
    @Override
    public void start(Stage primaryStage) {
        Scene scene = showError(primaryStage); // Create and display the error message scene
        primaryStage.setTitle("!! Error Message"); // Set the title of the window
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to create and display the error message scene
    private Scene showError(Stage primaryStage) {
        BorderPane bp = new BorderPane(); // Main layout container
        ImageView errorPic = new ImageView("file:error.jpg"); // Error image
        Label error = new Label(errorMessage); // Label to display the error message
        HBox messageHB = new HBox(10); // Horizontal box to hold the error message and image
        messageHB.getChildren().addAll(errorPic, error); // Add image and error message to the HBox
        errorPic.setFitHeight(20);
        errorPic.setFitWidth(30);
        Button okB = new Button("OK"); // OK button to close the window

        // Action event for OK button to hide the window
        okB.setOnAction(e -> {
            primaryStage.hide();
        });
        // Styling for OK button
        okB.setStyle("-fx-background-color: #FF2E2E;-fx-text-fill:white;-fx-font-weight: bold;");
        // Styling for error label
        error.setStyle("-fx-font-family:courier; -fx-font-size: 14px; -fx-text-fill:black;-fx-font-weight: bold;");
        bp.setTop(messageHB); // Set the HBox containing error message and image to the top of the BorderPane
        bp.setBottom(okB); // Set the OK button to the bottom of the BorderPane
        bp.setAlignment(okB, Pos.BOTTOM_RIGHT); // Align the OK button to the bottom-right corner

        // Create and return the scene with the BorderPane as the root and adjust its width based on the error message length
        return new Scene(bp, messageHB.getMaxWidth(), 80);
    }

    // Main method to launch the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}
