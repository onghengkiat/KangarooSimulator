package DataStructureAssignment.controller;

import DataStructureAssignment.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class mainmenuController {
    @FXML
    private AnchorPane rootMainmenu;
    private Alert alert = new Alert(Alert.AlertType.NONE);

    //randomly generates the input for kangaroo simulation
    public void randomStart(ActionEvent actionEvent) {
        try{
            //set it to be random mode
            OutputController.simulationMode = OutputController.RANDOM_MODE;

            //randomly generates the input
            RandomInputController.randomlyGenerate();
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/output.fxml")));

            //Switches from simulation mode selection page to output page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    //manually enters the input for kangaroo simulation
    public void inputStart(ActionEvent actionEvent) {
        try{
            //set it to be manual input mode
            OutputController.simulationMode = OutputController.MANUAL_MODE;

            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/input.fxml")));

            //switches from simulation mode selection page to input page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void toSimPage(ActionEvent actionEvent) {
        //this button is not needed for simulation mode selection page. As it is already in that page
    }

    public void toHistoryPage(ActionEvent actionEvent) {

        try{
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/records.fxml")));

            //switches from simulation mode selection page to history page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toSettings(ActionEvent actionEvent) {
        try{
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/settings.fxml")));

            //switches from simulation mode selection page to setting page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quit(ActionEvent actionEvent) {
        //asks the user for confirmation to quit the program
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure to quit the program?");
        alert.showAndWait();

        //if confirms to quit the program
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

                //switches from simulation mode selection page to login page by fading
                Main.switchSceneByFading(newScene,rootMainmenu);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
