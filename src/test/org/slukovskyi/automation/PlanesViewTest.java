package org.slukovskyi.automation;

import com.slukovskyi.airlinesui.AirlinesApplication;
import com.slukovskyi.models.planes.Plane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TableViewMatchers.hasTableCell;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlanesViewTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(AirlinesApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @Order(1)
    void createPassengerPlane() {
        clickOn("#planes-menu-btn");
        clickOn("#planesAddBtn");
        verifyThat("#addPlaneWindow", isVisible());
        clickOn("#planeTypesChoiceBox").clickOn("Passenger");
        clickOn("#selectPlaneTypeBtn");
        verifyThat("#addPassengerPlaneWindow", isVisible());
        clickOn("#addPassengerPlaneNameInput").write("TestPlane");
        clickOn("#addPassengerPlaneSeatsNumberInput").write("110");
        clickOn("#addPassengerPlaneCargoCapacityInput").write("15000");
        clickOn("#addPassengerPlaneMaximumDistanceInput").write("3200");
        clickOn("#addPassengerPlaneFuelConsumptionInput").write("400");
        clickOn("#addPassengerPlaneBusinessClassSeatsInput").write("20");
        clickOn("#addPassengerPlaneFirstClassSeatsInput").write("30");
        clickOn("#addPassengerPlaneEconomyClassSeatsInput").write("60");
        clickOn("#addPlaneBtn");
        verifyThat("#planesTable", hasTableCell("TestPlane"));
    }

    @Test
    @Order(2)
    void updatePassengerPlane() {
        clickOn("#planes-menu-btn");
        TableView<Plane> tableView = lookup("#planesTable").queryTableView();
        Plane plane = null;
        for (Plane item : tableView.getItems()) {
            if (item.getName().equals("TestPlane")) {
                plane = item;
            }
        }

        int index = tableView.getItems().indexOf(plane);
        clickOn((Node) lookup(".table-row-cell").nth(index - 2).query());
        clickOn("#planesModifyBtn");
        clickOn("#modifyPassengerPlaneNameInput").write("Modified");
        clickOn("#modifyPassengerPlaneBtn");
        verifyThat("#planesTable", hasTableCell("TestPlaneModified"));
    }

    @Test
    @Order(3)
    void deletePassengerPlane() {
        clickOn("#planes-menu-btn");
        TableView<Plane> tableView = lookup("#planesTable").queryTableView();
        Plane plane = null;
        for (Plane item : tableView.getItems()) {
            if (item.getName().equals("TestPlaneModified")) {
                plane = item;
            }
        }

        int index = tableView.getItems().indexOf(plane);
        clickOn((Node) lookup(".table-row-cell").nth(index - 2).query());
        clickOn("#planesDeleteBtn");
        List<Plane> planes = tableView.getItems();
        assertFalse(planes.contains(plane));
    }
}
