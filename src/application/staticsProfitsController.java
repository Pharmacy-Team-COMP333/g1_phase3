package application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class staticsProfitsController {
    @FXML
    private Label netIncome;
    @FXML
    private Label numOrders;
    @FXML
    private Label Profits;
    @FXML
    private PieChart paymentMethods;

    @FXML
    public void initialize() {
        try {
            Connector.a.connectDB();
            int num = 0;
            PreparedStatement st2 = Connector.a.connectDB().prepareStatement("SELECT COUNT(*) FROM bill;");
            ResultSet r2 = st2.executeQuery();
            if (r2.next()) {
                num = r2.getInt(1);
            }
            numOrders.setText(num + "");

            double num2 = 0;
            PreparedStatement st3 = Connector.a.connectDB().prepareStatement("SELECT SUM(profits) FROM bill;");
            ResultSet r3 = st3.executeQuery();
            if (r3.next()) {
                num2 = r3.getDouble(1);
            }
            Profits.setText(num2 + "$");

            double num3 = 0;
            PreparedStatement st4 = Connector.a.connectDB().prepareStatement("SELECT SUM(full_price) FROM bill;");
            ResultSet r4 = st4.executeQuery();
            if (r4.next()) {
                num3 = r4.getDouble(1);
            }
            netIncome.setText(num3 + "$");

            int cash = 0;
            int insh = 0;

            PreparedStatement st5 = Connector.a.connectDB().prepareStatement("SELECT COUNT(*) FROM bill WHERE bill_type = 'cash';");
            ResultSet r5 = st5.executeQuery();
            if (r5.next()) {
                cash = r5.getInt(1);
            }

            PreparedStatement st6 = Connector.a.connectDB().prepareStatement("SELECT COUNT(*) FROM bill WHERE bill_type = 'insurance';");
            ResultSet r6 = st6.executeQuery();
            if (r6.next()) {
                insh = r6.getInt(1);
            }

            // Print the values of cash and insh for debugging
            System.out.println("Cash: " + cash);
            System.out.println("Insurance: " + insh);

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Insurance", insh),
                    new PieChart.Data("Cash", cash)
            );
            paymentMethods.setData(pieChartData);
            paymentMethods.setStartAngle(90); // Adjust the starting angle
            paymentMethods.setLabelsVisible(true); // Show data labels

            // Apply custom colors using CSS
            paymentMethods.getData().forEach(data -> {
                String style = "";
                if ("Cash".equals(data.getName())) {
                    style = "-fx-pie-color: #ff7f50;"; // Coral color
                } else if ("Insurance".equals(data.getName())) {
                    style = "-fx-pie-color: #4682b4;"; // Steel blue color
                }
                data.getNode().setStyle(style);
            });

            Connector.a.connectDB().close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
