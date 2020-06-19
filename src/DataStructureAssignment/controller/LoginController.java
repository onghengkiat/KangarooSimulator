package DataStructureAssignment.controller;

import DataStructureAssignment.Main;
import DataStructureAssignment.utils.ConnectionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button register;
    @FXML
    private Button login;
    @FXML
    private Button quit;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label errormsg;
    @FXML
    private AnchorPane rootLogin;

    private Connection con = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private Alert alert = new Alert(Alert.AlertType.NONE);

    private static String userName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //when login page is initialize, connects to the database
        con = ConnectionUtil.conDB1();

        //shows the database connection status
        //if connection to the database is failed
        if (con == null) {
            setLblError(Color.TOMATO, "Server Error : Check");
        } else {
            //else if the connection is success
            setLblError(Color.GREEN, "Server is up : Good to go");
        }

    }

    public void menuButtonHandler(ActionEvent actionEvent) throws IOException {
        //if users clicked on quit button
        if (actionEvent.getSource() == quit) {

            //asks the user for confirmation to quit the program
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure to quit the application?");
            alert.showAndWait();

            //if confirms to quit
            if (alert.getResult() == ButtonType.OK) {
                //quit the program
                Main.exit();
            }
        }

        //if users clicked on login button
        if (actionEvent.getSource() == login) {

            //check the login status
            //if successful then switches to next scene
            if (login().equals("Success")) {
                try{
                    Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/mainmenu.fxml")));

                    //switch from login page to main menu page by fading
                    Main.switchSceneByFading(newScene,rootLogin);
                    return;
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //if users clicked on register button
        if (actionEvent.getSource() == register) {
            try{
                Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/registration.fxml")));

                //switches from login page to registration page
                Main.switchScene(newScene);
                return;
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to check the login status
    private String login() {
        String status = "Success";
        userName = username.getText().toString();
        String passWord = password.getText().toString();

        //check is the user enters the username and password
        if (userName.isEmpty() || passWord.isEmpty()) {
            //else, asks them to input
            setLblError(Color.TOMATO, "Empty credentials");
            status = "Error";
        } else {
            //if they have input the username and password
            //select the query using the username and password from the remote MySQL database
            //BINARY is used so that it is case sensitive
            String sql = "SELECT * FROM users WHERE BINARY username = ? and BINARY password = ?";
            try {
                //executes the sql query
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, passWord);
                resultSet = preparedStatement.executeQuery();

                //checks the query is empty or not
                //if it is, means we could not find the username or password that matches the user input
                if (!resultSet.next()) {
                    setLblError(Color.TOMATO, "Enter Correct Username/Password");
                    status = "Error";
                } else {
                    //else , it is successful to login
                    setLblError(Color.GREEN, "Login Successful!");
                }
            } catch (SQLException ex) {
                //in case got any unpredictable error occurs
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }
        return status;
    }

    //method uses to set the error message
    private void setLblError(Color color, String text) {
        errormsg.setTextFill(color);
        errormsg.setText(text);
    }

    public static String getUserName() {
        return userName;
    }
}
