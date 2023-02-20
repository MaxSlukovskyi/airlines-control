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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ModifyJetPlaneController implements Initializable {

    @FXML
    public TextField modifyJetPlaneNameInput;
    @FXML
    public TextField modifyJetPlaneSeatsNumberInput;
    @FXML
    public TextField modifyJetPlaneCargoCapacityInput;
    @FXML
    public TextField modifyJetPlaneMaximumDistanceInput;
    @FXML
    public TextField modifyJetPlaneFuelConsumptionInput;
    @FXML
    public ChoiceBox<String> modifyJetPlaneTypeChoiceBox;
    @FXML
    public Button modifyJetPlaneBtn;
    private JetPlane jetPlane;

    private final PlaneService planeService = PlaneService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> jetTypes = Arrays.stream(JetType.values()).map(Enum::toString).toList();
        modifyJetPlaneTypeChoiceBox.setItems(FXCollections.observableArrayList(jetTypes));
    }

    @FXML
    protected void onModifyJetPlaneButtonClick() {
        String name = this.modifyJetPlaneNameInput.getText();
        Integer seatsNumber = Integer.valueOf(this.modifyJetPlaneSeatsNumberInput.getText());
        Integer cargoCapacity = Integer.valueOf(this.modifyJetPlaneCargoCapacityInput.getText());
        Integer fuelConsumption = Integer.valueOf(this.modifyJetPlaneFuelConsumptionInput.getText());
        Integer maximumDistance = Integer.valueOf(this.modifyJetPlaneMaximumDistanceInput.getText());
        JetType jetType = JetType.valueOf(this.modifyJetPlaneTypeChoiceBox.getValue());

        this.jetPlane.setName(name);
        this.jetPlane.setSeatsNumber(seatsNumber);
        this.jetPlane.setCargoCapacity(cargoCapacity);
        this.jetPlane.setFuelConsumption(fuelConsumption);
        this.jetPlane.setMaximumDistance(maximumDistance);
        this.jetPlane.setJetType(jetType);

        planeService.update(jetPlane);
        Stage stage = (Stage) modifyJetPlaneBtn.getScene().getWindow();
        stage.close();
    }

    public void fillInputs() {
        this.modifyJetPlaneNameInput.setText(jetPlane.getName());
        this.modifyJetPlaneSeatsNumberInput.setText(jetPlane.getSeatsNumber().toString());
        this.modifyJetPlaneCargoCapacityInput.setText(jetPlane.getCargoCapacity().toString());
        this.modifyJetPlaneFuelConsumptionInput.setText(jetPlane.getFuelConsumption().toString());
        this.modifyJetPlaneMaximumDistanceInput.setText(jetPlane.getMaximumDistance().toString());
        this.modifyJetPlaneTypeChoiceBox.setValue(jetPlane.getJetType().toString());
    }

    public void setJetPlane(JetPlane jetPlane) {
        this.jetPlane = jetPlane;
    }
}
