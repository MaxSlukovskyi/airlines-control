package com.slukovskyi.airlinesui;

import com.slukovskyi.models.Airline;
import com.slukovskyi.models.planes.Plane;
import com.slukovskyi.services.AirlineService;
import com.slukovskyi.services.PlaneService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddPlaneToAirlineController implements Initializable {

    @FXML
    public Button selectAirlineBtn;
    @FXML
    public ChoiceBox<Airline> airlinesChoiceBox;

    private Plane plane;

    private final PlaneService planeService = PlaneService.getInstance();

    private final AirlineService airlineService = AirlineService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Airline> airlines = airlineService.getAll();
        this.airlinesChoiceBox.setItems(FXCollections.observableArrayList(airlines));
    }

    @FXML
    protected void onSelectAirlineButtonClick() {
        Airline selectedAirline = this.airlinesChoiceBox.getValue();
        planeService.addPlaneToAirline(plane, selectedAirline);
        Stage stage = (Stage) selectAirlineBtn.getScene().getWindow();
        stage.close();
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }
}
