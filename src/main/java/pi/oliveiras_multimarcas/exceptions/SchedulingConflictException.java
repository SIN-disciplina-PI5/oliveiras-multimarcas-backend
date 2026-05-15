package pi.oliveiras_multimarcas.exceptions;

public class SchedulingConflictException extends RuntimeException {

    public SchedulingConflictException() {
        super("Já existe um agendamento para esta data e horário");
    }
}
