package pi.oliveiras_multimarcas.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice // Indica que esta classe irá "aconselhar" os Controladores REST
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchException(
            NoSuchException ex,
            HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(), // Mensagem que você definiu (ex: "Agendamento não localizado")
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidArguments.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidArgumentsException(
            InvalidArguments ex,
            HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(), // Mensagem que você definiu (ex: "Parâmetro inválido: preço")
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}