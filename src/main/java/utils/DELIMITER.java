package utils;

public enum DELIMITER {

    COMMA(","),
    SEMICOLON(";"),
    TAB("\t");

    private final String value;

    private DELIMITER(String delimiter) {
        this.value = delimiter;
    }

    public String value(){
        return value;
    }

}
