package org.example.prac2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.example.prac2.App;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChooseController implements Initializable {

    @FXML
    private Button btn;

    @FXML
    private ComboBox<String> converterTypes;

    @FXML
    private ComboBox<String> msgTypes;

    ObservableList<String> mr231_types = FXCollections.observableArrayList("TTM", "VHW", "RSD");
    ObservableList<String> mr231_3_types = FXCollections.observableArrayList("TTM", "RSD");
    ObservableList<String> oTypes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GetConverterTypes();
        converterTypes.setOnAction(actionEvent -> {
            msgTypes.setItems(FXCollections.observableArrayList());
            if (Objects.equals(converterTypes.getValue(), "МР-231")) {
                msgTypes.setItems(mr231_types);
            }
            else {
                msgTypes.setItems(mr231_3_types);
            }
        });
        btn.setOnAction(actionEvent -> {
            if (converterTypes.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Выберите конвертер");
                alert.show();
            }

            if (msgTypes.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Выберите тип сообщения");
                alert.show();
            }

            switch (converterTypes.getValue()) {
                case "МР-231":
                    switch (msgTypes.getValue()) {
                        case "TTM":
                            App.instance.openMr231TTM();
                            break;
                        case "VHW":
                            App.instance.openMr231VHW();
                            break;
                        case "RSD":
                            App.instance.openMr231RSD();
                            break;
                    }
                    break;
                case "МР-231-3":
                    switch (msgTypes.getValue()) {
                        case "TTM":
                            App.instance.openMr231_3TTM();
                            break;
                        case "RSD":
                            App.instance.openMr231_3RSD();
                            break;
                    }
                    break;
            }
        });
    }

    private void GetConverterTypes() {
        try {
            Statement statement = App.db.getConnection().createStatement();
            //Statement statement = App.db.createStatement();
            ResultSet res = statement.executeQuery("select type_name from tb_msgtypes");
            while (res.next()) {
                oTypes.add(res.getString(1));
            }
            res.close();
            statement.close();
            converterTypes.setItems(oTypes);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
