package com.emsi.subtracker.views;

import com.emsi.subtracker.models.Abonnement;
import com.emsi.subtracker.services.SubscriptionService;
import com.emsi.subtracker.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Contrôleur de l'écran Analytics.
 */
public class AnalyticsController implements Initializable {

    @FXML
    private PieChart pieChartCategories;

    @FXML
    private BarChart<String, Number> barChartCosts;

    private final SubscriptionService service = new SubscriptionService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPieChartData();
        loadBarChartData();
    }

    private void loadPieChartData() {
        List<Abonnement> list = service.getAll();

        // Group by category and sum counts (or costs?)
        // Let's visualize Cost Distribution by Category
        Map<String, Double> costByCategory = list.stream()
                .collect(Collectors.groupingBy(
                        Abonnement::getCategorie,
                        Collectors.summingDouble(a -> {
                            // Normalize to monthly cost
                            return "Annuel".equalsIgnoreCase(a.getFrequence()) ? a.getPrix() / 12.0 : a.getPrix();
                        })));

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        costByCategory.forEach((cat, cost) -> {
            pieData.add(new PieChart.Data(cat, cost));
        });

        pieChartCategories.setData(pieData);
        // Title handled in FXML
    }

    private void loadBarChartData() {
        List<Abonnement> list = service.getAll();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Coût Mensuel");

        for (Abonnement sub : list) {
            double monthlyCost = "Annuel".equalsIgnoreCase(sub.getFrequence()) ? sub.getPrix() / 12.0 : sub.getPrix();
            series.getData().add(new XYChart.Data<>(sub.getNom(), monthlyCost));
        }

        barChartCosts.getData().add(series);
    }

    @FXML
    protected void goToDashboard() {
        try {
            Stage currentStage = (Stage) pieChartCategories.getScene().getWindow();
            SceneManager.changeScene(currentStage, "dashboard.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
