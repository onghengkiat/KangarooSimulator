package DataStructureAssignment.controller;

import DataStructureAssignment.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomInputController {

    //create arrays to store all areas and kangaroos
    private static Area[] map ;
    private static Kangaroo [] kangarooList;

    public static void randomlyGenerate() {
        Random rnd = new Random();

        //this is used for generating kangaroos later
        int maxKangarooForMap = 0;

        //generate 2 to 20 areas
        int numOfArea = rnd.nextInt(19) + 2;

        //initialize the map
        map = new Area [numOfArea];

        for(int i = 0 ; i < numOfArea ; i++){
            //set the id by default to be i+1
            int id = i+1;

            //randomly generate the current food of the area from 1 to 20
            int food = rnd.nextInt(20)+1;

            //randomly set the maximum number of kangaroo in the area from 2 to 10
            int maxKangaroo = rnd.nextInt(9)+2;

            //add it into maxKangarooForMap
            maxKangarooForMap += maxKangaroo;

            //randomly set the number of path from that area from 1 to numOfArea - 1
            //the number of path should not be larger than or equal to the number of area
            int pathNum = rnd.nextInt(numOfArea-1)+1;

            //randomly set the threshold value of the area from 1 to maxKangaroo/2
            //threshold value should not be larger than the number of maxKangaroo
            int threshold = rnd.nextInt(maxKangaroo/2)+1;

            //randomly set the regenerate rate of the food of the area from 1 to 10
            int regenerateRate = rnd.nextInt(10) + 1;
            map[i] = new Area (id,food,maxKangaroo,pathNum,threshold,regenerateRate);
        }

        //store all the index of the areas into dest ArrayList
        ArrayList<Integer> dest = new ArrayList<>();
        for(int i= 0 ; i <numOfArea ; i ++){
            dest.add(i);
        }

        //iterate through the map, assigns path for the areas
        for(int i = 0 ; i < numOfArea ; i++) {

            //shuffle the dest arraylist
            Collections.shuffle(dest);

            //used to iterate through the dest arraylist
            int currentIndex = 0;

            //randomly generates the paths for the area
            for (int j = 0; j < map[i].getPathNum(); j++) {

                //same with Area ID, set it to be j+1
                String pathID = j + 1 + "";

                //source is the area itself
                Area source = map[i];

                //destination index would be current index of dest
                int destIndex = dest.get(currentIndex);
                currentIndex ++;

                //if the destination index equals to itself
                //take the another one area
                if(destIndex == i){
                    destIndex = dest.get(currentIndex);
                    currentIndex++;
                }
                Area destination = map[destIndex];

                //randomly generate obstacle height from 1 to 10
                int obstacleHeight = rnd.nextInt(10)+1;
                //inserts the path into the area
                map[i].buildPath( new Path(pathID, source, destination, obstacleHeight),j);
            }
        }

        //randomly generates number of kangaroo from 1 to maxKangarooForMap/2
        int numOfKangarooForMap = rnd.nextInt(maxKangarooForMap/2)+1;

        //initialize the kangaroo list
        kangarooList = new Kangaroo [numOfKangarooForMap];

        for(int i = 0 ; i < numOfKangarooForMap; i++){
            //same as areas and paths, set i + 1 as the id
            String kangarooID = i + 1 + "";

            //randomly generates the jumping ability for the kangaroos from 1 to 10
            int jumpingAbility = rnd.nextInt(10)+1;

            //randomly generates the location for the kangaroo
            //get the index of the location
            int locationIndex = rnd.nextInt(numOfArea);

            //if the area has already full with kangaroos
            while(map[locationIndex].getNumOfKangaroo() == map[locationIndex].getMaxKangaroo()){
                //generate again a new location index
                locationIndex = rnd.nextInt(numOfArea);
            }

            //get the Area from the map
            Area location = map[locationIndex];

            //randomly generates the gender for the kangaroo, 0 means male , 1 means female
            int gender = rnd.nextInt(2);

            //randomly generates the max food in pouch for the kangaroo, from 1 to 20
            int maxFoodInPouch = rnd.nextInt(20) + 1;

            //creates a new kangaroo
            Kangaroo newKangaroo =  new Kangaroo(kangarooID, jumpingAbility, location, gender, maxFoodInPouch);

            //accumulate the kangaroo counter in the area
            location.addKangarooNum();
            if(newKangaroo.getGender() == Kangaroo.FEMALE){
                location.addFemaleKangarooNum();
            }

            //put it into the kangaroo list
            kangarooList[i] = newKangaroo;
        }
    }

    public static Area[] getMap() {
        return map;
    }

    public static Kangaroo[] getKangarooList() {
        return kangarooList;
    }
}
