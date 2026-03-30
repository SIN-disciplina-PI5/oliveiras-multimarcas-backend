package pi.oliveiras_multimarcas.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String objectName) {
        super(objectName + " não localizado");
    }
}
