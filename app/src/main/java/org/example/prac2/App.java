package org.example.prac2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.postgresql.jdbc.PgConnection;
import java.io.IOException;
import java.sql.DriverManager;
import java.util.Objects;

public class App extends Application {

    public static App instance;
    public static Database db;

    @Override
    public void init() throws Exception {
        super.init();
        instance = this;
        db = new Database();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("choose-view.fxml")));
        stage.setTitle("Конвертер");
        Scene scene = new Scene(root, 250, 100);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void openMr231TTM() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mr231_ttm-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("МР-231 TTM");
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void openMr231VHW() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mr231_vhw-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("МР-231 VHW");
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void openMr231RSD() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mr231_rsd-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("МР-231 RSD");
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void openMr231_3TTM() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mr231-3_ttm-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("МР-231-3 TTM");
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void openMr231_3RSD() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mr231-3_rsd-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("МР-231-3 RSD");
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}