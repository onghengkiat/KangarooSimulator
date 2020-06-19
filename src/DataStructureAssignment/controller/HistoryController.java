package DataStructureAssignment.controller;

import DataStructureAssignment.Main;
import DataStructureAssignment.utils.ConnectionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    @FXML
    private AnchorPane rootHistory;
    @FXML
    private VBox nodeContainer = null;

    private Alert alert = new Alert(Alert.AlertType.NONE);

    private Connection con = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //when history page is initialize, connects to the first database
        con = ConnectionUtil.conDB1();

        String sql = "SELECT * FROM records WHERE BINARY username = ?";
        try {
            //executes the sql SELECT query
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, LoginController.getUserName());
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                //gets the designed node from the fxml file
                HBox node = FXMLLoader.load(getClass().getResource("../fxml/node.fxml"));

                //makes hover effect
                node.setOnMouseEntered(event -> {
                    node.setStyle("-fx-background-color : #0A0E3F");
                });
                node.setOnMouseExited(event -> {
                    node.setStyle("-fx-background-color : #02030A");
                });

                //shows user the simulation date, simulation mode and record id of the records
                Label simDate = (Label) node.getChildren().get(0);
                simDate.setText(resultSet.getString(2));

                Label simMode = (Label) node.getChildren().get(1);
                simMode.setText(resultSet.getString(3));

                Label id = (Label) node.getChildren().get(2);
                id.setText(Integer.toString(resultSet.getInt(4)));

                //when toLog button is clicked, the user would be navigated to the page for log details viewing
                Button toLog = (Button) node.getChildren().get(3);

                toLog.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        try {
                            //get the record id and simulation date
                            DayTableController.setRecordID(Integer.parseInt(id.getText()));
                            DayTableController.setSimulationDate(simDate.getText());

                            //switches to the record viewing page
                            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/dayTable.fxml")));
                            Main.switchScene(newScene);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                });

                //adds and displays the node in the nodeContainer
                nodeContainer.getChildren().add(node);
            }
        }catch (SQLException f) {
            f.printStackTrace();
        }catch (IOException e) {
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
                Main.switchSceneByFading(newScene,rootHistory);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}
