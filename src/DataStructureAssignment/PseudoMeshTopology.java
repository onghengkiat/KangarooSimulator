package DataStructureAssignment;

/*
This class is used to set the position of the GUI area based on Mesh topology pattern.
The purpose of using this class is to make the GUI area visualization not be too messy when many areas.
The technical background:
Basically, I would make a big big circle in the map.
Then, I put the GUI area on the circle with constant angle difference between the areas.
This would make it to look like mesh topology
 */
public class PseudoMeshTopology {
    private double centreX;
    private double centreY;
    private double radius;
    private int angleDifferenceBetweenArea ;

    public PseudoMeshTopology(double centreX, double centreY, double radius, int numberOfArea) {
        this.centreX = centreX;
        this.centreY = centreY;
        this.radius = radius;

        //calculates the difference of angle between the areas in the circle.
        angleDifferenceBetweenArea = 360/numberOfArea;
    }

    public double horizontalDistance(int areaID){
        //radius * sin(degree) = o
        return radius*Math.sin(angleDifferenceBetweenArea*(areaID-1)*Math.PI/180);
    }

    public double verticalDistance(int areaID){
        //radius * cos(degree) = a
        return radius*Math.cos(angleDifferenceBetweenArea*(areaID-1)*Math.PI/180);
    }

    public double getCoordinateX(int areaID){
        return centreX - horizontalDistance(areaID);
    }

    public double getCoordinateY(int areaID){
        return centreY - verticalDistance(areaID);
    }
}
