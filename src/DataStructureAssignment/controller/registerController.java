package DataStructureAssignment.controller;

import DataStructureAssignment.Main;
import DataStructureAssignment.utils.ConnectionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class registerController implements Initializable {
    private Connection con = null;
    private PreparedStatement pst = null;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Button toLogin;
    @FXML
    private Label errormsg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //when the register page is initialized, get the connection to the database
        con = ConnectionUtil.conDB1();
    }

    public void Handler(ActionEvent actionEvent) throws IOException {
        //if users clicks on the exit to login button
        if (actionEvent.getSource() == toLogin) {
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/login.fxml")));
            //switches the scene from register page to login page
            Main.switchScene(newScene);
        }
    }

    @FXML
    private void add(ActionEvent actionEvent) throws SQLException {
        String userName = username.getText();
        String passWord = password.getText();
        String confirmPass = confirmPassword.getText();

        try {
            //checks the user already exists or not
            //binary is used so that it is case sensitive
            String sql = "SELECT * FROM users Where BINARY username = ?";
            //executes the sql query for getting any entry that matches the username
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            //check users enters all required field or not
            if (userName.isEmpty() || passWord.isEmpty()) {
                //if not asks them to enters all
                errormsg.setTextFill(Color.TOMATO);
                errormsg.setText("Error or empty credentials");

            } else if (resultSet.next()) {
                //checks the query is empty or not
                //if it is, means we found an entry where the username matches this username
                //so user exists already, register failed
                errormsg.setTextFill(Color.TOMATO);
                errormsg.setText("User already exists");

            } else if (!passWord.equals(confirmPass)) {
                //check the password is equal with confirm password or not
                //else ask him to check his password
                errormsg.setTextFill(Color.TOMATO);
                errormsg.setText("Please check your password");

            } else if (userName.equals(passWord) || userName.length() < 3 || passWord.length() < 6) {
                //to asks the user to have a stronger password for his account
                errormsg.setTextFill(Color.TOMATO);
                errormsg.setText("Try do this on your bank account?");

            } else {
                //if everything is alright, insert the user record into the database
                sql = "INSERT INTO users (username, password) VALUES ('" + userName + "', '" + passWord + "');";

                pst = con.prepareStatement(sql);
                int status = pst.executeUpdate();

                //checks if the registration process is success
                if (status == 1) {
                    errormsg.setTextFill(Color.GREEN);
                    errormsg.setText("Record Added");
                } else {
                    errormsg.setTextFill(Color.TOMATO);
                    errormsg.setText("Server error, please try later!");
                }

            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
}





