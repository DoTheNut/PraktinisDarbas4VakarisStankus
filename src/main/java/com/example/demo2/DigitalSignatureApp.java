package com.example.demo2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigitalSignatureApp extends Application {

    private TextField inputField1;
    private TextField signatureField;
    private TextField inputField3;
    private Label resultLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Skaitmeninis parašas");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Pirmas laukelis
        Label label1 = new Label("Įveskite tekstą:");
        GridPane.setConstraints(label1, 0, 0);
        inputField1 = new TextField();
        GridPane.setConstraints(inputField1, 1, 0);
        Button generateButton = new Button("Generuoti parašą");
        GridPane.setConstraints(generateButton, 1, 1);
        generateButton.setOnAction(e -> updateSignature());

        // Antras laukelis
        Label label2 = new Label("Parašas:");
        GridPane.setConstraints(label2, 0, 2);
        signatureField = new TextField();
        GridPane.setConstraints(signatureField, 1, 2);

        // Trečias laukelis
        Label label3 = new Label("Įveskite tekstą patikrinimui:");
        GridPane.setConstraints(label3, 0, 3);
        inputField3 = new TextField();
        GridPane.setConstraints(inputField3, 1, 3);
        Button checkButton = new Button("Patikrinti parašą");
        GridPane.setConstraints(checkButton, 1, 4);
        checkButton.setOnAction(e -> checkSignature());

        // Rezultato etiketė
        resultLabel = new Label("");
        GridPane.setConstraints(resultLabel, 1, 5);

        grid.getChildren().addAll(label1, inputField1, generateButton, label2, signatureField, label3, inputField3, checkButton, resultLabel);

        Scene scene = new Scene(grid, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateSignature() {
        String text = inputField1.getText();
        String signature = generateSignature(text);
        signatureField.setText(signature);
    }

    private void checkSignature() {
        String newText = inputField3.getText();
        String providedSignature = signatureField.getText();
        String newSignature = generateSignature(newText);

        if (providedSignature.equals(newSignature)) {
            resultLabel.setText("Parašas patvirtintas!");
            resultLabel.setStyle("-fx-text-fill: green;");
        } else {
            resultLabel.setText("Parašas nepatvirtintas!");
            resultLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private String generateSignature(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
