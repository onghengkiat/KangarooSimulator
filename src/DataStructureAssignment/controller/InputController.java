package DataStructureAssignment.controller;

import DataStructureAssignment.*;
import DataStructureAssignment.myException.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

public class InputController  {
    @FXML
    private Label currentAreaLabel;
    @FXML
    private Label currentPathLabel;
    @FXML
    private Label currentKangarooLabel;

    @FXML
    private TextField areaNum;

    @FXML
    private TextField areaID;
    @FXML
    private TextField kangarooNum;
    @FXML
    private TextField foodNum;
    @FXML
    private TextField threshold;
    @FXML
    private TextField foodRegen;
    @FXML
    private TextField pathsNum;

    @FXML
    private TextField pathName;
    @FXML
    private TextField destination;
    @FXML
    private TextField obstacle;

    @FXML
    private TextField kangaNum;

    @FXML
    private TextField kangarooID;
    @FXML
    private TextField kangarooLocation;
    @FXML
    private TextField pouchCapacity;
    @FXML
    private TextField jumpingAbility;
    @FXML
    private CheckBox male;
    @FXML
    private CheckBox female;

    private Alert alert = new Alert(Alert.AlertType.NONE);

    private static Area[] map;
    private static Kangaroo[] kangarooList;
    private static int currentAreaInput = 0;
    private static int currentPathInput = 0;
    private static int currentKangarooInput = 0;
    private static int totalNumberOfPath = 0;

