package com.slukovskyi.airlinesui;

import com.slukovskyi.models.Airline;
import com.slukovskyi.models.planes.JetPlane;
import com.slukovskyi.models.planes.PassengerPlane;
import com.slukovskyi.models.planes.Plane;
import com.slukovskyi.services.AirlineService;
import com.slukovskyi.services.PlaneService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public Button planesModifyBtn;
    @FXML
    public TableView<Plane> planesTable;
    @FXML
    public TableColumn<Plane, Long> planeIdColumn;
    @FXML
    public TableColumn<Plane, String> planeNameColumn;
    @FXML
    public Button airlinesAddBtn;
    @FXML
    public Button airlinesModifyBtn;
    @FXML
    public Button airlinesDeleteBtn;
    @FXML
    public Button airlinesMenuBtn;
    @FXML
    public Button planesMenuBtn;
    @FXML
    public Button aboutMenuBtn;
    @FXML
    public Rectangle passengerPlanesCounterBg;
    @FXML
    public Rectangle jetPlanesCounterBg;
    @FXML
    public Rectangle cargoCapacityCounterBg;
    @FXML
    public Rectangle totalSeatsCounterBg;
    @FXML
    public Label passengerPlanesCounterValue;
    @FXML
    public Label passengerPlanesCounterTitle;
    @FXML
    public Label jetPlanesCounterValue;
    @FXML
    public Label jetPlanesCounterTitle;
    @FXML
    public Label cargoCapacityCounterValue;
    @FXML
    public Label cargoCapacityCounterTitle;
    @FXML
    public Label totalSeatsCounterValue;
    @FXML
    public Label totalSeatsCounterTitle;
    @FXML
    public Button planesAddBtn;
    @FXML
    public Button planesDeleteBtn;
    @FXML
    public Button planesSearchBtn;
    @FXML
    public Button addPlaneToAirlineBtn;
    @FXML
    public Rectangle planesTableBg;
    @FXML
    public Rectangle airlinesTableBg;
    @FXML
    public TableColumn<Plane, String> planeAirlineColumn;
    @FXML
    public TableColumn<Plane, Long> planeSeatsNumberColumn;
    @FXML
    public TableColumn<Plane, Long> planeCargoCapacityColumn;
    @FXML
    public TableColumn<Plane, Long> planeMaximumDistanceColumn;
    @FXML
    public TableColumn<Plane, Long> planeFuelConsumptionColumn;
    @FXML
    public TableColumn<Plane, Integer> planeBusinessClassSeatsColumn;
    @FXML
    public TableColumn<Plane, Integer> planeFirstClassSeatsColumn;
    @FXML
    public TableColumn<Plane, Integer> planeEconomyClassSeatsColumn;
    @FXML
    public TableColumn<Plane, String> planeJetTypeColumn;
    @FXML
    public Button deletePlaneFromAirlineBtn;
    @FXML
    private TableColumn<Airline, Long> airlineIdColumn;
    @FXML
    private TableColumn<Airline, String> airlineNameColumn;
    @FXML
    private TableView<Airline> airlinesTable;

    private final PlaneService planeService = PlaneService.getInstance();
    private final AirlineService airlineService = AirlineService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setPlanesVisibility(false);
        this.airlinesModifyBtn.setDisable(true);
        this.airlinesDeleteBtn.setDisable(true);
        this.planesModifyBtn.setDisable(true);
        this.planesDeleteBtn.setDisable(true);
        this.addPlaneToAirlineBtn.setDisable(true);
        this.deletePlaneFromAirlineBtn.setDisable(true);
        this.passengerPlanesCounterValue.setText("0");
        this.jetPlanesCounterValue.setText("0");
        this.totalSeatsCounterValue.setText("0");
        this.cargoCapacityCounterValue.setText("0");
        this.setAirlinesVisibility(true);
        this.setDataToAirlinesTable();
    }

    @FXML
    protected void onPlaneButtonClick() {
        this.setAirlinesVisibility(false);
        this.setPlanesVisibility(true);
        this.setDataToPlanesTable(planeService.getAll());
    }

    @FXML
    protected void onAirlinesButtonClick() {
        this.setPlanesVisibility(false);
        this.setAirlinesVisibility(true);
        this.setDataToAirlinesTable();
    }

    @FXML
    protected void onAboutButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
                AboutController.class.getResource("about.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Опис");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow());
        stage.showAndWait();

        this.setDataToAirlinesTable();
    }

    @FXML
    protected void addAirlineButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
                AddAirlineController.class.getResource("add-airline.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Додати авіакомпанію");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow());
        stage.showAndWait();

        this.setDataToAirlinesTable();
    }

    @FXML
    protected void modifyAirlineButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(
                ModifyAirlineController.class.getResource("modify-airline.fxml"));

        Parent root = loader.load();

        ModifyAirlineController controller = loader.getController();

        Airline airline = airlinesTable.getSelectionModel().getSelectedItem();
        controller.setAirline(airline);
        controller.fillInputs();

        stage.setScene(new Scene(root));
        stage.setTitle("Редагування авіакомпанії");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow());
        stage.showAndWait();

        this.setDataToAirlinesTable();
    }

    @FXML
    protected void deleteAirlineButtonClick() {
        Airline airline = airlinesTable.getSelectionModel().getSelectedItem();
        airlineService.delete(airline);
        this.setDataToAirlinesTable();
    }

    @FXML
    protected void airlinesTableMouseClick() {
        this.airlinesModifyBtn.setDisable(false);
        this.airlinesDeleteBtn.setDisable(false);
        Airline airline = airlinesTable.getSelectionModel().getSelectedItem();
        if (airline != null) {
            this.passengerPlanesCounterValue.setText(String.valueOf(airline.getPlanes().stream()
                    .filter(PassengerPlane.class::isInstance).count()));
            this.jetPlanesCounterValue.setText(String.valueOf(airline.getPlanes().stream()
                    .filter(JetPlane.class::isInstance).count()));
            this.cargoCapacityCounterValue.setText(String.valueOf(airline.getPlanes().stream()
                    .mapToInt(Plane::getCargoCapacity).sum()));
            this.totalSeatsCounterValue.setText(String.valueOf(airline.getPlanes().stream()
                    .mapToInt(Plane::getSeatsNumber).sum()));
        }
    }

    @FXML
    protected void planesTableMouseClick() {
        this.planesModifyBtn.setDisable(false);
        this.planesDeleteBtn.setDisable(false);
        Plane plane = this.planesTable.getSelectionModel().getSelectedItem();
        if (plane != null) {
            this.addPlaneToAirlineBtn.setDisable(plane.getAirline() != null);
            this.deletePlaneFromAirlineBtn.setDisable(plane.getAirline() == null);
        }
    }

    @FXML
    protected void addPlaneToAirlineButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                AddPlaneToAirlineController.class.getResource("add-plane-to-airline.fxml"));

        Parent root = loader.load();

        AddPlaneToAirlineController controller = loader.getController();
        Plane plane = this.planesTable.getSelectionModel().getSelectedItem();
        controller.setPlane(plane);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Додати літак до авіакомпанії");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow());
        stage.showAndWait();

        this.setDataToPlanesTable(planeService.getAll());
    }

    @FXML
    protected void deletePlaneFromAirlineButtonClick() {
        Plane plane = this.planesTable.getSelectionModel().getSelectedItem();
        planeService.deletePlaneFromAirline(plane);
        this.setDataToPlanesTable(planeService.getAll());
    }

    @FXML
    protected void addPlaneButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                AddPlaneController.class.getResource("add-plane.fxml"));

        Parent root = loader.load();

        AddPlaneController controller = loader.getController();

        controller.updatePlaneTypeOptions();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Додати літак");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow());
        stage.showAndWait();

        this.setDataToPlanesTable(planeService.getAll());
    }

    @FXML
    protected void modifyPlaneButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        Plane plane = this.planesTable.getSelectionModel().getSelectedItem();

        if (plane instanceof PassengerPlane passengerPlane) {
            FXMLLoader loader = new FXMLLoader(
                    ModifyPassengerPlaneController.class.getResource("modify-passenger-plane.fxml"));

            Parent root = loader.load();

            ModifyPassengerPlaneController controller = loader.getController();

            controller.setPassengerPlane(passengerPlane);
            controller.fillInputs();
            stage.setScene(new Scene(root));
        } else if (plane instanceof JetPlane jetPlane) {
            FXMLLoader loader = new FXMLLoader(
                    ModifyJetPlaneController.class.getResource("modify-jet-plane.fxml"));

            Parent root = loader.load();

            ModifyJetPlaneController controller = loader.getController();

            controller.setJetPlane(jetPlane);
            controller.fillInputs();
            stage.setScene(new Scene(root));
        }

        stage.setTitle("Редагувати літак");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow());
        stage.showAndWait();

        this.setDataToPlanesTable(planeService.getAll());
    }

    @FXML
    protected void deletePlaneButtonClick() {
        Plane plane = this.planesTable.getSelectionModel().getSelectedItem();
        planeService.delete(plane);
        this.setDataToPlanesTable(planeService.getAll());
    }

    @FXML
    protected void searchPlanesButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                SearchPlanesController.class.getResource("search-planes.fxml"));

        Parent root = loader.load();

        SearchPlanesController controller = loader.getController();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Пошук літаків");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow());
        stage.showAndWait();
        Airline airline = controller.getAirline();
        Integer minValue = controller.getMinValue();
        Integer maxValue = controller.getMaxValue();

        if (airline != null && minValue != null && maxValue != null) {
            List<Plane> planes = planeService.getAirlinePlanesByFuelConsumption(minValue, maxValue, airline);
            this.setDataToPlanesTable(planes);
        }
    }

    private void setAirlinesVisibility(boolean value) {
        airlinesTable.setVisible(value);
        airlinesTableBg.setVisible(value);
        airlinesAddBtn.setVisible(value);
        airlinesModifyBtn.setVisible(value);
        airlinesDeleteBtn.setVisible(value);
        passengerPlanesCounterBg.setVisible(value);
        passengerPlanesCounterTitle.setVisible(value);
        passengerPlanesCounterValue.setVisible(value);
        jetPlanesCounterBg.setVisible(value);
        jetPlanesCounterTitle.setVisible(value);
        jetPlanesCounterValue.setVisible(value);
        cargoCapacityCounterBg.setVisible(value);
        cargoCapacityCounterTitle.setVisible(value);
        cargoCapacityCounterValue.setVisible(value);
        totalSeatsCounterBg.setVisible(value);
        totalSeatsCounterTitle.setVisible(value);
        totalSeatsCounterValue.setVisible(value);
    }

    private void setPlanesVisibility(boolean value) {
        planesTable.setVisible(value);
        planesTableBg.setVisible(value);
        planesAddBtn.setVisible(value);
        planesModifyBtn.setVisible(value);
        planesDeleteBtn.setVisible(value);
        planesSearchBtn.setVisible(value);
        addPlaneToAirlineBtn.setVisible(value);
        deletePlaneFromAirlineBtn.setVisible(value);
    }

    private void setDataToAirlinesTable() {
        airlineIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        airlineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        List<Airline> airlines = airlineService.getAll();

        final ObservableList<Airline> data = FXCollections.observableArrayList(airlines);

        airlinesTable.setItems(data);
        airlinesTable.refresh();
    }

    private void setDataToPlanesTable(List<Plane> planes) {
        planeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        planeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        planeAirlineColumn.setCellValueFactory(cellData -> cellData.getValue().getAirline() != null ?
                new SimpleStringProperty(cellData.getValue().getAirline().getName()) : null);
        planeCargoCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("cargoCapacity"));
        planeSeatsNumberColumn.setCellValueFactory(new PropertyValueFactory<>("seatsNumber"));
        planeFuelConsumptionColumn.setCellValueFactory(new PropertyValueFactory<>("fuelConsumption"));
        planeMaximumDistanceColumn.setCellValueFactory(new PropertyValueFactory<>("maximumDistance"));

        planeBusinessClassSeatsColumn.setCellValueFactory(data -> {
            Plane plane = data.getValue();
            if (plane instanceof PassengerPlane) {
                return new ReadOnlyObjectWrapper<>(((PassengerPlane) plane).getBusinessClassSeatsNumber());
            } else {
                return null;
            }
        });

        planeFirstClassSeatsColumn.setCellValueFactory(data -> {
            Plane plane = data.getValue();
            if (plane instanceof PassengerPlane) {
                return new ReadOnlyObjectWrapper<>(((PassengerPlane) plane).getFirstClassSeatsNumber());
            } else {
                return null;
            }
        });

        planeEconomyClassSeatsColumn.setCellValueFactory(data -> {
            Plane plane = data.getValue();
            if (plane instanceof PassengerPlane) {
                return new ReadOnlyObjectWrapper<>(((PassengerPlane) plane).getEconomyClassSeatsNumber());
            } else {
                return null;
            }
        });

        planeJetTypeColumn.setCellValueFactory(data -> {
            Plane plane = data.getValue();
            if (plane instanceof JetPlane) {
                return new ReadOnlyObjectWrapper<>(((JetPlane) plane).getJetType().toString());
            } else {
                return null;
            }
        });

        final ObservableList<Plane> data = FXCollections.observableArrayList(planes);

        planesTable.setItems(data);
        planesTable.refresh();
    }
}