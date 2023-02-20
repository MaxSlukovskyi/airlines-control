package com.slukovskyi.repositories.impl;

import com.slukovskyi.exceptions.DaoOperationException;
import com.slukovskyi.models.Airline;
import com.slukovskyi.models.enums.JetType;
import com.slukovskyi.models.planes.JetPlane;
import com.slukovskyi.models.planes.PassengerPlane;
import com.slukovskyi.models.planes.Plane;
import com.slukovskyi.repositories.PlaneRepository;
import com.slukovskyi.utils.PropertiesReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

@Slf4j
public class PlaneDbRepositoryImpl implements PlaneRepository {

    private static PlaneRepository instance;

    private static final String INSERT_PLANE_SQL = "INSERT INTO planes(plane_name, plane_seats_number, " +
            "plane_cargo_capacity, plane_maximum_distance, plane_fuel_consumption, plane_type) VALUES(?,?,?,?,?,?);";
    private static final String INSERT_PASSENGER_PLANE_SQL = "INSERT INTO passenger_planes(plane_id, business_class_seats_number, " +
            "first_class_seats_number, economy_class_seats_number) VALUES(?,?,?,?);";
    private static final String INSERT_JET_PLANE_SQL = "INSERT INTO jet_planes(plane_id, jet_type) VALUES(?,?);";
    private static final String SELECT_JET_PLANE_BY_ID_SQL = "SELECT * FROM jet_planes JOIN planes ON jet_planes.plane_id=planes.plane_id LEFT JOIN airlines a on planes.plane_airline_id = a.airline_id WHERE jet_planes.plane_id = ?;";
    private static final String SELECT_PASSENGER_PLANE_BY_ID_SQL = "SELECT * FROM passenger_planes JOIN planes ON passenger_planes.plane_id=planes.plane_id LEFT JOIN airlines a on planes.plane_airline_id = a.airline_id WHERE passenger_planes.plane_id = ?;";
    private static final String SELECT_ALL_JET_PLANES_SQL = "SELECT * FROM jet_planes JOIN planes ON jet_planes.plane_id=planes.plane_id LEFT JOIN airlines a on planes.plane_airline_id = a.airline_id;";
    private static final String SELECT_ALL_PASSENGER_PLANES_SQL = "SELECT * FROM passenger_planes JOIN planes ON passenger_planes.plane_id=planes.plane_id LEFT JOIN airlines a on planes.plane_airline_id = a.airline_id;";
    private static final String SELECT_ALL_JET_PLANES_BY_AIRLINE_SQL = "SELECT * FROM jet_planes JOIN planes ON jet_planes.plane_id=planes.plane_id JOIN airlines a on a.airline_id = planes.plane_airline_id WHERE planes.plane_airline_id = ?;";
    private static final String SELECT_ALL_PASSENGER_PLANES_BY_AIRLINE_SQL = "SELECT * FROM passenger_planes JOIN planes ON passenger_planes.plane_id=planes.plane_id JOIN airlines a on a.airline_id = planes.plane_airline_id WHERE planes.plane_airline_id = ?;";
    private static final String SELECT_ALL_JET_PLANES_WITHOUT_AIRLINE_SQL = "SELECT * FROM jet_planes JOIN planes ON jet_planes.plane_id=planes.plane_id LEFT JOIN airlines a on a.airline_id = planes.plane_airline_id WHERE planes.plane_airline_id IS NULL;";
    private static final String SELECT_ALL_PASSENGER_PLANES_WITHOUT_AIRLINE_SQL = "SELECT * FROM passenger_planes JOIN planes ON passenger_planes.plane_id=planes.plane_id LEFT JOIN airlines a on a.airline_id = planes.plane_airline_id WHERE planes.plane_airline_id IS NULL;";
    private static final String SELECT_ALL_JET_PLANES_WITH_AIRLINE_SQL = "SELECT * FROM jet_planes JOIN planes ON jet_planes.plane_id=planes.plane_id LEFT JOIN airlines a on a.airline_id = planes.plane_airline_id WHERE planes.plane_airline_id IS NOT NULL;";
    private static final String SELECT_ALL_PASSENGER_PLANES_WITH_AIRLINE_SQL = "SELECT * FROM passenger_planes JOIN planes ON passenger_planes.plane_id=planes.plane_id LEFT JOIN airlines a on a.airline_id = planes.plane_airline_id WHERE planes.plane_airline_id IS NOT NULL;";
    private static final String UPDATE_PLANE_SQL = "UPDATE planes SET plane_name = ?, plane_seats_number = ?, plane_cargo_capacity = ?, plane_maximum_distance = ?, plane_fuel_consumption = ?, plane_airline_id = ? WHERE plane_id = ?;";
    private static final String UPDATE_JET_PLANE_SQL = "UPDATE jet_planes SET jet_type = ? WHERE plane_id = ?;";
    private static final String UPDATE_PASSENGER_PLANE_SQL = "UPDATE passenger_planes SET business_class_seats_number = ?, first_class_seats_number = ?, economy_class_seats_number = ? WHERE plane_id = ?;";
    private static final String DELETE_PASSENGER_PLANE_SQL = "DELETE FROM passenger_planes WHERE plane_id = ?;";
    private static final String DELETE_JET_PLANE_SQL = "DELETE FROM jet_planes WHERE plane_id = ?;";
    private static final String DELETE_PLANE_SQL = "DELETE FROM planes WHERE plane_id = ?;";
    private String CONNECTION_URL;
    private String USER;
    private String PASSWORD;

