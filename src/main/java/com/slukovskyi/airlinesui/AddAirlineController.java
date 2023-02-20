package com.slukovskyi.airlinesui;

import com.slukovskyi.models.Airline;
import com.slukovskyi.services.AirlineService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddAirlineController implements Initializable {

    private final AirlineService airlineService = AirlineService.getInstance();
    @FXML
    public TextField addAirlineNameInput;
    @FXML
    public Button addAirlineBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addAirlineBtn.setDisable(true);
    }

    @FXML
    protected void onAddAirlineButtonClick() {
        String name = addAirlineNameInput.getText();
        Airline airline = new Airline(name);
        airlineService.add(airline);
        Stage stage = (Stage) addAirlineBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onAirlineNameInputChanged() {
        addAirlineBtn.setDisable(addAirlineNameInput.getText().isEmpty());
    }
}
