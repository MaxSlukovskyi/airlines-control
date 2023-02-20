package com.slukovskyi.airlinesui;

import com.slukovskyi.models.planes.PassengerPlane;
import com.slukovskyi.services.PlaneService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPassengerPlaneController implements Initializable {
    @FXML
    public TextField modifyPassengerPlaneNameInput;
    @FXML
    public TextField modifyPassengerPlaneSeatsNumberInput;
    @FXML
    public TextField modifyPassengerPlaneCargoCapacityInput;
    @FXML
    public TextField modifyPassengerPlaneMaximumDistanceInput;
    @FXML
    public TextField modifyPassengerPlaneFuelConsumptionInput;
    @FXML
    public TextField modifyPassengerPlaneBusinessClassSeatsInput;
    @FXML
    public TextField modifyPassengerPlaneFirstClassSeatsInput;
    @FXML
    public TextField modifyPassengerPlaneEconomyClassSeatsInput;
    @FXML
    public Button modifyPassengerPlaneBtn;

    private PassengerPlane passengerPlane;

    private final PlaneService planeService = PlaneService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void onModifyPassengerPlaneButtonClick() {
        String name = this.modifyPassengerPlaneNameInput.getText();
        Integer seatsNumber = Integer.valueOf(this.modifyPassengerPlaneSeatsNumberInput.getText());
        Integer cargoCapacity = Integer.valueOf(this.modifyPassengerPlaneCargoCapacityInput.getText());
        Integer fuelConsumption = Integer.valueOf(this.modifyPassengerPlaneFuelConsumptionInput.getText());
        Integer maximumDistance = Integer.valueOf(this.modifyPassengerPlaneMaximumDistanceInput.getText());
        Integer businessClassSeats = Integer.valueOf(this.modifyPassengerPlaneBusinessClassSeatsInput.getText());
        Integer firstClassSeats = Integer.valueOf(this.modifyPassengerPlaneFirstClassSeatsInput.getText());
        Integer economyClassSeats = Integer.valueOf(this.modifyPassengerPlaneEconomyClassSeatsInput.getText());

        this.passengerPlane.setName(name);
        this.passengerPlane.setSeatsNumber(seatsNumber);
        this.passengerPlane.setCargoCapacity(cargoCapacity);
        this.passengerPlane.setFuelConsumption(fuelConsumption);
        this.passengerPlane.setMaximumDistance(maximumDistance);
        this.passengerPlane.setBusinessClassSeatsNumber(businessClassSeats);
        this.passengerPlane.setFirstClassSeatsNumber(firstClassSeats);
        this.passengerPlane.setEconomyClassSeatsNumber(economyClassSeats);

        planeService.update(passengerPlane);
        Stage stage = (Stage) modifyPassengerPlaneBtn.getScene().getWindow();
        stage.close();
    }

    public void fillInputs() {
        this.modifyPassengerPlaneNameInput.setText(passengerPlane.getName());
        this.modifyPassengerPlaneSeatsNumberInput.setText(passengerPlane.getSeatsNumber().toString());
        this.modifyPassengerPlaneCargoCapacityInput.setText(passengerPlane.getCargoCapacity().toString());
        this.modifyPassengerPlaneFuelConsumptionInput.setText(passengerPlane.getFuelConsumption().toString());
        this.modifyPassengerPlaneMaximumDistanceInput.setText(passengerPlane.getMaximumDistance().toString());
        this.modifyPassengerPlaneBusinessClassSeatsInput.setText(passengerPlane.getBusinessClassSeatsNumber().toString());
        this.modifyPassengerPlaneFirstClassSeatsInput.setText(passengerPlane.getFirstClassSeatsNumber().toString());
        this.modifyPassengerPlaneEconomyClassSeatsInput.setText(passengerPlane.getEconomyClassSeatsNumber().toString());
    }

    public void setPassengerPlane(PassengerPlane passengerPlane) {
        this.passengerPlane = passengerPlane;
    }
}