    private PlaneDbRepositoryImpl() {
        try {
            CONNECTION_URL = PropertiesReader.getProperty("database.url");
            USER = PropertiesReader.getProperty("database.user");
            PASSWORD = PropertiesReader.getProperty("database.password");
        }
        catch (IOException e) {
            log.info("ERROR: {}", e.getMessage());
        }
    }

    public static PlaneRepository getInstance() {
        if (instance == null) {
            instance = new PlaneDbRepositoryImpl();
        }
        return instance;
    }

    @Override
    public Plane add(Plane plane) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            return savePlane(plane, connection);
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage(), e);
        }
    }

    private Plane savePlane(Plane plane, Connection connection) throws SQLException {
        PreparedStatement insertStatement = prepareInsertStatement(connection, plane);
        executeUpdate(insertStatement, "Plane was not created");
        return plane;
    }

    private PreparedStatement prepareInsertStatement(Connection connection, Plane plane) {
        try {
            PreparedStatement insertStatement = null;
            Long planeId = insertPlane(connection, plane);
            plane.setId(planeId);

            if (plane instanceof PassengerPlane) {
                insertStatement = connection.prepareStatement(INSERT_PASSENGER_PLANE_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
                insertStatement.setLong(1, planeId);
                return fillInsertStatementWithPassengerPlaneData(insertStatement, plane);
            }
            else if (plane instanceof JetPlane){
                insertStatement = connection.prepareStatement(INSERT_JET_PLANE_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
                insertStatement.setLong(1, planeId);
                return fillInsertStatementWithJetPlaneData(insertStatement, plane);
            }
            return null;
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot prepare statement to insert plane", e);
        }
    }

    //save plane to table planes, return generated plane_id
    private Long insertPlane(Connection connection, Plane plane) {
        try {
            PreparedStatement planeInsertStatement = connection.prepareStatement(INSERT_PLANE_SQL,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement filledInsertStatement = fillInsertStatementWithPlaneData(planeInsertStatement, plane);
            executeUpdate(filledInsertStatement, "Plane was not created");
            Long id = fetchGeneratedId(filledInsertStatement);
            return id;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PreparedStatement fillInsertStatementWithPlaneData(PreparedStatement insertStatement, Plane plane)
            throws SQLException {
        insertStatement.setString(1, plane.getName());
        insertStatement.setInt(2, plane.getSeatsNumber());
        insertStatement.setInt(3, plane.getCargoCapacity());
        insertStatement.setInt(4, plane.getMaximumDistance());
        insertStatement.setInt(5, plane.getFuelConsumption());
        insertStatement.setString(6, plane.getClass().getSimpleName());
        return insertStatement;
    }

    private PreparedStatement fillInsertStatementWithPassengerPlaneData(PreparedStatement insertStatement, Plane plane)
            throws SQLException {
        PassengerPlane passengerPlane = (PassengerPlane) plane;
        insertStatement.setInt(2, passengerPlane.getBusinessClassSeatsNumber());
        insertStatement.setInt(3, passengerPlane.getFirstClassSeatsNumber());
        insertStatement.setInt(4, passengerPlane.getEconomyClassSeatsNumber());
        return insertStatement;
    }

    private PreparedStatement fillInsertStatementWithJetPlaneData(PreparedStatement insertStatement, Plane plane)
            throws SQLException {
        insertStatement.setString(2, ((JetPlane) plane).getJetType().toString());
        return insertStatement;
    }

    private void executeUpdate(PreparedStatement insertStatement, String errorMessage) throws SQLException {
        int rowsAffected = insertStatement.executeUpdate();
        if (rowsAffected == 0) {
            throw new DaoOperationException(errorMessage);
        }
    }

    private Long fetchGeneratedId(PreparedStatement insertStatement) throws SQLException {
        ResultSet generatedKeys = insertStatement.getGeneratedKeys();

        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        } else {
            throw new DaoOperationException("Can not obtain an plane ID");
        }
    }

    @Override
    public List<Plane> getAll() {
        List<Plane> planes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet passengerPlanesRS = statement.executeQuery(SELECT_ALL_PASSENGER_PLANES_SQL);
            planes.addAll(collectToList(passengerPlanesRS, "PassengerPlane"));
            ResultSet jetPlanesRS = statement.executeQuery(SELECT_ALL_JET_PLANES_SQL);
            planes.addAll(collectToList(jetPlanesRS, "JetPlane"));
            return planes;
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    private List<Plane> collectToList(ResultSet rs, String planeType) throws SQLException {
        List<Plane> accountList = new ArrayList<>();
        if (planeType.equals("PassengerPlane")) {
            while (rs.next()) {
                Plane passengerPlane = parseFullPassengerPlaneRow(rs);
                accountList.add(passengerPlane);
            }
        }
        else if (planeType.equals("JetPlane")) {
            while (rs.next()) {
                Plane jetPlane = parseFullJetPlaneRow(rs);
                accountList.add(jetPlane);
            }
        }
        return accountList;
    }

    @Override
    public List<Plane> getAllByAirline(Airline airline) {
        List<Plane> planes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PASSENGER_PLANES_BY_AIRLINE_SQL);
            statement.setLong(1, airline.getId());
            ResultSet passengerPlanesRS = statement.executeQuery();
            planes.addAll(collectToList(passengerPlanesRS, "PassengerPlane"));
            statement = connection.prepareStatement(SELECT_ALL_JET_PLANES_BY_AIRLINE_SQL);
            statement.setLong(1, airline.getId());
            ResultSet jetPlanesRS = statement.executeQuery();
            planes.addAll(collectToList(jetPlanesRS, "JetPlane"));
            return planes;
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    @Override
    public List<Plane> getAllWithoutAirline() {
        List<Plane> planes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet passengerPlanesRS = statement.executeQuery(SELECT_ALL_PASSENGER_PLANES_WITHOUT_AIRLINE_SQL);
            planes.addAll(collectToList(passengerPlanesRS, "PassengerPlane"));
            ResultSet jetPlanesRS = statement.executeQuery(SELECT_ALL_JET_PLANES_WITHOUT_AIRLINE_SQL);
            planes.addAll(collectToList(jetPlanesRS, "JetPlane"));
            return planes;
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    @Override
    public List<Plane> getAllWithAirline() {
        List<Plane> planes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet passengerPlanesRS = statement.executeQuery(SELECT_ALL_PASSENGER_PLANES_WITH_AIRLINE_SQL);
            planes.addAll(collectToList(passengerPlanesRS, "PassengerPlane"));
            ResultSet jetPlanesRS = statement.executeQuery(SELECT_ALL_JET_PLANES_WITH_AIRLINE_SQL);
            planes.addAll(collectToList(jetPlanesRS, "JetPlane"));
            return planes;
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    @Override
    public Plane getById(Long id) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            return findPlaneById(id, connection);
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot find Plane by id = %d", id), e);
        }
    }

    private Plane findPlaneById(Long id, Connection connection) throws SQLException {
        PreparedStatement selectByIdStatement = prepareSelectPassengerPlaneByIdStatement(id, connection);
        ResultSet resultSet = selectByIdStatement.executeQuery();
        if (resultSet.next()) {
            return parseFullPassengerPlaneRow(resultSet);
        }
        else {
            selectByIdStatement = prepareSelectJetPlaneByIdStatement(id, connection);
            resultSet = selectByIdStatement.executeQuery();
            resultSet.next();
            return parseFullJetPlaneRow(resultSet);
        }
    }

    private PreparedStatement prepareSelectJetPlaneByIdStatement(Long id, Connection connection) {
        try {
            PreparedStatement selectByIdStatement = connection.prepareStatement(SELECT_JET_PLANE_BY_ID_SQL);
            selectByIdStatement.setLong(1, id);
            return selectByIdStatement;
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot prepare statement to select jet plane by id", e);
        }
    }

    private PreparedStatement prepareSelectPassengerPlaneByIdStatement(Long id, Connection connection) {
        try {
            PreparedStatement selectByIdStatement = connection.prepareStatement(SELECT_PASSENGER_PLANE_BY_ID_SQL);
            selectByIdStatement.setLong(1, id);
            return selectByIdStatement;
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot prepare statement to select passenger plane by id", e);
        }
    }

    private Plane parseFullJetPlaneRow(ResultSet rs) throws SQLException {
        JetPlane jetPlane = new JetPlane();
        jetPlane.setId(rs.getLong(1));
        jetPlane.setJetType(JetType.valueOf(rs.getString(2)));
        jetPlane.setName(rs.getString(4));
        jetPlane.setSeatsNumber(rs.getInt(5));
        jetPlane.setCargoCapacity(rs.getInt(6));
        jetPlane.setMaximumDistance(rs.getInt(7));
        jetPlane.setFuelConsumption(rs.getInt(8));

        if (rs.getLong(11) != 0) {
            Airline airline = new Airline();
            airline.setId(rs.getLong(11));
            airline.setName(rs.getString(12));
            jetPlane.setAirline(airline);
        }

        return jetPlane;
    }


    private Plane parseFullPassengerPlaneRow(ResultSet rs) throws SQLException {
        PassengerPlane passengerPlane = new PassengerPlane();
        passengerPlane.setId(rs.getLong(1));
        passengerPlane.setBusinessClassSeatsNumber(rs.getInt(2));
        passengerPlane.setFirstClassSeatsNumber(rs.getInt(3));
        passengerPlane.setEconomyClassSeatsNumber(rs.getInt(4));
        passengerPlane.setName(rs.getString(6));
        passengerPlane.setSeatsNumber(rs.getInt(7));
        passengerPlane.setCargoCapacity(rs.getInt(8));
        passengerPlane.setMaximumDistance(rs.getInt(9));
        passengerPlane.setFuelConsumption(rs.getInt(10));

        if (rs.getLong(11) != 0) {
            Airline airline = new Airline();
            airline.setId(rs.getLong(11));
            airline.setName(rs.getString(14));
            passengerPlane.setAirline(airline);
        }

        return passengerPlane;
    }

    @Override
    public void update(Plane plane) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            PreparedStatement updateStatement = preparePlaneUpdateStatement(plane, connection);
            executeUpdate(updateStatement, "Plane was not updated");
            if (plane instanceof JetPlane) {
                updateStatement = prepareJetPlaneUpdateStatement(plane, connection);
                executeUpdate(updateStatement, "Plane was not updated");
            } else if (plane instanceof PassengerPlane) {
                updateStatement = preparePassengerPlaneUpdateStatement(plane, connection);
                executeUpdate(updateStatement, "Plane was not updated");
            }
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot update Plane with id = %d", plane.getId()), e);
        }
    }

    private PreparedStatement preparePlaneUpdateStatement(Plane plane, Connection connection) {
        try {
            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_PLANE_SQL);
            return fillUpdateStatementWithPlaneData(updateStatement, plane);
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot prepare update statement for plane id = %d", plane.getId()), e);
        }
    }

    private PreparedStatement fillUpdateStatementWithPlaneData(PreparedStatement updateStatement, Plane plane)
            throws SQLException {
        updateStatement.setString(1, plane.getName());
        updateStatement.setInt(2, plane.getSeatsNumber());
        updateStatement.setInt(3, plane.getCargoCapacity());
        updateStatement.setInt(4, plane.getMaximumDistance());
        updateStatement.setInt(5, plane.getFuelConsumption());
        if (plane.getAirline() != null) {
            updateStatement.setLong(6, plane.getAirline().getId());
        }
        else {
            updateStatement.setNull(6, NULL);
        }
        updateStatement.setLong(7, plane.getId());
        return updateStatement;
    }

    private PreparedStatement prepareJetPlaneUpdateStatement(Plane plane, Connection connection) {
        try {
            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_JET_PLANE_SQL);
            return fillUpdateStatementWithJetPlaneData(updateStatement, plane);
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot prepare update statement for plane id = %d", plane.getId()), e);
        }
    }

    private PreparedStatement preparePassengerPlaneUpdateStatement(Plane plane, Connection connection) {
        try {
            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_PASSENGER_PLANE_SQL);
            return fillUpdateStatementWithPassengerPlaneData(updateStatement, plane);
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot prepare update statement for plane id = %d", plane.getId()), e);
        }
    }

    private PreparedStatement fillUpdateStatementWithPassengerPlaneData(PreparedStatement insertStatement, Plane plane)
            throws SQLException {
        PassengerPlane passengerPlane = (PassengerPlane) plane;
        insertStatement.setInt(1, passengerPlane.getBusinessClassSeatsNumber());
        insertStatement.setInt(2, passengerPlane.getFirstClassSeatsNumber());
        insertStatement.setInt(3, passengerPlane.getEconomyClassSeatsNumber());
        insertStatement.setLong(4, passengerPlane.getId());
        return insertStatement;
    }

    private PreparedStatement fillUpdateStatementWithJetPlaneData(PreparedStatement insertStatement, Plane plane)
            throws SQLException {
        insertStatement.setString(1, ((JetPlane) plane).getJetType().toString());
        insertStatement.setLong(2, plane.getId());
        return insertStatement;
    }

    @Override
    public void delete(Plane plane) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            PreparedStatement updateStatement = null;
            if (plane instanceof PassengerPlane) {
                updateStatement = preparePassengerPlaneDeleteStatement(plane, connection);
            } else if (plane instanceof JetPlane) {
                updateStatement = prepareJetPlaneDeleteStatement(plane, connection);
            }
            executeUpdate(updateStatement, "Plane was not deleted");
            updateStatement = connection.prepareStatement(DELETE_PLANE_SQL);
            updateStatement.setLong(1, plane.getId());
            executeUpdate(updateStatement, "Plane was not deleted");

        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot delete Plane with id = %d", plane.getId()), e);
        }
    }

    private PreparedStatement preparePassengerPlaneDeleteStatement(Plane plane, Connection connection) {
        try {
            PreparedStatement deleteStatement = connection.prepareStatement(DELETE_PASSENGER_PLANE_SQL);
            deleteStatement.setLong(1, plane.getId());
            return deleteStatement;
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot prepare delete statement for plane id = %d", plane.getId()), e);
        }
    }

    private PreparedStatement prepareJetPlaneDeleteStatement(Plane plane, Connection connection) {
        try {
            PreparedStatement deleteStatement = connection.prepareStatement(DELETE_JET_PLANE_SQL);
            deleteStatement.setLong(1, plane.getId());
            return deleteStatement;
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot prepare delete statement for plane id = %d", plane.getId()), e);
        }
    }
}

