package pi.oliveiras_multimarcas.exceptions;

public class NoSuchException extends RuntimeException {

    public NoSuchException(String objectName){
        super(objectName + " não localizado");
    }
}
