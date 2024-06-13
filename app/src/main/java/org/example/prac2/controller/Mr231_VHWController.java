package org.example.prac2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.example.prac2.App;
import org.example.prac2.model.WaterSpeedHeadingMessage;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Mr231_VHWController implements Initializable {

    @FXML
    private TextField recTime;

    @FXML
    private TextField cource;

    @FXML
    private TextField speed;

    @FXML
    private ListView<String> list;

    private final ObservableList<String> messages = FXCollections.observableArrayList();
    private final ObservableList<WaterSpeedHeadingMessage> vhws = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UpdateMessages();
    }

    @FXML
    public void onSendMessageButton(ActionEvent actionEvent) {
        try {
            CheckFields();
            PreparedStatement statement = App.db.getConnection()
                    .prepareStatement("insert into tb_vhw (msgrectime, course, speed) " +
                            "values (?, ?, ?)");
            /*PreparedStatement statement = App.db
                    .prepareStatement("insert into tb_vhw (msgrectime, course, speed) " +
                            "values (?, ?, ?)");*/
            statement.setTimestamp(1, Timestamp.valueOf(recTime.getText()));
            statement.setDouble(2, Double.parseDouble(cource.getText()));
            statement.setDouble(3, Double.parseDouble(speed.getText()));

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
        WaterSpeedHeadingMessage vhw = vhws.get(list.getSelectionModel().getSelectedIndex());

        recTime.setText(vhw.getMsgRecTime().toString());
        cource.setText(vhw.getCourse().toString());
        speed.setText(vhw.getSpeed().toString());
    }

    private void UpdateMessages() {
        list.setItems(FXCollections.observableArrayList());
        messages.clear();
        vhws.clear();
        try {
            Statement statement = App.db.getConnection().createStatement();
            //Statement statement = App.db.createStatement();
            ResultSet res = statement.executeQuery("select * from tb_vhw");
            while (res.next()){
                vhws.add(new WaterSpeedHeadingMessage(res.getTimestamp(1),
                        res.getDouble(2),
                        res.getDouble(4)));
            }
            res.close();
            statement.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            vhws.forEach(it -> messages.add(it.toString()));
            list.setItems(messages);
        }
    }

    private void CheckFields() throws IllegalArgumentException {
        if (Timestamp.valueOf(recTime.getText()).getTime() > System.currentTimeMillis()) {
            throw new IllegalArgumentException("Введите время, которое меньше текущего");
        }

        if (Double.parseDouble(cource.getText()) < 0 || Double.parseDouble(cource.getText()) > 359.9) {
            throw new IllegalArgumentException("Курс должен быть больше или равен 0 и меньше 360");
        }
    }
}
