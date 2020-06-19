package DataStructureAssignment.myException;

/*
Exception for the number of area of input page
Number of area cannot be more than 20 and maximum of kangaroo in one area cannot be more than 10
Because it will looks very messy
Total number of the kangaroos in the map also cannot exceed total maximum number of kangaroos in areas
 */
public class ManyAreaOrKangarooException extends Exception{
    public ManyAreaOrKangarooException(){}
}
