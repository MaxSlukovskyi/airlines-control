package org.slukovskyi.automation;

import com.slukovskyi.airlinesui.AirlinesApplication;
import com.slukovskyi.models.Airline;
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
class AirlinesViewTest extends ApplicationTest{

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(AirlinesApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @Order(1)
    void createAirline() {
        clickOn("#airlinesAddBtn");
        verifyThat("#addAirlineWindow", isVisible());
        clickOn("#addAirlineNameInput").write("AirlineName");
        clickOn("#addAirlineBtn");
        verifyThat("#airlinesTable", hasTableCell("AirlineName"));
    }

    @Test
    @Order(2)
    void updateAirline() {
        TableView<Airline> tableView = lookup("#airlinesTable").queryTableView();
        Airline airline = null;
        for (Airline item : tableView.getItems()) {
            if (item.getName().equals("AirlineName")) {
                airline = item;
            }
        }

        int index = tableView.getItems().indexOf(airline);
        clickOn((Node) lookup(".table-row-cell").nth(index).query());
        clickOn("#airlinesModifyBtn");
        verifyThat("#modifyAirlineWindow", isVisible());
        clickOn("#modifyAirlineNameInput").write("Modified");
        clickOn("#modifyAirlineBtn");
        verifyThat("#airlinesTable", hasTableCell("AirlineNameModified"));
    }

    @Test
    @Order(3)
    void deleteAirline() {
        TableView<Airline> tableView = lookup("#airlinesTable").queryTableView();
        Airline airline = null;
        for (Airline item : tableView.getItems()) {
            if (item.getName().equals("AirlineNameModified")) {
                airline = item;
            }
        }

        int index = tableView.getItems().indexOf(airline);
        clickOn((Node) lookup(".table-row-cell").nth(index).query());
        clickOn("#airlinesDeleteBtn");
        List<Airline> airlines = tableView.getItems();
        assertFalse(airlines.contains(airline));
    }
}
