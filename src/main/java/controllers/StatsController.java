package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import managers.DataStore;

public class StatsController {

    @FXML private Label labelTotalPatients;
    @FXML private Label labelTotalDoctors;
    @FXML private Label labelTotalRooms;
    @FXML private Label labelAvailableRooms;
    @FXML private Label labelOccupiedRooms;
    @FXML private Label labelTotalAppointments;
    @FXML private Label labelOccupancyRate;
    @FXML private Label labelOccupancyBar;

    // Charts
    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;

    private DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        refreshStats();
        AnimationManager.fadeIn(pieChart);
        AnimationManager.fadeIn(barChart);
    }

    @FXML
    public void handleRefresh() {
        refreshStats();
        AnimationManager.pulse(labelTotalPatients);
        AnimationManager.pulse(labelTotalDoctors);
        AnimationManager.pulse(labelTotalRooms);
        AnimationManager.pulse(labelTotalAppointments);
    }

    private void refreshStats() {
        int totalPatients     = store.getTotalPatients();
        int totalDoctors      = store.getTotalDoctors();
        int totalRooms        = store.getTotalRooms();
        int availableRooms    = store.getAvailableRoomsCount();
        int occupiedRooms     = totalRooms - availableRooms;
        int totalAppointments = store.getTotalAppointments();

        // Mettre à jour les labels
        labelTotalPatients.setText(String.valueOf(totalPatients));
        labelTotalDoctors.setText(String.valueOf(totalDoctors));
        labelTotalRooms.setText(String.valueOf(totalRooms));
        labelAvailableRooms.setText(String.valueOf(availableRooms));
        labelOccupiedRooms.setText(String.valueOf(occupiedRooms));
        labelTotalAppointments.setText(String.valueOf(totalAppointments));

        // Taux d'occupation
        if (totalRooms > 0) {
            int rate = (int) ((occupiedRooms * 100.0) / totalRooms);
            labelOccupancyRate.setText(rate + "%");
            int bars = rate / 10;
            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                bar.append(i < bars ? "█" : "░");
            }
            labelOccupancyBar.setText(bar.toString());
        } else {
            labelOccupancyRate.setText("0%");
            labelOccupancyBar.setText("░░░░░░░░░░");
        }

        // ===== PIE CHART =====
        pieChart.getData().clear();

        if (availableRooms > 0) {
            PieChart.Data disponibles = new PieChart.Data(
                    "Disponibles (" + availableRooms + ")", availableRooms);
            pieChart.getData().add(disponibles);
        }
        if (occupiedRooms > 0) {
            PieChart.Data occupees = new PieChart.Data(
                    "Occupées (" + occupiedRooms + ")", occupiedRooms);
            pieChart.getData().add(occupees);
        }

        // Colorier le pie chart
        javafx.application.Platform.runLater(() -> {
            if (pieChart.getData().size() >= 1) {
                pieChart.getData().get(0).getNode()
                        .setStyle("-fx-pie-color: #2E7D32;");
            }
            if (pieChart.getData().size() >= 2) {
                pieChart.getData().get(1).getNode()
                        .setStyle("-fx-pie-color: #C62828;");
            }
        });

        // ===== BAR CHART =====
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Hospital Data");

        series.getData().add(new XYChart.Data<>("Patients", totalPatients));
        series.getData().add(new XYChart.Data<>("Médecins", totalDoctors));
        series.getData().add(new XYChart.Data<>("Chambres", totalRooms));
        series.getData().add(new XYChart.Data<>("Rendez-vous", totalAppointments));
        series.getData().add(new XYChart.Data<>("Disponibles", availableRooms));
        series.getData().add(new XYChart.Data<>("Occupées", occupiedRooms));

        barChart.getData().add(series);

        // Colorier les barres
        javafx.application.Platform.runLater(() -> {
            String[] colors = {
                    "#1A237E", "#028090", "#0D47A1",
                    "#F57C00", "#2E7D32", "#C62828"
            };
            for (int i = 0; i < series.getData().size(); i++) {
                if (series.getData().get(i).getNode() != null) {
                    series.getData().get(i).getNode()
                            .setStyle("-fx-bar-fill: " + colors[i] + ";");
                }
            }
        });
    }
}