package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ConfirmationDialog extends Application {
	private String confirmationMessage;
	private boolean confirmationResult;
	private int done = -1;

	/**
	 * Default constructor initializes with a default confirmation message.
	 */
	public ConfirmationDialog() {
		this.confirmationMessage = "Are you sure?";
		this.confirmationResult = false;
	}

	/**
	 * Constructor initializes with a custom confirmation message.
	 * @param confirmationMessage the custom confirmation message.
	 */
	public ConfirmationDialog(String confirmationMessage) {
		this.confirmationMessage = confirmationMessage;
		this.confirmationResult = false;
	}

	/**
	 * Sets the confirmation message to be displayed in the dialog.
	 * @param confirmationMessage the confirmation message to set.
	 */
	public void setConfirmationMessage(String confirmationMessage) {
		this.confirmationMessage = confirmationMessage;
	}

	/**
	 * Gets the result of the confirmation (true if confirmed, false otherwise).
	 * @return the confirmation result.
	 */
	public boolean getConfirmationResult() {
		return confirmationResult;
	}

	/**
	 * Starts the JavaFX application and displays the confirmation dialog.
	 * @param primaryStage the primary stage for this application.
	 */
	@Override
	public void start(Stage primaryStage) {
		Scene scene = showConfirmation(primaryStage);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Delete Confirmation.");
		primaryStage.showAndWait(); // Use showAndWait to wait for user response
	}

	// Buttons for user response
	Button yesButton = new Button("Yes");
	Button noButton = new Button("No");

	/**
	 * Creates and returns the confirmation dialog scene.
	 * @param primaryStage the stage on which the dialog is displayed.
	 * @return the scene containing the confirmation dialog.
	 */
	private Scene showConfirmation(Stage primaryStage) {
		BorderPane bp = new BorderPane();
		bp.setStyle("-fx-background-color:#F0DFD0;");

		// Label displaying the confirmation message
		Label messageLabel = new Label(confirmationMessage);
		messageLabel.setStyle("-fx-text-fill:black;-fx-font-weight: bold;");
		messageLabel.setAlignment(Pos.TOP_CENTER);

		// Set action for the Yes button
		yesButton.setOnAction(e -> {
			confirmationResult = true;
			setDone(1);
			primaryStage.hide();
		});
		yesButton.setStyle("-fx-background-color: #B9D1BF;-fx-text-fill:white;-fx-font-weight: bold;");

		// Set action for the No button
		noButton.setStyle("-fx-background-color: #FF2E2E;-fx-text-fill:white;-fx-font-weight: bold;");
		noButton.setOnAction(e -> {
			confirmationResult = false;
			setDone(0);
			primaryStage.hide();
		});

		// HBox to hold the buttons
		HBox buttonBox = new HBox(10);
		buttonBox.getChildren().addAll(yesButton, noButton);
		buttonBox.setAlignment(Pos.CENTER);

		// Set the label and button box in the BorderPane
		bp.setTop(messageLabel);
		bp.setCenter(buttonBox);
		bp.setAlignment(buttonBox, Pos.CENTER);

		return new Scene(bp, 300, 100);
	}

	/**
	 * Sets the done status after the user makes a choice.
	 * @param i the status to set (1 for yes, 0 for no).
	 */
	private void setDone(int i) {
		done = i;
	}

	/**
	 * Main method to launch the application.
	 * @param args command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Gets the done status indicating the user's choice.
	 * @return the done status.
	 */
	public int getDone() {
		return done;
	}
}