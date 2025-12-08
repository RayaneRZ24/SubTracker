package com.emsi.subtracker.views;

import com.emsi.subtracker.models.Abonnement;
import com.emsi.subtracker.services.SubscriptionService;
import com.emsi.subtracker.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Contrôleur du formulaire d'ajout d'abonnement.
 */
public class AddSubscriptionController implements Initializable {

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrix;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private ComboBox<String> cmbFrequence;
    @FXML
    private ComboBox<String> cmbCategorie;

    private final SubscriptionService service = new SubscriptionService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des ComboBox
        cmbFrequence.getItems().addAll("Mensuel", "Annuel");
        cmbFrequence.getSelectionModel().selectFirst();

        cmbCategorie.getItems().addAll("Divertissement", "Travail", "Sport", "Musique", "Autre");
        cmbCategorie.getSelectionModel().select("Autre");

        // Date par défaut = aujourd'hui
        dateDebut.setValue(LocalDate.now());
    }

    @FXML
    protected void onBtnEnregistrerClick() {
        if (validerChamps()) {
            try {
                // 1. Récupération des valeurs
                String nom = txtNom.getText();
                double prix = Double.parseDouble(txtPrix.getText());
                LocalDate date = dateDebut.getValue();
                String frequence = cmbFrequence.getValue();
                String categorie = cmbCategorie.getValue();

                // Génération d'un ID (timestamp simple pour l'exemple)
                int id = (int) (System.currentTimeMillis() % 100000);

                // 2. Création et sauvegarde
                Abonnement nouvelAbonnement = new Abonnement(id, nom, prix, date, frequence, categorie);
                service.add(nouvelAbonnement);

                // 3. Retour au Dashboard
                retourAuDashboard();

            } catch (NumberFormatException e) {
                afficherErreur("Le prix doit être un nombre valide.");
            }
        }
    }

    @FXML
    protected void onBtnAnnulerClick() {
        retourAuDashboard();
    }

    private void retourAuDashboard() {
        Stage currentStage = (Stage) txtNom.getScene().getWindow();
        SceneManager.changeScene(currentStage, "dashboard.fxml");
    }

    private boolean validerChamps() {
        if (txtNom.getText() == null || txtNom.getText().trim().isEmpty()) {
            afficherErreur("Le nom est obligatoire.");
            return false;
        }
        if (txtPrix.getText() == null || txtPrix.getText().trim().isEmpty()) {
            afficherErreur("Le prix est obligatoire.");
            return false;
        }
        if (dateDebut.getValue() == null) {
            afficherErreur("La date est obligatoire.");
            return false;
        }
        return true;
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.show();
    }
}
