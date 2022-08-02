package exceptions;

public class MalformedCSVException extends Exception{

    public MalformedCSVException(){
        super("Make sure the provided CSV file contains the name of the entity");
    }
}
