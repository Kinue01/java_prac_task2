package org.example.prac2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.example.prac2.App;
import org.example.prac2.model.RadarSystemDataMessage;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Mr231_3_RSDController implements Initializable {
    @FXML
    private TextField recTime;

    @FXML
    private TextField initialDistance;

    @FXML
    private TextField initialBearing;

    @FXML
    private TextField movingCircleOfDistance;

    @FXML
    private TextField bearing;

    @FXML
    private TextField distanceFromShip;

    @FXML
    private TextField bearing2;

    @FXML
    private TextField distanceScale;

    @FXML
    private ComboBox<String> distanceUnit;

    @FXML
    private ComboBox<String> displayOrientation;

    @FXML
    private ComboBox<String> workingMode;

    @FXML
    private ListView<String> list;

    private final ObservableList<String> units = FXCollections.observableArrayList();
    private final ObservableList<String> orientations = FXCollections.observableArrayList();
    private final ObservableList<String> modes = FXCollections.observableArrayList();
    private final ObservableList<String> messages = FXCollections.observableArrayList();
    private final ObservableList<RadarSystemDataMessage> rsds = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GetDistUnits();
        GetOrientations();
        GetModes();
        UpdateMessages();
    }

    private void GetDistUnits() {
        try {
            Statement statement = App.db.getConnection().createStatement();
            //Statement statement = App.db.createStatement();
            ResultSet res = statement.executeQuery("select unit_name from tb_distance_unit");
            while (res.next()) {
                units.add(res.getString(1));
            }
            res.close();
            statement.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            distanceUnit.setItems(units);
        }
    }

    private void GetOrientations() {
        try {
            Statement statement = App.db.getConnection().createStatement();
            //Statement statement = App.db.createStatement();
            ResultSet res = statement.executeQuery("select orientation_name from tb_display_orientation");
            while (res.next()) {
                orientations.add(res.getString(1));
            }
            res.close();
            statement.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            displayOrientation.setItems(orientations);
        }
    }

    private void GetModes() {
        try {
            Statement statement = App.db.getConnection().createStatement();
            //Statement statement = App.db.createStatement();
            ResultSet res = statement.executeQuery("select mode_name from tb_working_mode");
            while (res.next()) {
                modes.add(res.getString(1));
            }
            res.close();
            statement.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            workingMode.setItems(modes);
        }
    }

    private void UpdateMessages() {
        list.setItems(FXCollections.observableArrayList());
        messages.clear();
        rsds.clear();
        try {
            Statement statement = App.db.getConnection().createStatement();
            //Statement statement = App.db.createStatement();
            ResultSet res = statement.executeQuery("select * from tb_rsd");
            while (res.next()) {
                String workingMode = "", distanceUnit = "", displayOrientation = "";

                switch (res.getInt(11)) {
                    case 1: workingMode = "S"; break;
                    case 2: workingMode = "P"; break;
                };

                switch (res.getInt(9)) {
                    case 1: distanceUnit = "K"; break;
                    case 2: distanceUnit = "N"; break;
                };

                switch (res.getInt(10)) {
                    case 1: displayOrientation = "C"; break;
                    case 2: displayOrientation = "H"; break;
                    case 3: displayOrientation = "N"; break;
                };

                rsds.add(new RadarSystemDataMessage(workingMode,
                        displayOrientation,
                        distanceUnit,
                        res.getDouble(8),
                        res.getDouble(7),
                        res.getDouble(6),
                        res.getDouble(5),
                        res.getDouble(4),
                        res.getDouble(3),
                        res.getDouble(2),
                        res.getTimestamp(1)));
            }
            res.close();
            statement.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            rsds.forEach(it -> messages.add(it.toString()));
            list.setItems(messages);
        }
    }

    @FXML
    public void onSendMessageButton(ActionEvent actionEvent) {
        try {
            CheckFields();
            PreparedStatement statement = App.db.getConnection()
                    .prepareStatement("insert into tb_rsd values " +
                            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            /*PreparedStatement statement = App.db
                    .prepareStatement("insert into tb_rsd values " +
                            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");*/
            statement.setTimestamp(1, Timestamp.valueOf(recTime.getText()));
            statement.setDouble(2, Double.parseDouble(initialDistance.getText()));
            statement.setDouble(3, Double.parseDouble(initialBearing.getText()));
            statement.setDouble(4, Double.parseDouble(movingCircleOfDistance.getText()));
            statement.setDouble(5, Double.parseDouble(bearing.getText()));
            statement.setDouble(6, Double.parseDouble(distanceFromShip.getText()));
            statement.setDouble(7, Double.parseDouble(bearing2.getText()));
            statement.setDouble(8, Double.parseDouble(distanceScale.getText()));

            if (distanceUnit.getValue().contains("K")) {
                statement.setInt(9, 1);
            }
            else {
                statement.setInt(9, 2);
            }

            if (displayOrientation.getValue().contains("C")) {
                statement.setInt(10, 1);
            }
            else if (displayOrientation.getValue().contains("H")) {
                statement.setInt(10, 2);
            }
            else {
                statement.setInt(10, 3);
            }

            if (workingMode.getValue().contains("S")) {
                statement.setInt(11, 1);
            }
            else {
                statement.setInt(11, 2);
            }

            int res = statement.executeUpdate();

            if (res == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Добавлено успешно");
                alert.show();
            }

            statement.close();

            UpdateMessages();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        catch (IllegalArgumentException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getMessage());
            alert.show();
        }
    }

    @FXML
    public void onListViewMouseClick(MouseEvent mouseEvent) {
        RadarSystemDataMessage rsd = rsds.get(list.getSelectionModel().getSelectedIndex());

        recTime.setText(rsd.getMsgRecTime().toString());
        initialDistance.setText(rsd.getInitialDistance().toString());
        initialBearing.setText(rsd.getInitialBearing().toString());
        movingCircleOfDistance.setText(rsd.getMovingCircleOfDistance().toString());
        bearing.setText(rsd.getBearing().toString());
        distanceFromShip.setText(rsd.getDistanceFromShip().toString());
        bearing2.setText(rsd.getBearing2().toString());
        distanceScale.setText(rsd.getDistanceScale().toString());

        switch (rsd.getDistanceUnit()) {
            case "K": distanceUnit.setValue(units.get(0)); break;
            case "N": distanceUnit.setValue(units.get(1)); break;
        }

        switch (rsd.getDisplayOrientation()) {
            case "C": displayOrientation.setValue(orientations.get(0)); break;
            case "H": displayOrientation.setValue(orientations.get(1)); break;
            case "N": displayOrientation.setValue(orientations.get(2)); break;
        }

        switch (rsd.getWorkingMode()) {
            case "S": workingMode.setValue(modes.get(0)); break;
            case "P": workingMode.setValue(modes.get(1)); break;
        }
    }

    private void CheckFields() throws IllegalArgumentException {
        if (Timestamp.valueOf(recTime.getText()).getTime() > System.currentTimeMillis()) {
            throw new  IllegalArgumentException("Введите время, которое меньше текущего");
        }

        if (distanceUnit.getValue() == null) {
            throw new IllegalArgumentException("Выберите единицу измерения расстояния");
        }

        if (displayOrientation.getValue() == null) {
            throw new IllegalArgumentException("Выберите ориентацию дисплея");
        }

        if (workingMode.getValue() == null) {
            throw new IllegalArgumentException("Выберите режим работы");
        }
    }
}
