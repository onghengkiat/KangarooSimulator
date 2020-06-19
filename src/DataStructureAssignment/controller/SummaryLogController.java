package DataStructureAssignment.controller;

import DataStructureAssignment.Main;
import DataStructureAssignment.utils.ConnectionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SummaryLogController implements Initializable {
    @FXML
    private AnchorPane rootSummaryLog;
    @FXML
    private Label summaryLog;
    @FXML
    private Label summaryDay;
    private Alert alert = new Alert(Alert.AlertType.NONE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            summaryDay.setText("Summary for Day - " + DayTableController.getDay());
            summaryLog.setText(ConnectionUtil.getSummary(DayTableController.getRecordID(),
                    DayTableController.getDay()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void toSimPage(ActionEvent actionEvent) {
        try{
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/mainmenu.fxml")));
            //switches from history page into the simulation mode selection page which is the default main menu page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toHistoryPage(ActionEvent actionEvent){
        /*
        This is needed due to some special conditions like:
        1) The page currently displayed is not the first page
         */
        try{
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/records.fxml")));
            //switches to first page of the history page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toSettings(ActionEvent actionEvent) {
        try{
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/settings.fxml")));
            //switches from history page to setting page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quit(ActionEvent actionEvent) {
        //asks user for confirmation to quit the programs
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure to quit the program?");
        alert.showAndWait();

        //if confirms to quit
        if (alert.getResult() == ButtonType.OK) {
            //quit the program
            Main.exit();
        }
    }

    public void logOut(ActionEvent actionEvent) {
        //asks the user for confirmation to log out from the account
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure to log out from this account?");
        alert.showAndWait();

        //if confirms to log out
        if (alert.getResult() == ButtonType.OK) {
            try {
                Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/login.fxml")));
                //Switches from history page to login page by fading
                Main.switchSceneByFading(newScene,rootSummaryLog);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void backToDay(ActionEvent actionEvent){
        try{
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/dayTable.fxml")));
            //switches to the record viewing page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
