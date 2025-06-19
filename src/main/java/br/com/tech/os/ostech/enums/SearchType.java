package br.com.tech.os.ostech.enums;

public enum SearchType {

    NAME("name"),
    PHONE("phone"),
    EMAIL("email"),
    BLANK("blank");

    private final String value;

    SearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SearchType fromValue(String value) {
        for (SearchType type : SearchType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return BLANK;
    }

}

