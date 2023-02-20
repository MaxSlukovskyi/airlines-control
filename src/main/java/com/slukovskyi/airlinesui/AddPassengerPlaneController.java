package com.slukovskyi.airlinesui;

import com.slukovskyi.models.enums.JetType;
import com.slukovskyi.models.planes.JetPlane;
import com.slukovskyi.models.planes.PassengerPlane;
import com.slukovskyi.services.PlaneService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddPassengerPlaneController implements Initializable {

    @FXML
    public TextField addPassengerPlaneNameInput;
    @FXML
    public TextField addPassengerPlaneSeatsNumberInput;
    @FXML
    public TextField addPassengerPlaneCargoCapacityInput;
    @FXML
    public TextField addPassengerPlaneMaximumDistanceInput;
    @FXML
    public TextField addPassengerPlaneFuelConsumptionInput;
    @FXML
    public TextField addPassengerPlaneBusinessClassSeatsInput;
    @FXML
    public TextField addPassengerPlaneFirstClassSeatsInput;
    @FXML
    public TextField addPassengerPlaneEconomyClassSeatsInput;

    private final PlaneService planeService = PlaneService.getInstance();
    @FXML
    public Button addPlaneBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    protected void onAddPassengerPlaneButtonClick() {
        String name = this.addPassengerPlaneNameInput.getText();
        Integer seatsNumber = Integer.valueOf(this.addPassengerPlaneSeatsNumberInput.getText());
        Integer cargoCapacity = Integer.valueOf(this.addPassengerPlaneCargoCapacityInput.getText());
        Integer maximumDistance = Integer.valueOf(this.addPassengerPlaneMaximumDistanceInput.getText());
        Integer fuelConsumption = Integer.valueOf(this.addPassengerPlaneFuelConsumptionInput.getText());
        Integer businessSeats = Integer.valueOf(this.addPassengerPlaneBusinessClassSeatsInput.getText());
        Integer firstClassSeats = Integer.valueOf(this.addPassengerPlaneFirstClassSeatsInput.getText());
        Integer economySeats = Integer.valueOf(this.addPassengerPlaneEconomyClassSeatsInput.getText());

        PassengerPlane passengerPlane = new PassengerPlane(name, seatsNumber, cargoCapacity, maximumDistance,
                fuelConsumption, businessSeats, firstClassSeats, economySeats);
        planeService.add(passengerPlane);
        Stage stage = (Stage) addPlaneBtn.getScene().getWindow();
        stage.close();
    }
}
