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

public class ModifyAirlineController implements Initializable {

    private Airline airline;
    @FXML
    public TextField modifyAirlineNameInput;
    @FXML
    public Button modifyAirlineBtn;

    private final AirlineService airlineService = AirlineService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void onModifyAirlineButtonClick() {
        String name = modifyAirlineNameInput.getText();
        airline.setName(name);
        airlineService.update(airline);
        Stage stage = (Stage) modifyAirlineBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onAirlineNameInputChanged() {
        modifyAirlineBtn.setDisable(modifyAirlineNameInput.getText().isEmpty());
    }

    public void fillInputs() {
        this.modifyAirlineNameInput.setText(airline.getName());
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

}
