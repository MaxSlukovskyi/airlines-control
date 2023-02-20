package com.slukovskyi.repositories.impl;

import com.slukovskyi.exceptions.DaoOperationException;
import com.slukovskyi.models.Airline;
import com.slukovskyi.models.planes.Plane;
import com.slukovskyi.repositories.AirlineRepository;
import com.slukovskyi.repositories.PlaneRepository;
import com.slukovskyi.utils.PropertiesReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AirlineDbRepositoryImpl implements AirlineRepository {

    private static AirlineRepository instance;

    private static final String SELECT_ALL_AIRLINES_SQL = "SELECT * FROM airlines;";
    private static final String SELECT_AIRLINE_BY_ID_SQL = "SELECT * FROM airlines WHERE airline_id = ?;";
    private static final String INSERT_AIRLINE_SQL = "INSERT INTO airlines(airline_name) VALUES(?);";
    private static final String UPDATE_AIRLINE_SQL = "UPDATE airlines SET airline_name = ? WHERE airline_id = ?;";
    private static final String DELETE_AIRLINE_SQL = "DELETE FROM airlines WHERE airline_id = ?;";
    private String CONNECTION_URL;
    private String USER;
    private String PASSWORD;

    private final PlaneRepository planeRepository = PlaneDbRepositoryImpl.getInstance();

    private AirlineDbRepositoryImpl() {
        try {
            CONNECTION_URL = PropertiesReader.getProperty("database.url");
            USER = PropertiesReader.getProperty("database.user");
            PASSWORD = PropertiesReader.getProperty("database.password");
        }
        catch (IOException e) {
            log.info("ERROR: {}", e.getMessage());
        }
    }

    public static AirlineRepository getInstance() {
        if (instance == null) {
            instance = new AirlineDbRepositoryImpl();
        }
        return instance;
    }

    @Override
    public List<Airline> getAll() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(SELECT_ALL_AIRLINES_SQL);
            return collectToList(rs);
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    private List<Airline> collectToList(ResultSet rs) throws SQLException {
        List<Airline> airlineList = new ArrayList<>();
        while (rs.next()) {
            Airline airline = parseAirlineRow(rs);
            airlineList.add(airline);
        }
        return airlineList;
    }

    private Airline parseAirlineRow(ResultSet rs) throws SQLException {
        Airline airline = new Airline();
        airline.setId(rs.getLong(1));
        airline.setName(rs.getString(2));
        List<Plane> planes = planeRepository.getAllByAirline(airline);
        airline.setPlanes(planes);
        return airline;
    }

    @Override
    public Airline getById(Long id) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            PreparedStatement selectByIdStatement = prepareSelectByIdStatement(id, connection);
            ResultSet resultSet = selectByIdStatement.executeQuery();
            resultSet.next();
            return parseAirlineRow(resultSet);
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    private PreparedStatement prepareSelectByIdStatement(Long id, Connection connection) {
        try {
            PreparedStatement selectByIdStatement = connection.prepareStatement(SELECT_AIRLINE_BY_ID_SQL);
            selectByIdStatement.setLong(1, id);
            return selectByIdStatement;
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot prepare statement to select airline by id", e);
        }
    }

    @Override
    public Airline add(Airline airline) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            return saveAirline(airline, connection);
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage(), e);
        }
    }

    private Airline saveAirline(Airline airline, Connection connection) throws SQLException {
        PreparedStatement insertStatement = prepareInsertStatement(connection, airline);
        executeUpdate(insertStatement, "Airline was not created");
        Long id = fetchGeneratedId(insertStatement);
        airline.setId(id);
        return airline;
    }

    private PreparedStatement prepareInsertStatement(Connection connection, Airline airline) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement(INSERT_AIRLINE_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            return fillStatementWithAirlineData(insertStatement, airline);
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot prepare statement to insert airline", e);
        }
    }

    private PreparedStatement fillStatementWithAirlineData(PreparedStatement insertStatement, Airline airline)
            throws SQLException {
        insertStatement.setString(1, airline.getName());
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
            throw new DaoOperationException("Can not obtain an airline ID");
        }
    }

    @Override
    public void update(Airline airline) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            PreparedStatement updateStatement = prepareUpdateStatement(airline, connection);
            executeUpdate(updateStatement, "Account was not updated");
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot update Airline with id = %d", airline.getId()), e);
        }
    }

    private PreparedStatement prepareUpdateStatement(Airline airline, Connection connection) {
        try {
            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_AIRLINE_SQL);
            fillStatementWithAirlineData(updateStatement, airline);
            updateStatement.setLong(2, airline.getId());
            return updateStatement;
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot prepare update statement for account id = %d", airline.getId()), e);
        }
    }

    @Override
    public void delete(Airline airline) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            PreparedStatement updateStatement = prepareAirlineDeleteStatement(airline, connection);
            executeUpdate(updateStatement, "Airline was not deleted");
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot delete airline with id = %d", airline.getId()), e);
        }
    }

    private PreparedStatement prepareAirlineDeleteStatement(Airline airline, Connection connection) {
        try {
            PreparedStatement deleteStatement = connection.prepareStatement(DELETE_AIRLINE_SQL);
            deleteStatement.setLong(1, airline.getId());
            return deleteStatement;
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot prepare delete statement for airline id = %d", airline.getId()), e);
        }
    }
}
