package com.emsi.subtracker;

import com.emsi.subtracker.models.Abonnement;
import com.emsi.subtracker.services.SubscriptionService;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Random;

/**
 * Interface graphique temporaire pour tester le Service et le CSV sans FXML.
 */
public class TemporaryGui extends Application {

    private final SubscriptionService service = new SubscriptionService();
    private final TableView<Abonnement> table = new TableView<>();
    private final Label lblTotal = new Label("Total Mensuel: 0.00 DH");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Subscription Tracker - TEST UI");

        // 1. Configuration de la TableView
        TableColumn<Abonnement, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));

        TableColumn<Abonnement, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));

        TableColumn<Abonnement, Double> colPrix = new TableColumn<>("Prix");
        colPrix.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrix()));

        TableColumn<Abonnement, String> colFreq = new TableColumn<>("Fréquence");
        colFreq.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFrequence()));

        TableColumn<Abonnement, String> colCat = new TableColumn<>("Catégorie");
        colCat.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategorie()));

        table.getColumns().addAll(colId, colNom, colPrix, colFreq, colCat);

        // 2. Boutons d'action
        Button btnAdd = new Button("Ajouter Random");
        btnAdd.setOnAction(e -> ajouterRandom());

        Button btnDelete = new Button("Supprimer Sélection");
        btnDelete.setOnAction(e -> supprimerSelection());

        Button btnRefresh = new Button("Rafraîchir");
        btnRefresh.setOnAction(e -> rafraichirDonnees());

        HBox buttonBar = new HBox(10, btnAdd, btnDelete, btnRefresh);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10));

        // 3. Layout principal
        VBox topPane = new VBox(10, new Label("Gestion Abonnements (Test Mode)"), lblTotal);
        topPane.setAlignment(Pos.CENTER);
        topPane.setPadding(new Insets(10));
        topPane.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        BorderPane root = new BorderPane();
        root.setTop(topPane);
        root.setCenter(table);
        root.setBottom(buttonBar);

        // Chargement initial
        rafraichirDonnees();

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void rafraichirDonnees() {
        table.setItems(FXCollections.observableArrayList(service.getAll()));
        double total = service.calculerTotalMensuel();
        lblTotal.setText("Total Mensuel: " + total + " DH");
    }

    private void ajouterRandom() {
        Random rand = new Random();
        int id = (int) (System.currentTimeMillis() % 10000);
        String[] apps = { "Netflix", "Spotify", "Adobe", "Gym", "Internet", "Phone" };
        String nom = apps[rand.nextInt(apps.length)] + " " + rand.nextInt(100);
        double prix = 10 + rand.nextInt(100);
        String freq = rand.nextBoolean() ? "Mensuel" : "Annuel";

        Abonnement sub = new Abonnement(id, nom, prix, LocalDate.now(), freq, "Autre");
        service.add(sub);
        rafraichirDonnees();
    }

    private void supprimerSelection() {
        Abonnement selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.remove(selected.getId());
            rafraichirDonnees();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une ligne.");
            alert.show();
        }
    }
}
