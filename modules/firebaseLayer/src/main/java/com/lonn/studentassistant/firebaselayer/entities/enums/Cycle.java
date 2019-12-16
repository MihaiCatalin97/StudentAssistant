package com.lonn.studentassistant.firebaselayer.entities.enums;

import androidx.annotation.NonNull;

public enum Cycle {
    LICENTA("Licenta"),
    MASTER("Master");

    String cycleName;

    Cycle(String cycleName) {
        this.cycleName = cycleName;
    }

    @Override
    @NonNull
    public String toString() {
        return cycleName;
    }
}
