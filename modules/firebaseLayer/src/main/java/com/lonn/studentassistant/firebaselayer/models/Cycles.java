package com.lonn.studentassistant.firebaselayer.models;

import androidx.annotation.NonNull;

public enum Cycles {
    LICENTA("Licenta"),
    MASTER("Master");

    String cycleName;

    Cycles(String cycleName) {
        this.cycleName = cycleName;
    }

    @Override
    @NonNull
    public String toString() {
        return cycleName;
    }
}
