package com.slukovskyi.airlinesui;

import com.slukovskyi.models.Airline;
import com.slukovskyi.services.AirlineService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchPlanesController implements Initializable {

    @FXML
    public Button searchPlanesBtn;
    @FXML
    public ChoiceBox<Airline> airlinesChoiceBox;
    @FXML
    public TextField minValueInput;
    @FXML
    public TextField maxValueInput;

    private AirlineService airlineService = AirlineService.getInstance();

    private Airline airline;
    private Integer minValue;
    private Integer maxValue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Airline> airlines = airlineService.getAll();
        this.airlinesChoiceBox.setItems(FXCollections.observableArrayList(airlines));
    }

    @FXML
    protected void onSearchPlanesButtonClick() {
        Airline airline = airlinesChoiceBox.getValue();
        Integer minValue = Integer.valueOf(minValueInput.getText());
        Integer maxValue = Integer.valueOf(maxValueInput.getText());
        this.airline = airline;
        this.minValue = minValue;
        this.maxValue = maxValue;
        Stage stage = (Stage) searchPlanesBtn.getScene().getWindow();
        stage.close();
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }
}
