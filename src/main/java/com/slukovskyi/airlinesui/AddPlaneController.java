package com.slukovskyi.airlinesui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddPlaneController implements Initializable {

    @FXML
    public Button selectPlaneTypeBtn;
    @FXML
    public ChoiceBox<String> planeTypesChoiceBox;
    private List<String> planeTypes = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.planeTypes.add("Passenger");
        this.planeTypes.add("Jet");
    }

    @FXML
    protected void onSelectPlaneTypeButtonClick(ActionEvent event) throws IOException {
        String selectedType = planeTypesChoiceBox.getValue();
        if (selectedType.equals("Passenger")) {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(
                    AddPassengerPlaneController.class.getResource("add-passenger-plane.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Додати пасажирський літак");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow());
            stage.showAndWait();
        } else if (selectedType.equals("Jet")) {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(
                    AddJetPlaneController.class.getResource("add-jet-plane.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Додати бізнес-джет");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow());
            stage.showAndWait();
        }

        Stage stage = (Stage) selectPlaneTypeBtn.getScene().getWindow();
        stage.close();
    }

    public void setPlaneTypes(List<String> planeTypes) {
        this.planeTypes = planeTypes;
    }

    public void updatePlaneTypeOptions() {
        this.planeTypesChoiceBox.setItems(FXCollections.observableArrayList(this.planeTypes));
    }
}
