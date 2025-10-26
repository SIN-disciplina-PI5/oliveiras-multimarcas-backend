package pi.oliveiras_multimarcas.models.enums;

public enum UserPosition {
    ADMIN("admin"),
    USER("user");

    private String position;

    UserPosition(String position){
        this.position = position;
    }

    public String getPosition(){
        return position;
    }
}