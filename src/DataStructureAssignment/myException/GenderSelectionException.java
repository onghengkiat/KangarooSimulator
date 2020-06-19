package DataStructureAssignment.myException;

/*
Exception for the gender selection in the input page.
If the user did not select any one of the gender, this exception would be thrown
Or if select both genders, this exception also would be thrown. We assume no bisexual kangaroos.
 */
public class GenderSelectionException extends Exception{
    public GenderSelectionException(){

    }
}
