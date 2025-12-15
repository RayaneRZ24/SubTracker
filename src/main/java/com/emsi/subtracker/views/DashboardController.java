package com.emsi.subtracker.views;

import com.emsi.subtracker.models.Abonnement;
import com.emsi.subtracker.services.SubscriptionService;
import com.emsi.subtracker.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur de l'écran principal (Dashboard) - Version Cartes Visuelles.
 */
public class DashboardController implements Initializable {

    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblTotalMensuel;

    @FXML
    private FlowPane cardsContainer;

    @FXML
    private TextField searchField;

    private final SubscriptionService service = new SubscriptionService();
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private List<Abonnement> allSubscriptions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("DashboardController initialization started...");

            // Init Search Listener
            if (searchField != null) {
                searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filterSubscriptions(newValue);
                });
            } else {
                System.err.println("WARNING: searchField is null in DashboardController");
            }

            refreshDashboard();
            System.out.println("DashboardController initialization completed.");
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR initializing DashboardController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshDashboard() {
        com.emsi.subtracker.models.User currentUser = com.emsi.subtracker.utils.UserSession.getInstance().getUser();
        if (currentUser != null && lblWelcome != null) {
            lblWelcome.setText("Bonjour, " + currentUser.getUsername());
        }

        // Fetch Data once
        this.allSubscriptions = service.getAll();

        // 1. Mise à jour du total
        double total = service.calculerTotalMensuel();

        // Currency Conversion
        String currency = com.emsi.subtracker.utils.UserSession.getInstance().getCurrency();
        double displayTotal = convertPrice(total, currency);
        String symbol = getCurrencySymbol(currency);

        lblTotalMensuel.setText(df.format(displayTotal) + " " + symbol);

        // 2. Initial Display (all or filtered by current text)
        String currentSearch = (searchField != null) ? searchField.getText() : "";
        filterSubscriptions(currentSearch);
    }

    private double convertPrice(double priceInDH, String targetCurrency) {
        switch (targetCurrency) {
            case "EUR":
                return priceInDH * 0.091; // Approx rate
            case "USD":
                return priceInDH * 0.099; // Approx rate
            default:
                return priceInDH;
        }
    }

    private String getCurrencySymbol(String currency) {
        switch (currency) {
            case "EUR":
                return "€";
            case "USD":
                return "$";
            default:
                return "DH";
        }
    }

    private void filterSubscriptions(String query) {
        if (allSubscriptions == null)
            return;

        cardsContainer.getChildren().clear();

        String lowerCaseQuery = (query != null) ? query.toLowerCase() : "";

        for (Abonnement sub : allSubscriptions) {
            if (lowerCaseQuery.isEmpty()
                    || (sub.getNom() != null && sub.getNom().toLowerCase().contains(lowerCaseQuery))) {
                VBox card = createSubscriptionCard(sub);
                cardsContainer.getChildren().add(card);
            }
        }
    }

    private VBox createSubscriptionCard(Abonnement sub) {
        VBox card = new VBox(10);
        card.getStyleClass().add("sub-card");

        // Determine Brand Color
        String name = sub.getNom() != null ? sub.getNom().toLowerCase() : "";
        if (name.contains("netflix")) {
            card.getStyleClass().add("brand-netflix");
        } else if (name.contains("spotify")) {
            card.getStyleClass().add("brand-spotify");
        } else if (name.contains("amazon") || name.contains("prime")) {
            card.getStyleClass().add("brand-amazon");
        } else if (name.contains("apple") || name.contains("icloud")) {
            card.getStyleClass().add("brand-apple");
        } else if (name.contains("adobe") || name.contains("creative")) {
            card.getStyleClass().add("brand-adobe");
        } else if (name.contains("basic") || name.contains("fit")) {
            card.getStyleClass().add("brand-basicfit");
        } else {
            card.getStyleClass().add("brand-default");
        }

        // Title
        Label lblName = new Label(sub.getNom());
        lblName.getStyleClass().add("sub-card-title");
        lblName.setWrapText(true);

        // Price
        String currency = com.emsi.subtracker.utils.UserSession.getInstance().getCurrency();
        double displayPrice = convertPrice(sub.getPrix(), currency);
        String symbol = getCurrencySymbol(currency);

        Label lblPrice = new Label(df.format(displayPrice) + " " + symbol);
        lblPrice.getStyleClass().add("sub-card-price");

        // Footer Info (Category & Date)
        Label lblDate = new Label(
                "Echéance: " + (sub.getDateDebut() != null ? sub.getDateDebut().plusMonths(1).format(dtf) : "N/A"));
        lblDate.getStyleClass().add("sub-card-date");

        Label lblFreq = new Label(sub.getFrequence());
        lblFreq.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");

        card.getChildren().addAll(lblName, lblPrice, lblDate, lblFreq);

        // Interaction (Click placeholder)
        card.setOnMouseClicked(e -> {
            System.out.println("Selected: " + sub.getNom());
        });

        return card;
    }

    @FXML
    protected void onBtnAjouterClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/add_subscription.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Apply CSS to Modal
            scene.getStylesheets().add(getClass().getResource("/styles_v2.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Nouvel Abonnement");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            // Refresh après fermeture
            refreshDashboard();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onProfileClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/user_profile.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(getClass().getResource("/styles_v2.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Mon Profil");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSettingsClick() {
        try {
            Stage currentStage = (Stage) lblTotalMensuel.getScene().getWindow();
            SceneManager.switchScene(currentStage, "settings.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBtnSupprimerClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Fonctionnalité en cours de mise à jour");
        alert.setContentText("La suppression par sélection multiple arrive bientôt pour la vue Cartes.");
        alert.showAndWait();
    }

    @FXML
    protected void goToAnalytics() {
        try {
            Stage currentStage = (Stage) lblTotalMensuel.getScene().getWindow();
            SceneManager.switchScene(currentStage, "analytics.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
