package DataStructureAssignment.controller;

import DataStructureAssignment.*;
import DataStructureAssignment.utils.ConnectionUtil;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class OutputController implements Initializable {

    @FXML
    private AnchorPane mapAnchorPane;

    @FXML
    private TextFlow detailsTextFlow;

    @FXML
    private TextFlow logTextFlow;

    @FXML
    private Label dayLabel;

    static int simulationMode ;
    final static int MANUAL_MODE = 0;
    final static int RANDOM_MODE = 1;

    private Alert alert = new Alert(Alert.AlertType.NONE);

    private Kangaroo [] kangarooList;
    private Area[] map;
    SequentialTransition st;
    private boolean saved;
    private LinkedList<InfoStore> infoStoreList = new LinkedList<>();

    //this stores the static unchanged path details to be stored in database if user saves it
    //and shows in the bottom of the summary text flow
    private String pathDetails = "";
    private String logText = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        saved = false;
        //check it is what simulation mode
        if(simulationMode == MANUAL_MODE) {
            map = InputController.getMap();
            kangarooList = InputController.getKangarooList();
        }
        else if(simulationMode == RANDOM_MODE){
            map = RandomInputController.getMap();
            kangarooList = RandomInputController.getKangarooList();
        }

        for(int i = 0 ; i < map.length ; i++){
            pathDetails += "\n\tPath for Area " + map[i].getID();
            for(int j = 0; j < map[i].getPath().length ; j ++){
                pathDetails += "\n\t\t"+map[i].getPath()[j].toString();
            }
        }

        detailsTextFlow.getChildren().add(0,new InfoStore().getDetailsTextFlow());
        Text pd = new Text("\nPath Details:"+pathDetails);
        pd.setFill(Color.WHITE);
        detailsTextFlow.getChildren().add(1,pd);

        //generates the gui map
        generateGUI();

        //gets the sequantial animation of the simulation
        st=new SequentialTransition();


        //and also gets and displays the log history of the kangaroo jumping
        calculateLog(st);
    }

    //generates the GUI image of the components at the initial condition
    public void generateGUI(){

        if(map.length > 5) {
            //if more than 5 areas need to be generated
            //generates the GUI area using the pattern of mesh topology
            Area.setPseudoMeshTopology(new PseudoMeshTopology(500, 400, 350, map.length));
            for(int i = 0 ; i < map.length ; i++) {
                map[i].generateMeshGuiArea();
                mapAnchorPane.getChildren().add(map[i].getGuiAreaWithLabel());
            }
        }else{
            //else if number of areas are less than or equal to 5
            //just simply randomly generates the GUI area
            for(int i = 0 ; i < map.length ; i++) {
                map[i].generateGuiArea(850,700,map,i);
                mapAnchorPane.getChildren().add(map[i].getGuiAreaWithLabel());
            }

        }

        //generates the GUI image for the paths and put them into the mapAnchorPane
        for(int i =0 ; i < map.length; i++){
            for(int j = 0 ; j < map[i].getPathNum() ; j++) {
                map[i].getPath()[j].generateGuiPath();
                mapAnchorPane.getChildren().add(map[i].getPath()[j].getGuiPath().getArrow());
            }
        }

        //generates the GUI image for the kangaroos and put them into the mapAnchorPane
        for(int i = 0; i < kangarooList.length ; i++){
            kangarooList[i].generateGuiKangaroo();
            mapAnchorPane.getChildren().add(kangarooList[i].getGuiKangaroo());
        }
    }

    public void calculateLog(SequentialTransition st){
        //generates and gets the log history
        int day = 0;

        //index to put in the log text in the TextFlow object
        int currentLogTextIndex = 0;

        //the simulation duration wont be longer than 30 days
        while(++day <= 30){

            //set the text that displays the current day
            Text dayText = new Text("--- Day " + day + " ---");
            dayText.setFont(Font.font("Verdana",FontWeight.BLACK,14));
            dayText.setFill(Color.WHITE);
            logTextFlow.getChildren().add(currentLogTextIndex,dayText);
            currentLogTextIndex++;

            logText += dayText.getText();

            //changes the day label on top left by fading it and showing it again
            st.getChildren().add(dayFade("Day " + day));
            st.getChildren().add(dayShow());
            st.getChildren().add(new PauseTransition(Duration.millis(1000)));

            //calculate the details of the simulation
            //shows it in the details text flow by fading it and showing it again
            calculateDetails();
            st.getChildren().add(detailFade(day));
            st.getChildren().add(detailShow());
            st.getChildren().add(new PauseTransition(Duration.millis(1000)));

            //gotJump variable is used to determine is there any kangaroo jump on that day
            boolean gotJump = false;
            String log = "";

            //gets the log history about what happens on that day
            for (int i = 0; i < kangarooList.length; i++) {
                //the kangaroo try to jumps
                Path jumpPath = kangarooList[i].jump();
                //jumpPath != null means the kangaroo did jump from an Area to another
                if (jumpPath != null) {
                    //appends it into the log history
                    log += "\nKangaroo - " + (i+1) + " jump from " + jumpPath.getSource().getID() + " to " +jumpPath.getDestination().getID();
                    gotJump = true;

                    //adds the animation of the kangaroo jumping into the SequantialTransition
                    st.getChildren().add(guiKangarooJump(jumpPath,kangarooList[i]));

                    //after the animation, regenerates the location of the kangaroo inside the area.
                    // Else it will be overlapping each other
                    //then moves it to there using PathTransition
                    st.getChildren().add(guiKangarooEntersArea(jumpPath,kangarooList[i]));
                    //makes the animation wait for 1 second before the next animation to start
                    st.getChildren().add(new PauseTransition(Duration.millis(1000)));

                    //if the destination is colony area and the colony area is still tomato colour which means
                    //the destination area becomes colony after this kangaroo enters
                    //so changes the colour of the area
                    if(jumpPath.getDestination().isColony() && jumpPath.getDestination().getGuiArea().getStroke().equals(Color.TOMATO)){
                        //it uses 1 second to change stroke colour from TOMATO to FORESTGREEN
                        StrokeTransition strokeTransition = new StrokeTransition(Duration.millis(1000));
                        strokeTransition.setFromValue(Color.TOMATO);
                        strokeTransition.setToValue(Color.FORESTGREEN);
                        strokeTransition.setShape(jumpPath.getDestination().getGuiArea());
                        st.getChildren().add(strokeTransition);

                        //after that wait for 1 second before playing next animation
                        st.getChildren().add(new PauseTransition(Duration.millis(1000)));
                    }

                }
            }

            //at the end of the day, the area will regenerate food
            for (Area area : map) {
                area.regenerateFood();
            }

            //if there is no any kangaroo jumps on that day
            if (!gotJump) {
                //simulation ends
                //all kangaroos are staying at home and not jumping on that day
                Text end = new Text("\nAll kangaroos are staying at home, stay home, stay safe");
                end.setFill(Color.WHITE);
                logTextFlow.getChildren().add(currentLogTextIndex,end);
                currentLogTextIndex++;

                logText += "\nAll kangaroos are staying at home, stay home, stay safe";
                //shows the simulation ends on the day label
                st.getChildren().add(dayFade("Day " + (day) + " - Ends"));
                st.getChildren().add(dayShow());
                st.getChildren().add(new PauseTransition(Duration.millis(1000)));
                break;
            }else{
                logText += log + "\n";
                //if there is kangaroo jumps on that day
                //appends the log history into the text flow and continues simulation on next day
                Text lg = new Text(log + "\n");
                lg.setFill(Color.WHITE);
                logTextFlow.getChildren().add(currentLogTextIndex,lg);
                currentLogTextIndex++;
            }
        }

        //appends these ending text into the log history
        Text end =new Text("\n--- SIMULATION ENDS ---\n");
        end.setFill(Color.WHITE);
        logTextFlow.getChildren().add(currentLogTextIndex,end);
    }


    /*
    1)calculate the total number of colony area
    2)calculate the total number of colony kangaroo
    3)calculate the total number of kangaroos that are not at colony area
    4)calculate the total number of female kangaroos
    5)calculate the total number of male kangaroos
    6)displays the details of each area and kangaroo
     */
    public void calculateDetails(){
        int numOfFemale = 0;
        int numOfMale = 0;
        int numOfColony = 0;
        int numOfColonyKangaroo = 0;

        //store the details of each area and kangaroo
        String areaDetails = "";
        String kangarooDetails = "";

        //iterates through the areas in the map
        for (Area area : map) {
            //if the area is colony
            if (area.isColony()) {
                //accumulate the number of colony kangaroo and area
                numOfColonyKangaroo += area.getNumOfKangaroo();
                numOfColony ++;
            }
            //accumulate the number of female and male kangaroo
            numOfFemale += area.getNumOfFemaleKangaroo();
            numOfMale += area.getNumOfKangaroo() - area.getNumOfFemaleKangaroo();

            //appends the area details
            areaDetails += "\n" + area;
        }

        //calculate the number of kangaroos that are not at colony area
        int numOfRemainingKangaroo = kangarooList.length - numOfColonyKangaroo;
        for (int i = 1; i <= kangarooList.length ; i ++) {
            //appends the area details
            kangarooDetails += "\nKangaroo - " + i + kangarooList[i-1];
        }

        //store all those informations into infoStore object
        InfoStore infoStore = new InfoStore();
        infoStore.setNumberOfArea(""+ map.length);
        infoStore.setNumberOfColony("" + numOfColony);
        infoStore.setNumberOfKangaroo("" + kangarooList.length);
        infoStore.setMaleKangaroo("" + numOfMale);
        infoStore.setFemaleKangaroo("" + numOfFemale);
        infoStore.setNumberOfColonyKangaroo("" + numOfColonyKangaroo);
        infoStore.setNumberOfRemainingKangaroo("" + numOfRemainingKangaroo);
        infoStore.setDetailsOfArea("" + areaDetails);
        infoStore.setDetailsOfKangaroo("" + kangarooDetails);
        infoStoreList.add(infoStore);
    }

    public void exit(ActionEvent actionEvent) {
        //asks user for confirmation to quit from the program
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure to quit the program?");
        alert.showAndWait();

        //if confirms to quit
        if(alert.getResult() == ButtonType.OK){
            //quit the program
            Main.exit();
        }
    }

    public void toSimPage(ActionEvent actionEvent) {
        //asks for confirmation to go back to the simulation mode selection page
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure to back to the main menu?");
        alert.showAndWait();

        //if confirms to go back the main menu
        if(alert.getResult() == ButtonType.OK) {
            try {
                Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/mainmenu.fxml")));
                //switches the scene from output page to simulation mode selection page which is the mainmenu
                Main.switchScene(newScene);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(ActionEvent actionEvent) {
        if(saved){
            //if user already saves this simulation history once before
            //inhibits him/her from saving same record again
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("You already saved this before!");
            alert.show();
        }else {
            //else if the user never saves it before
            boolean success = false;
            try{

                //firstly select the query from the first database to know how many number of records already there
                Connection conn = ConnectionUtil.conDB1();
                String sql = "SELECT * FROM records WHERE BINARY username = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, LoginController.getUserName());
                ResultSet resultSet = preparedStatement.executeQuery();
                int rowResult = 0;
                while(resultSet.next()){
                    rowResult++;
                }
                int recordId = rowResult+1;

                //get the current time as the simulation time
                Calendar calender = new GregorianCalendar();
                calender.setTimeInMillis(System.currentTimeMillis());

                //insert this record to the records table in the first database
                sql = "INSERT INTO records VALUES(?,?,?,?)";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, LoginController.getUserName());
                preparedStatement.setString(2, calender.getTime().toString());
                preparedStatement.setString(3, simulationMode == MANUAL_MODE ? "Manual Mode": "Random Mode");
                preparedStatement.setInt(4, recordId);
                preparedStatement.executeUpdate();

                int day = 0;
                for(InfoStore info : infoStoreList){
                    day ++;
                    //insert the log history into the second database
                    ConnectionUtil.insertAreaDetail(recordId, day ,info.getDetailsOfArea());
                    ConnectionUtil.insertKangarooDetail(recordId, day ,info.getDetailsOfKangaroo());
                    ConnectionUtil.insertSummary(recordId, day, info.getSummary());
                    ConnectionUtil.insertPathAndJumping(recordId, pathDetails,logText );
                }



                //if no exception is thrown, the save process is successful
                success = true;
            }catch(SQLException e){
                System.out.println(e);
            }

            //shows whether the action of saving the record is successful or not
            alert.setAlertType(Alert.AlertType.INFORMATION);

            if (success) {
                //if save successful
                //saved is set to true so that the user could not save the same record again
                saved = true;

                //tells the user he/she the data has been saved in the database
                alert.setContentText("Save Successful!");
                alert.show();
            } else {
                //else if failed to save it, tells him/her to try again later
                //probably due to database connection problem
                alert.setContentText("Failed to Save! Try again later.");
                alert.show();
            }
        }
    }

    //plays the animation of the kangaroo
    public void start(ActionEvent actionEvent) {
        st.play();
    }

    public void pause(ActionEvent actionEvent) {
        st.pause();
    }

    public PathTransition guiKangarooJump(Path path, Kangaroo kangaroo){
        //generates the animation of the kangaroo moves along the path
        PathTransition pt = new PathTransition();

        //the kangaroo will spend 1 second to move along the path
        pt.setDuration(Duration.millis(1000));

        //set the animation on the GUI kangaroo
        pt.setNode(kangaroo.getGuiKangaroo());

        //the GUI kangaroo will moves along the middle line of the path
        pt.setPath(path.getGuiPath().getMiddleLine());
        return pt;
    }

    public PathTransition guiKangarooEntersArea(Path path, Kangaroo kangaroo){
        PathTransition pt = new PathTransition();

        //the kangaroo will spend 0.5 second to move to somewhere in the area
        pt.setDuration(Duration.millis(500));
        //set the animation on the GUI kangaroo
        pt.setNode(kangaroo.getGuiKangaroo());

        //the path for the kangaroo to move is randomly generated
        double currentX = path.getCoorDestX();
        double currentY = path.getCoorDestY();
        double destinationX = kangaroo.randomCoorX(path.getDestination());
        double destinationY = kangaroo.randomCoorY(path.getDestination(),destinationX);
        pt.setPath(new Line(currentX,currentY,destinationX,destinationY));
        return pt;
    }

    public FadeTransition dayFade(String day){
        //faded the day label in one second from solid text to transparent
        FadeTransition fd = new FadeTransition();
        fd.setDuration(Duration.millis(1000));
        fd.setNode(dayLabel);
        fd.setFromValue(1);
        fd.setToValue(0);
        //at the end of the transition, change the day label text
        fd.setOnFinished(e->{
            dayLabel.setText(day);
        });
        return fd;
    }

    public FadeTransition dayShow(){
        //is used after dayFade
        //shows the day label in one second from transparent to solid text
        FadeTransition fd = new FadeTransition();
        fd.setDuration(Duration.millis(1000));
        fd.setNode(dayLabel);
        fd.setFromValue(0);
        fd.setToValue(1);
        return fd;
    }

    public FadeTransition detailFade(int day){
        //faded the details text
        FadeTransition fd = new FadeTransition();
        fd.setDuration(Duration.millis(1000));
        fd.setNode(detailsTextFlow);
        fd.setFromValue(1);
        fd.setToValue(0);
        //at the end of the transition, change the day label text
        fd.setOnFinished(e->{
            //remove the old one and replace it with the new one
            detailsTextFlow.getChildren().remove(0);
            detailsTextFlow.getChildren().add(0,infoStoreList.get(day-1).getDetailsTextFlow());
        });
        return fd;
    }

    public FadeTransition detailShow(){
        //is used after detailFade
        //shows the details text in 1 second from transparent to solid text
        FadeTransition fd = new FadeTransition();
        fd.setDuration(Duration.millis(1000));
        fd.setNode(detailsTextFlow);
        fd.setFromValue(0);
        fd.setToValue(1);
        return fd;

    }
}