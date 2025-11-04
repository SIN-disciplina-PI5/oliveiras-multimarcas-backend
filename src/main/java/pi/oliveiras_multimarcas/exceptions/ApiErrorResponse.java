package pi.oliveiras_multimarcas.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.Instant;

@Getter
public class ApiErrorResponse {

  private final Instant timestamp;
  private final int status;
  private final String error;
  private final String message;
  private final String path; // Opcional, mas útil

  public ApiErrorResponse(HttpStatus status, String message, String path) {
    this.timestamp = Instant.now();
    this.status = status.value();
    this.error = status.getReasonPhrase();
    this.message = message;
    this.path = path;
  }
}