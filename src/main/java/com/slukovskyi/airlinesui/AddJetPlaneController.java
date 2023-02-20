package com.slukovskyi.airlinesui;

import com.slukovskyi.models.enums.JetType;
import com.slukovskyi.models.planes.JetPlane;
import com.slukovskyi.services.PlaneService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddJetPlaneController implements Initializable {

    @FXML
    public TextField addJetPlaneNameInput;
    @FXML
    public TextField addJetPlaneSeatsNumberInput;
    @FXML
    public TextField addJetPlaneCargoCapacityInput;
    @FXML
    public TextField addJetPlaneMaximumDistanceInput;
    @FXML
    public TextField addJetPlaneFuelConsumptionInput;
    @FXML
    public ChoiceBox<String> addJetPlaneTypeInput;
    @FXML
    public Button addAirlineBtn;

    private final PlaneService planeService = PlaneService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> jetTypes = Arrays.stream(JetType.values()).map(Enum::toString).toList();
        addJetPlaneTypeInput.setItems(FXCollections.observableArrayList(jetTypes));
    }

    @FXML
    protected void onAddJetPlaneButtonClick() {
        String name = this.addJetPlaneNameInput.getText();
        Integer seatsNumber = Integer.valueOf(this.addJetPlaneSeatsNumberInput.getText());
        Integer cargoCapacity = Integer.valueOf(this.addJetPlaneCargoCapacityInput.getText());
        Integer maximumDistance = Integer.valueOf(this.addJetPlaneMaximumDistanceInput.getText());
        Integer fuelConsumption = Integer.valueOf(this.addJetPlaneFuelConsumptionInput.getText());
        JetType jetType = JetType.valueOf(this.addJetPlaneTypeInput.getValue());

        JetPlane jetPlane = new JetPlane(name, seatsNumber, cargoCapacity, maximumDistance, fuelConsumption, jetType);
        planeService.add(jetPlane);
        Stage stage = (Stage) addAirlineBtn.getScene().getWindow();
        stage.close();
    }
}
