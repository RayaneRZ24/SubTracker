package com.emsi.subtracker.views;

import com.emsi.subtracker.models.Abonnement;
import com.emsi.subtracker.models.User;
import com.emsi.subtracker.services.SubscriptionService;
import com.emsi.subtracker.utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {

    @FXML
    private Label lblUsername;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblTotalSubscriptions;
    @FXML
    private Label lblTotalCost;

    private final SubscriptionService service = new SubscriptionService();
    private final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserData();
    }

    private void loadUserData() {
        User user = UserSession.getInstance().getUser();
        if (user != null) {
            lblUsername.setText(user.getUsername());
            lblEmail.setText(user.getEmail());

            // Fetch stats
            List<Abonnement> subs = service.getAll();
            lblTotalSubscriptions.setText(String.valueOf(subs.size()));

            double total = service.calculerTotalMensuel();
            lblTotalCost.setText(df.format(total) + " DH");
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) lblUsername.getScene().getWindow();
        stage.close();
    }
}
