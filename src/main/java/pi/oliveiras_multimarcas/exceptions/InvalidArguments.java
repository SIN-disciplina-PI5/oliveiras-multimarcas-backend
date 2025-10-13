package pi.oliveiras_multimarcas.exceptions;

public class InvalidArguments extends RuntimeException {

    public InvalidArguments(String invalidParameter){
        super("Parâmetro inválido: "+ invalidParameter);
    }
}
