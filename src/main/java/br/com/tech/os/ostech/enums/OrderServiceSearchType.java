package br.com.tech.os.ostech.enums;

public enum OrderServiceSearchType {

    CLIENT("client"),
    SMARTPHONE("smartphone"),
    BUDGET("budget"),
    STATUS("status"),
    DEPARTURE_DATE("departure_date"),
    BLANK("blank");

    private final String value;

    OrderServiceSearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderServiceSearchType fromValue(String value) {
        for (OrderServiceSearchType type : OrderServiceSearchType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return BLANK;
    }

}