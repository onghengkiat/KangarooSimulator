package DataStructureAssignment;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class InfoStore {
    private String numberOfArea ="";
    private String numberOfColony ="";
    private String numberOfKangaroo ="";
    private String maleKangaroo ="";
    private String femaleKangaroo ="";
    private String numberOfColonyKangaroo ="";
    private String numberOfRemainingKangaroo ="";
    private String detailsOfArea ="";
    private String detailsOfKangaroo ="";

    private TextFlow detailsTextFlow;

    public InfoStore() {
        setNumberOfArea("");
        setNumberOfColony("");
        setNumberOfKangaroo("");
        setMaleKangaroo("");
        setFemaleKangaroo("");
        setNumberOfColonyKangaroo("");
        setNumberOfRemainingKangaroo("");
        setDetailsOfArea("");
        setDetailsOfKangaroo("");
    }

    public void setNumberOfArea(String numberOfArea) {
       this.numberOfArea = "\nNumber of Area: " + numberOfArea;
    }

    public void setNumberOfColony(String numberOfColony) {
       this.numberOfColony = "\nNumber of Colony: " + numberOfColony;
    }

    public void setNumberOfKangaroo(String numberOfKangaroo) {
       this.numberOfKangaroo = "\nNumber of Kangaroo: " + numberOfKangaroo;
    }

    public void setMaleKangaroo(String maleKangaroo) {
       this.maleKangaroo = "\nMale Kangaroo: " + maleKangaroo;
    }

    public void setFemaleKangaroo(String femaleKangaroo) {
       this.femaleKangaroo = "\nFemale Kangaroo: " + femaleKangaroo;
    }

    public void setNumberOfColonyKangaroo(String numberOfColonyKangaroo) {
       this.numberOfColonyKangaroo  = "\nColony Kangaroo: " + numberOfColonyKangaroo;
    }

    public void setNumberOfRemainingKangaroo(String numberOfRemainingKangaroo) {
       this.numberOfRemainingKangaroo = "\nRemaining Kangaroo: " + numberOfRemainingKangaroo;
    }

    public void setDetailsOfArea(String detailsOfArea) {
       this.detailsOfArea = "\n\nDetails of Area:" + detailsOfArea;
    }

    public void setDetailsOfKangaroo(String detailsOfKangaroo) {
       this.detailsOfKangaroo = "\n\nDetails of Kangaroo: " + detailsOfKangaroo;
    }

    public String getNumberOfArea() {
        return numberOfArea;
    }

    public String getNumberOfColony() {
        return numberOfColony;
    }

    public String getNumberOfKangaroo() {
        return numberOfKangaroo;
    }

    public String getMaleKangaroo() {
        return maleKangaroo;
    }

    public String getFemaleKangaroo() {
        return femaleKangaroo;
    }

    public String getNumberOfColonyKangaroo() {
        return numberOfColonyKangaroo;
    }

    public String getNumberOfRemainingKangaroo() {
        return numberOfRemainingKangaroo;
    }

    public String getDetailsOfArea() {
        return detailsOfArea;
    }

    public String getDetailsOfKangaroo() {
        return detailsOfKangaroo;
    }

    public TextFlow getDetailsTextFlow(){
        detailsTextFlow = new TextFlow();

        //set the font of the title of the detail text flow box
        Text summaryHeader = new Text("Summary ");
        summaryHeader.setFont(Font.font ("Verdana", FontWeight.BLACK, 14));
        summaryHeader.setFill(Color.WHITE);

        Text summary = new Text(getSummary());
        summary.setFill(Color.WHITE);

        //put the title of the details into the detailsTextFlowBox
        Text detailsOfArea = new Text(getDetailsOfArea());
        detailsOfArea.setFill(Color.WHITE);
        Text detailsOfKangaroo = new Text(getDetailsOfKangaroo());
        detailsOfKangaroo.setFill(Color.WHITE);
        detailsTextFlow.getChildren().addAll(summaryHeader,summary,detailsOfArea,detailsOfKangaroo);
        return detailsTextFlow;
    }

    public String getSummary(){
        return getNumberOfArea() + getNumberOfColony() + getNumberOfKangaroo() +
                getMaleKangaroo() + getFemaleKangaroo() + getNumberOfColonyKangaroo() +
                getNumberOfRemainingKangaroo() ;
    }

    @Override
    public String toString(){
        return getNumberOfArea() + getNumberOfColony() +getNumberOfKangaroo() + getMaleKangaroo() +
                getFemaleKangaroo() + getNumberOfColonyKangaroo() + getNumberOfRemainingKangaroo() +
                getDetailsOfArea() + getDetailsOfKangaroo();
    }
}
