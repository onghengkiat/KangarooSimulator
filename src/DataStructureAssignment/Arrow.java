package DataStructureAssignment;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Arrow {
    private Line middleLine;
    private Line leftLine;
    private Line rightLine;
    private Group arrow;

    public Arrow(double startX, double startY, double endX, double endY, double radius1, double radius2) {
        //initialize the arrow
        arrow = new Group();

        //make the arrow pointing to the edge and not center
        double lengthOfMiddleLine = Math.hypot(startX - endX , startY - endY);

        /*
        The formula used is using the proportion between the points in the line
        If the line having point (x1,y1) and (x2,y2) on both edges and (x3,y3) on somewhere in between the line
        If the length of x1y1 to x3,y3 is 3, the length of x2,y2 to x3,y3 is 5
        ((x2*3 + x1*5)/(3+5) , (y2*3 + y1*5)/(3+5)) would equals to (x3,y3)
         */

        //trims the part inside the destination circle
        double proportion = radius2/lengthOfMiddleLine;
        double newEndX = startX * proportion + endX * (1-proportion);
        double newEndY = startY * proportion + endY * (1-proportion);

        //trims the part inside the source circle
        proportion = radius1/lengthOfMiddleLine;
        double newStartX = endX * proportion + startX *(1-proportion);
        double newStartY = endY * proportion + startY * (1-proportion);

        //creates the middle line of the arrow
        middleLine = new Line(newStartX,newStartY,newEndX,newEndY);

        double angle = Math.atan2((newEndY - newStartY), (newEndX - newStartX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        double lengthOfLine = 10;

        //right line
        double x1 = (-cos + sin) * lengthOfLine + newEndX;
        double y1 = (-sin - cos) * lengthOfLine + newEndY;
        //left line
        double x2 = (cos + sin) * lengthOfLine + newEndX;
        double y2 = (sin - cos) * lengthOfLine + newEndY;

        //creates the left line and right line that are bending from the middle line of the arrow
        leftLine = new Line(newEndX, newEndY, x1, y1);
        rightLine = new Line(newEndX , newEndY, x2,y2);

        //incorporate all 3 lines into a group
        arrow.getChildren().addAll(middleLine,leftLine,rightLine);
    }

    public Line getMiddleLine() {
        return middleLine;
    }

    public Group getArrow(){
        return arrow;
    }

    public double getCoorDestX(){
        return middleLine.getEndX();
    }

    public double getCoorDestY(){
        return middleLine.getEndY();
    }
}
