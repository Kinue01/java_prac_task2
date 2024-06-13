package org.example.prac2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.example.prac2.App;
import org.example.prac2.model.IFF;
import org.example.prac2.model.TargetStatus;
import org.example.prac2.model.TargetType;
import org.example.prac2.model.TrackedTargetMessage;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class Mr231_3_TTMController implements Initializable {

    @FXML
    private ListView<String> list;

    @FXML
    private ComboBox<String> status;

    @FXML
    private ComboBox<String> iff;

    @FXML
    private TextField recTime;

    @FXML
    private TextField targetNum;

    @FXML
    private TextField distance;

    @FXML
    private TextField bearing;

    @FXML
    private TextField speed;

    @FXML
    private TextField cource;

    @FXML
    private ComboBox<String> type;

    @FXML
    private TextField msgTime;

    private final ObservableList<String> messages = FXCollections.observableArrayList();
    private final ObservableList<String> iffs = FXCollections.observableArrayList();
    private final ObservableList<String> statuses = FXCollections.observableArrayList();
    private final ObservableList<String> types = FXCollections.observableArrayList();
    private final ObservableList<TrackedTargetMessage> ttms = FXCollections.observableArrayList();

    @FXML
    protected void onSendButtonClick() {
        try {
            CheckFields();
            PreparedStatement st = App.db.getConnection()
                    .prepareStatement("insert into tb_ttm values (?, 2, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            /*PreparedStatement st = App.db
                    .prepareStatement("insert into tb_ttm values (?, 2, ?, ?, ?, ?, ?, ?, ?, ?, ?)");*/
            st.setTimestamp(1, Timestamp.valueOf(recTime.getText()));
            st.setLong(2, Long.parseLong(msgTime.getText()));
            st.setInt(3, Integer.parseInt(targetNum.getText()));
            st.setDouble(4, Double.parseDouble(distance.getText()));
            st.setDouble(5, Double.parseDouble(bearing.getText()));
            st.setDouble(6, Double.parseDouble(cource.getText()));
            st.setDouble(7, Double.parseDouble(speed.getText()));

            if (Objects.equals(type.getValue(), "Поверхность")) {
                st.setInt(8, 1);
            }
            else if (Objects.equals(type.getValue(), "Воздух")) {
                st.setInt(8, 2);
            }
            else {
                st.setInt(8, 3);
            }

            if (status.getValue().contains("L")) {
                st.setInt(9, 1);
            }
            else if (status.getValue().contains("Q")) {
                st.setInt(9, 2);
            }
            else {
                st.setInt(9, 3);
            }

            if (iff.getValue().contains("b")) {
                st.setInt(10, 1);
            }
            else if (iff.getValue().contains("p")) {
                st.setInt(10, 2);
            }
            else {
                st.setInt(10, 3);
            }

            int res = st.executeUpdate();

            if (res == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Добавлено успешно");
                alert.show();
            }

            st.close();

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
        TrackedTargetMessage curr = ttms.get(list.getSelectionModel().getSelectedIndex());

        recTime.setText(curr.getMsgRecTime().toString());
        targetNum.setText(curr.getTargetNumber().toString());
        distance.setText(curr.getDistance().toString());
        bearing.setText(curr.getBearing().toString());
        speed.setText(curr.getSpeed().toString());
        cource.setText(curr.getCourse().toString());
        msgTime.setText(curr.getMsgTime().toString());

        switch (curr.getIff()) {
            case FOE: iff.setValue(iffs.get(1)); break;
            case FRIEND: iff.setValue(iffs.get(0)); break;
            case UNKNOWN: iff.setValue(iffs.get(2)); break;
        }

        switch (curr.getStatus()){
            case LOST: status.setValue(statuses.get(0)); break;
            case UNRELIABLE_DATA: status.setValue(statuses.get(1)); break;
            case TRACKED: status.setValue(statuses.get(2)); break;
        }

        switch (curr.getType()) {
            case SURFACE: type.setValue(types.get(0)); break;
            case AIR: type.setValue(types.get(1)); break;
            case UNKNOWN: type.setValue(types.get(2)); break;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GetIffs();
        GetStatuses();
        GetTypes();
        UpdateMessages();
    }

    private void UpdateMessages() {
        list.setItems(FXCollections.observableArrayList());
        messages.clear();
        ttms.clear();
        try {
            Statement st = App.db.getConnection().createStatement();
            //Statement st = App.db.createStatement();
            ResultSet rs = st.executeQuery("select * from tb_ttm where msgtype = 2");
            while (rs.next()) {
                TargetType type = null;
                TargetStatus status = null;
                IFF iff = null;

                switch (rs.getInt(9)) {
                    case 1: type = TargetType.SURFACE; break;
                    case 2: type = TargetType.AIR; break;
                    case 3: type = TargetType.UNKNOWN; break;
                };
                switch (rs.getInt(10)) {
                    case 1: status = TargetStatus.LOST; break;
                    case 2: status = TargetStatus.UNRELIABLE_DATA; break;
                    case 3: status = TargetStatus.TRACKED; break;
                };
                switch (rs.getInt(11)) {
                    case 1: iff = IFF.FRIEND; break;
                    case 2: iff = IFF.FOE; break;
                    case 3: iff = IFF.UNKNOWN; break;
                };

                ttms.add(new TrackedTargetMessage(
                        rs.getTimestamp(1),
                        rs.getLong(3),
                        rs.getInt(4),
                        rs.getDouble(5),
                        rs.getDouble(6),
                        rs.getDouble(7),
                        rs.getDouble(8),
                        type,
                        status,
                        iff));
            }
            rs.close();
            st.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        ttms.forEach(it -> messages.add(it.toString()));

        list.setItems(messages);
    }

    private void GetIffs() {
        try{
            Statement st = App.db.getConnection().createStatement();
            //Statement st = App.db.createStatement();
            ResultSet res = st.executeQuery("select iff_name from tb_iff");
            while (res.next()) {
                iffs.add(res.getString(1));
            }
            res.close();
            st.close();
            iff.setItems(iffs);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void GetStatuses() {
        try {
            Statement statement = App.db.getConnection().createStatement();
            //Statement statement = App.db.createStatement();
            ResultSet res = statement.executeQuery("select status_name from tb_target_status");
            while (res.next()) {
                statuses.add(res.getString(1));
            }
            res.close();
            statement.close();
            status.setItems(statuses);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void GetTypes() {
        try {
            Statement statement = App.db.getConnection().createStatement();
            //Statement statement = App.db.createStatement();
            ResultSet res = statement.executeQuery("select type_name from tb_target_type");
            while (res.next()) {
                types.add(res.getString(1));
            }
            res.close();
            statement.close();
            type.setItems(types);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void CheckFields() throws IllegalArgumentException {
        if (Integer.parseInt(targetNum.getText()) <= 0 || Integer.parseInt(targetNum.getText()) > 99) {
            throw new IllegalArgumentException("Номер цели должен быть больше 0 и меньше 100");
        }

        if (Double.parseDouble(distance.getText()) < 0 || Double.parseDouble(distance.getText()) > 32) {
            throw new IllegalArgumentException("Дистанция должна быть больше или равна 0 и меньше 32");
        }

        if (Double.parseDouble(bearing.getText()) < 0 || Double.parseDouble(bearing.getText()) > 359.9) {
            throw new IllegalArgumentException("Пеленг должен быть больше или равен 0 и меньше 360");
        }

        if (Double.parseDouble(speed.getText()) < 0 || Double.parseDouble(speed.getText()) > 90) {
            throw new IllegalArgumentException("Скорость должна быть больше или равна 0 и меньше 90");
        }

        if (Double.parseDouble(cource.getText()) < 0 || Double.parseDouble(speed.getText()) > 359.9) {
            throw new IllegalArgumentException("Курс должен быть больше или равен 0 и меньше 360");
        }

        if (type.getValue() == null) {
            throw new IllegalArgumentException("Выберите тип цели");
        }

        if (status.getValue() == null) {
            throw new IllegalArgumentException("Выберите статус цели");
        }

        if (iff.getValue() == null) {
            throw new IllegalArgumentException("Выберите признак опознавания цели");
        }

        if (Timestamp.valueOf(recTime.getText()).getTime() > System.currentTimeMillis()) {
            throw new IllegalArgumentException("Введите время, которое меньше текущего");
        }
    }
}