    public void toSimPage(ActionEvent actionEvent) {
        try {
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/mainmenu.fxml")));

            //switches from input page to simulation mode selection page
            Main.switchScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toHistory(ActionEvent actionEvent) {
        try {
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/records.fxml")));

            //switches from input page to history page
            Main.switchScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toSettings(ActionEvent actionEvent) {
        try {
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/settings.fxml")));

            //switches from input page to setting page
            Main.switchScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(ActionEvent actionEvent) {
        //asks user for confirmation to quit the program
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure to quit the program?");
        alert.showAndWait();

        //if confirms to quit
        if(alert.getResult() == ButtonType.OK){
            //quit the program
            Main.exit();
        }
    }

    public void logOut(ActionEvent actionEvent) {
        //asks user for confirmation to log out from the account
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure to log out from this account?");
        alert.showAndWait();

        //if confirms to log out
        if (alert.getResult() == ButtonType.OK) {
            try {
                Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/login.fxml")));

                //switches from input page to login page
                Main.switchScene(newScene);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //next button on page that inputs number of area
    //click next will move to page that inputs area details
    public void nextToAreaInput(ActionEvent actionEvent) {
        try{
            //get the number of area
            int numOfArea = Integer.parseInt(areaNum.getText());

            //number of area cannot be less than 2
            //else the simulation got no meaning
            if(numOfArea <= 1){
                throw new WeirdNumberException();
            }

            //number of area cannot be more than 20
            //else the maps will look very very messy, 20 already messy enough
            if(numOfArea > 20){
                throw new ManyAreaOrKangarooException();
            }

            //initialize the map array that stores all the areas
            map = new Area[numOfArea];

            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/input-area.fxml")));

            //switches scene from input number of area page to input details of areas page
            Main.switchScene(newScene);
        }catch (IOException e) {
            e.printStackTrace();
        }catch(NumberFormatException f){
            //in case user inputs wrong format
            //for example enters String in the field that should accept integer
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please input ONLY positive integer");
            alert.show();
        }catch (WeirdNumberException g) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please have more than 1 area");
            alert.show();
        } catch (ManyAreaOrKangarooException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please do not input more than 20 areas");
            alert.show();
        }
    }

    /*
    back button on page that inputs for details of areas
    click back will either return to the previous record for area details or
    if the current record is already the first record, then returns to page that inputs number of area
     */
    public void backToNumOfAreaInput(ActionEvent actionEvent) {
        //if the current record is the first record
        if(currentAreaInput == 0){
            try{
                Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/input.fxml")));

                //switches scene from page that input details of area to page that input number of area
                Main.switchScene(newScene);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            /*
            else if it is not the first record,
            return to the previous record,
            shows user what he/she has entered just now for the previous record
             */
            currentAreaInput --;
            //accumulates the total number of path
            totalNumberOfPath -= map[currentAreaInput].getPathNum();

            currentAreaLabel.setText("Area - " + (currentAreaInput + 1));
            kangarooNum.setText(Integer.toString(map[currentAreaInput].getMaxKangaroo()));
            foodNum.setText(Integer.toString(map[currentAreaInput].getFood()));
            threshold.setText(Integer.toString(map[currentAreaInput].getThreshold()));
            foodRegen.setText(Integer.toString(map[currentAreaInput].getRegenerateRate()));
            pathsNum.setText(Integer.toString(map[currentAreaInput].getPathNum()));
            areaID.setText(Integer.toString(map[currentAreaInput].getID()));
        }
    }

    /*
    next button on page that inputs for details of areas
    click next then the user inputs for the current area would be stored inside the map array
    if the current record is already the last record,
    then switches to next scene which is page that inputs for path details
     */
    public void nextToPathInput(ActionEvent actionEvent) {
        try {
            /*
            Some requirements must be met for the details of area:
            1)number of maximum kangaroo cannot be less than 1
            2)number of path cannot be equal to or larger than the number of area
            3)number of maximum kangaroo cannot be more than 10, else it would be very very messy
             */

            int numberOfPath = Integer.parseInt(pathsNum.getText());
            if(Integer.parseInt(kangarooNum.getText()) < 1 ){
                throw new WeirdNumberException();
            }

            if(numberOfPath >= map.length){
                throw new PathExceedsException();
            }

            if(Integer.parseInt(kangarooNum.getText()) > 10 ){
                throw new ManyAreaOrKangarooException();
            }

            //get the input from the fields, if any error occurs here, NumberFormatException would be thrown
            map[currentAreaInput] = new Area(Integer.parseInt(areaID.getText()), Integer.parseInt(foodNum.getText()),
                    Integer.parseInt(kangarooNum.getText()), Integer.parseInt(pathsNum.getText()),
                    Integer.parseInt(threshold.getText()),Integer.parseInt(foodRegen.getText()));

            //accumulates the total number of path
            totalNumberOfPath += map[currentAreaInput].getPathNum();

            //if this is the last prompt for area details input
            if(currentAreaInput >= map.length - 1 ){
                try{
                    //reset the currentAreaInput to 0 for path input later
                    currentAreaInput = 0;

                    //if total number of path is 0, skips the path input process
                    if(totalNumberOfPath == 0){
                        //straight switches from page that inputs details of area to page that inputs details of kangaroo
                        Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/input-kangaroo.fxml")));
                        Main.switchScene(newScene);
                    }else{
                        //else if path exists
                        //accumulate until which area has path to ask for input of path details for that area later
                        while(map[currentAreaInput].getPathNum() == 0){
                            currentAreaInput++;
                        }

                        //switches scene from page that inputs area details to page that inputs kangaroo details
                        Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/input-path.fxml")));
                        Main.switchScene(newScene);
                        return;
                    }
                }catch (IOException e) { }
            }else {
                //if it is not the last record
                //prompts the user to input details for the next area
                currentAreaInput++;

                //reset all the fields' contents to default
                currentAreaLabel.setText("Area - " + (currentAreaInput + 1));
                kangarooNum.setText("");
                foodNum.setText("");
                threshold.setText("");
                foodRegen.setText("");
                pathsNum.setText("");

                //area ID by default is currentAreaInput + 1
                //user is not allowed to edit it
                areaID.setText(currentAreaInput + 1 + "");
            }
        }catch(NumberFormatException e){
            //in case user inputs wrong format
            //for example enters String in the field that should accept integer
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please input ONLY integers for all the inputs");
            alert.show();
        }catch (WeirdNumberException g) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Max Num of Kangaroos MUST more than 0");
            alert.show();
        } catch (PathExceedsException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Num of path cannot exceeds number of area - 1");
            alert.show();
        } catch (ManyAreaOrKangarooException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please do not enter more than 10 max kangaroos");
            alert.show();
        }
    }

    /*
    back button on page that inputs for details of paths
    click back will either
    1)return to the previous record for path details OR
    2)if the current record is already the first path input for that area but the area is not the first area, then returns
    to the last path input for the previous area OR
    3)if the current record is already the first path input for first area, then returns to page that inputs details of area
     */
    public void backToAreaInput(ActionEvent actionEvent) {
        //checks is this the first path input for the current area
        //if it is not the first path input
        if(currentPathInput > 0){
            //returns to the previous record of path input for the current area
            currentPathInput --;
        }else{
            //if it is the first path input
            do{
                //backwards to the last path input for the previous area
                currentAreaInput --;

                //if there is no any previous area that prompts for path input
                if(currentAreaInput < 0){
                    try {
                        //reset the current area input,and users have to input from the first record again
                        currentAreaInput = 0;

                        //switches from page that inputs for path details to page that inputs for area details
                        Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/input-area.fxml")));
                        Main.switchScene(newScene);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(map[currentAreaInput].getPathNum() != 0 ){
                    //if there is any, then displays the record of the last path input for that area
                    currentPathInput = map[currentAreaInput].getPathNum()-1;
                }
            }while(map[currentAreaInput].getPathNum() == 0);
        }

        //shows user what he/she has entered just now for the previous record
        currentPathLabel.setText("Path - " + (currentPathInput + 1) + " for Area - " + (currentAreaInput + 1));
        pathName.setText(map[currentAreaInput].getPath()[currentPathInput].getID());
        destination.setText(Integer.toString(map[currentAreaInput].getPath()[currentPathInput].getDestination().getID()));
        obstacle.setText(Integer.toString(map[currentAreaInput].getPath()[currentPathInput].getObstacleHeight()));

    }

    /*
    next button on page that inputs for details of paths
    click next will stores the details of the user input for certain area in the Area object in the map array,
    if the current record is already the last record, then switches to the page that input for number of kangaroo
     */
    public void nextToKangarooNumInput(ActionEvent actionEvent) {
        try {

            if(Integer.parseInt(destination.getText()) - 1 == currentAreaInput){
                throw new WeirdNumberException();
            }
            //stores the details of the user input for certain area in the Area object in the map array
            //if users input any wrong format for in the fields, NumberFormatException would be thrown
            //source would be itself, so no need prompt input from user
            map[currentAreaInput].buildPath(new Path(pathName.getText(),map[currentAreaInput],
                    map[Integer.parseInt(destination.getText()) - 1],Integer.parseInt(obstacle.getText())), currentPathInput);

            //if this is the last prompt for path input for the certain area
            if(currentPathInput >= map[currentAreaInput].getPathNum() - 1) {

                //reset the currentPathInput to 0
                currentPathInput = 0;

                //checks if there are any area needs to prompt user for path details inputs
                do {
                    currentAreaInput++;

                    //if there is no any area that needs to prompt user for path details inputs
                    if(currentAreaInput >= map.length){
                        //reset the currentAreaInput to 0
                        currentAreaInput = 0;

                        //switches to the scene for entering number of kangaroos
                        Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/input-numKangaroo.fxml")));
                        Main.switchScene(newScene);
                        return;
                    }
                } while (map[currentAreaInput].getPathNum() == 0);
            }else{
                //if still needs to prompt users for path details input for the current area
                currentPathInput ++;
            }

            //reset the contents of the field to the default value
            //path id by default is currentPathInput + 1
            //users could not edit the path id
            currentPathLabel.setText("Path - " + (currentPathInput + 1) + " for Area - " + (currentAreaInput + 1));
            pathName.setText(currentPathInput + 1 + "");
            destination.setText("");
            obstacle.setText("");

        }catch(NumberFormatException e){
            //in case user inputs wrong format
            //for example enters String in the field that should accept integer
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please input ONLY integers for obstacle");
            alert.show();
        }catch (ArrayIndexOutOfBoundsException f){
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please enter ONLY valid areaID");
            alert.show();
        }catch(IOException g){} catch (WeirdNumberException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Destination cannot be itself");
            alert.show();
        }

    }

    /*
    back button on page that inputs for number of kangaroo
    click back will switches scene to first record of the page that inputs for path details,
    if the total number of path is 0, then returns to the last record of the page that inputs for area details
    */
    public void backToPathInput(ActionEvent actionEvent){
        //if no need to prompt for path input
        if(totalNumberOfPath == 0){
            try {
                //switches from page that inputs for number of kangaroo to page that inputs for area details
                //backs to the first area details entered by the user
                currentAreaInput = 0;
                Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/input-area.fxml")));
                Main.switchScene(newScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                //switches from page that inputs for number of kangaroo to page that inputs for path details
                //backs to the first path details entered by the user
                currentAreaInput = 0;
                while(map[currentAreaInput].getPathNum() == 0 ){
                    currentAreaInput ++;
                }
                currentPathInput = map[currentAreaInput].getPathNum() - 1;

                Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/input-path.fxml")));
                Main.switchScene(newScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    next button on page that inputs for number of kangaroo
    click next will switches scene to the page that inputs for kangaroo details
    */
    public void nextToKangarooInput(ActionEvent actionEvent)  {
        try {
            //number of kangaroo cannot less than 1
            int numberOfKangaroo = Integer.parseInt(kangaNum.getText());

            if(numberOfKangaroo < 1){
                throw new WeirdNumberException();
            }
            int maxKangaroo = 0 ;
            for(int i = 0 ; i< map.length; i++){
                maxKangaroo += map[i].getMaxKangaroo();
            }
            //number of kangaroo cannot more than total maximum number of kangaroo of all the areas
            if(numberOfKangaroo > maxKangaroo){
                throw new ManyAreaOrKangarooException();
            }

            //initialize a kangaroo list to store all the kangaroos
            kangarooList = new Kangaroo[numberOfKangaroo];

            //switches scene from the page that inputs for the number of kangaroo to page that inputs for kangaroo details
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/input-kangaroo.fxml")));
            Main.switchScene(newScene);
            return;
        }catch (WeirdNumberException h) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please input more than 0 kangaroo");
            alert.show();
        }catch(NumberFormatException e){
            //in case user inputs wrong format
            //for example enters String in the field that should accept integer
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please input ONLY positive integer");
            alert.show();
        }catch (ArrayIndexOutOfBoundsException f){
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please enter ONLY positive integer");
            alert.show();
        }catch(IOException g){} catch (ManyAreaOrKangarooException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("It cannot exceed total maximum number of kangaroo in areas");
            alert.show();
        }
    }



    /*
    back button on page that inputs for the kangaroo details
    if click back, it will either
    1) return to the previous record for the kangaroo details
    2) if it is already the first record, then switches to the page that inputs for number of kangaroo
    */
    public void backToKangarooNumInput(ActionEvent actionEvent) {
        //if it is the first record
        if(currentKangarooInput == 0){
            try{
                //switches scene from the page that inputs for kangaroo details to page that inputs for number of kangaroo
                Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/input-numKangaroo.fxml")));
                Main.switchScene(newScene);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //if it is not the first record
            //shows user what he/she has entered just now for the previous record
            currentKangarooInput --;
            currentKangarooLabel.setText("Kangaroo - " + (currentKangarooInput + 1));
            kangarooID.setText(kangarooList[currentKangarooInput].getID());
            kangarooLocation.setText(Integer.toString(kangarooList[currentKangarooInput].getLocation().getID()));
            pouchCapacity.setText(Integer.toString(kangarooList[currentKangarooInput].getMaxFoodInPouch()));
            jumpingAbility.setText(Integer.toString(kangarooList[currentKangarooInput].getJumpingAbility()));

            //the kangaroo returns the food to the area when its attribute is being reset
            kangarooList[currentKangarooInput].getLocation().putFood(kangarooList[currentKangarooInput].getFoodInPouch());
        }
    }

    /*
    next button on page that inputs for the kangaroo details
    if click next, it will either
    1) stores the details of the kangaroo entered by the user in the kangaroo list
    2) if it is already the last record, then generates the output and switches scene to the output page
    */
    public void nextToOutput(ActionEvent actionEvent) {
        try {
            //user cannot put the kangaroo into an area that is already full of kangaroos
            if(map[Integer.parseInt(kangarooLocation.getText()) - 1].isFull() ){
                throw new AreaFullException();
            }

            //if no gender is selected or both are selected
            if ((male.isSelected() && female.isSelected()) || (!male.isSelected() && !female.isSelected())){
                throw new GenderSelectionException();
            }
            //stores the info of the kanagroo in the kangaroo list based on the gender
            if(male.isSelected()) {
                kangarooList[currentKangarooInput] = new Kangaroo(kangarooID.getText(), Integer.parseInt(jumpingAbility.getText()),
                        map[Integer.parseInt(kangarooLocation.getText()) - 1], Kangaroo.MALE,
                        Integer.parseInt(pouchCapacity.getText()));
            }else if(female.isSelected()) {
                kangarooList[currentKangarooInput] = new Kangaroo(kangarooID.getText(), Integer.parseInt(jumpingAbility.getText()),
                        map[Integer.parseInt(kangarooLocation.getText()) - 1], Kangaroo.FEMALE,
                        Integer.parseInt(pouchCapacity.getText()));

                //accumulate the number of female kangaroo in the area
                kangarooList[currentKangarooInput].getLocation().addFemaleKangarooNum();
            }

            //accumulate the number of kangaroo in the area
            kangarooList[currentKangarooInput].getLocation().addKangarooNum();

            //when the kangaroo is assign to the area, it store the food from the area
            kangarooList[currentKangarooInput].storeFood();

            //if this is the last prompt for the kangaroo details
            if(currentKangarooInput >= kangarooList.length - 1){
                try{
                    //reset the currentKangarooInput to 0
                    currentKangarooInput = 0;

                    //switches scene from the page that inputs for kangaroo details to output page
                    Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/output.fxml")));
                    Main.switchScene(newScene);
                    return;
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                //if it is not the last record
                //reset the contents of all the field
                //and prompts user to input the details for next kangaroo
                currentKangarooInput++;
                currentKangarooLabel.setText("Kangaroo - " + (currentKangarooInput + 1));

                //kangaroo id by default is currentKangarooInput + 1 and not editable by user
                kangarooID.setText(currentKangarooInput + 1 + "");
                jumpingAbility.setText("");
                kangarooLocation.setText("");
                pouchCapacity.setText("");
                male.setSelected(false);
                female.setSelected(false);
            }

        }catch(NumberFormatException e){
            //in case user inputs wrong format
            //for example enters String in the field that should accept integer
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please input ONLY integers for food in pouch and jumping ability");
            alert.show();
        }catch (ArrayIndexOutOfBoundsException f){
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please enter ONLY valid location");
            alert.show();
        } catch (GenderSelectionException g) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please select ONE AND ONLY ONE gender");
            alert.show();
        } catch (AreaFullException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("The area selected is FULL");
            alert.show();
        }

    }

    public static Kangaroo [] getKangarooList(){
        return kangarooList;
    }

    public static Area[] getMap(){
        return map;
    }
}

