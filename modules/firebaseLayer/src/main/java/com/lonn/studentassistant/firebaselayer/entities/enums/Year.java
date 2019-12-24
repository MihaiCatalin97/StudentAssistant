package com.lonn.studentassistant.firebaselayer.entities.enums;

public enum Year {
    FIRST("First", 1),
    SECOND("Second", 2),
    THIRD("Third", 3);

    private String yearString;
    private int year;

    Year(String yearString, int year) {
        this.yearString = yearString;
        this.year = year;
    }

    public String getYearString() {
        return yearString;
    }

    public static Year valueOf(int year) {
        switch (year) {
            case 1:
                return FIRST;
            case 2:
                return SECOND;
            case 3:
                return THIRD;
            default:
                return FIRST;
        }
    }
}
