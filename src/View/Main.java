package View;

import Model.ConnectionDB;
import Controller.Scene1;
import Controller.Scene2;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage1) throws IOException {
        ConnectionDB connectionDB = new ConnectionDB();

        FXMLLoader fxmlLoader1 = new FXMLLoader(Scene1.class.getResource("/View/Scene1.fxml"));
        Scene scene1 = new Scene(fxmlLoader1.load());
        Scene1 control1 = fxmlLoader1.getController();
        stage1.setScene(scene1);

        FXMLLoader fxmlLoader2 = new FXMLLoader(Scene2.class.getResource("/View/Scene2.fxml"));
        Scene scene2 = new Scene(fxmlLoader2.load());
        Scene2 control2 = fxmlLoader2.getController();
        Stage stage2 = new Stage();
        stage2.setScene(scene2);

        control1.setControl2(stage2, control2);
        control1.setConnectionDB(connectionDB);
        control2.setControl1(control1);
        stage1.show();
        stage1.setOnCloseRequest(windowEvent -> {
            connectionDB.close();
        });
    }
}
