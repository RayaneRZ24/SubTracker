package com.emsi.subtracker.views;

import com.emsi.subtracker.services.UserService;
import com.emsi.subtracker.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button registerButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Label successLabel;

    private final UserService userService;

    public RegisterController() {
        this.userService = new UserService();
    }

    @FXML
    private void handleRegister() {
        errorLabel.setText("");
        successLabel.setText("");

        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Les mots de passe ne correspondent pas.");
            return;
        }

        if (!isValidEmail(email)) {
            errorLabel.setText("Format d'email invalide.");
            return;
        }

        try {
            userService.register(username, email, password);
            successLabel.setText("Compte créé avec succès ! Connectez-vous.");
            // Optional: Auto redirect to login after short delay
        } catch (Exception e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    @FXML
    private void goToLogin() {
        try {
            SceneManager.switchScene((Stage) registerButton.getScene().getWindow(), "login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Erreur de navigation.");
        }
    }
}
