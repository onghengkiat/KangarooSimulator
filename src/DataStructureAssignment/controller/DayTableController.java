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

public class DayTableController implements Initializable {
    @FXML
    private AnchorPane rootDayTable;
    @FXML
    private Label timeStamp;
    @FXML
    private VBox items;
    private Alert alert = new Alert(Alert.AlertType.NONE);


    private static int day;
    private static int recordID;
    private static String simulationDate;

    private Connection con = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        timeStamp.setText(simulationDate);

        //when this page is initialize, connects to the first database
        con = ConnectionUtil.conDB1();
        day = 0;

        String sql = "SELECT * FROM summaries WHERE BINARY username = ? AND id = ?";
        try {
            //executes the sql SELECT query
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, LoginController.getUserName());
            preparedStatement.setInt(2, recordID);
            resultSet = preparedStatement.executeQuery();

            //calculate the number of day of simulation of the record selected by the user in history first page
            while(resultSet.next()) {
                day++;
                //gets the designed node from the fxml file
                HBox node = FXMLLoader.load(getClass().getResource("../fxml/dayNode.fxml"));

                //makes hover effect
                node.setOnMouseEntered(event -> {
                    node.setStyle("-fx-background-color : #0A0E3F");
                });
                node.setOnMouseExited(event -> {
                    node.setStyle("-fx-background-color : #02030A");
                });

                //shows user the day in the record
                Label dayLabel = (Label) node.getChildren().get(0);
                dayLabel.setText(Integer.toString(resultSet.getInt(3)));

                //when toArea button is clicked, the user would be navigated to the page for area details viewing
                Button toArea = (Button) node.getChildren().get(1);
                toArea.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        try {
                            day = Integer.parseInt(dayLabel.getText());
                            //switches to the record viewing page
                            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/areaLog.fxml")));
                            Main.switchScene(newScene);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                });

                //when toKangaroo button is clicked, the user would be navigated to the page for kangaroo details viewing
                Button toKangaroo = (Button) node.getChildren().get(2);
                toKangaroo.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        try {
                            day = Integer.parseInt(dayLabel.getText());

                            //switches to the record viewing page
                            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/kangarooLog.fxml")));
                            Main.switchScene(newScene);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                });

                //when toPath button is clicked, the user would be navigated to the page for path details viewing
                Button toPath = (Button) node.getChildren().get(3);
                toPath.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        try {
                            //switches to the record viewing page
                            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/pathLog.fxml")));
                            Main.switchScene(newScene);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                });

                //when toJumping button is clicked, the user would be navigated to the page for jumping logs viewing
                Button toJumping = (Button) node.getChildren().get(4);
                toJumping.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        try {
                            //switches to the record viewing page
                            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/jumpingLog.fxml")));
                            Main.switchScene(newScene);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                });

                //when toSummary button is clicked, the user would be navigated to the page for summary viewing
                Button toSummary = (Button) node.getChildren().get(5);
                toSummary.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        try {
                            //get the record id
                            day = Integer.parseInt(dayLabel.getText());

                            //switches to the record viewing page
                            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/summaryLog.fxml")));
                            Main.switchScene(newScene);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                });

                //adds and displays the node in the nodeContainer
                items.getChildren().add(node);
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
                Main.switchSceneByFading(newScene,rootDayTable);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void backToHistory(ActionEvent actionEvent) {
        try{
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/records.fxml")));
            //switches to first page of the history page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void setRecordID(int recordID) {
        DayTableController.recordID = recordID;
    }

    public static void setSimulationDate(String simulationDate) {
        DayTableController.simulationDate = simulationDate;
    }

    public static int getDay() {
        return day;
    }

    public static int getRecordID() {
        return recordID;
    }

}
