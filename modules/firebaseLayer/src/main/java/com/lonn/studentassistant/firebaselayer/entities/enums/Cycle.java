package com.lonn.studentassistant.firebaselayer.entities.enums;

import androidx.annotation.NonNull;

public enum Cycle {
    LICENTA("Licenta", 3),
    MASTER("Master", 2);

    String cycleName;
    int numberOfYears;

    Cycle(String cycleName, int numberOfYears) {
        this.cycleName = cycleName;
        this.numberOfYears = numberOfYears;
    }

    @Override
    @NonNull
    public String toString() {
        return cycleName;
    }

    public int getNumberOfYears() {
        return numberOfYears;
    }
}
