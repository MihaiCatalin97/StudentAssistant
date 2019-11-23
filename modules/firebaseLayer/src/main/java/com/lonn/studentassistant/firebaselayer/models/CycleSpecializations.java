package com.lonn.studentassistant.firebaselayer.models;

import androidx.annotation.NonNull;

import static com.lonn.studentassistant.firebaselayer.models.Cycles.LICENTA;
import static com.lonn.studentassistant.firebaselayer.models.Cycles.MASTER;
import static com.lonn.studentassistant.firebaselayer.models.Specializations.INFORMATICA;
import static com.lonn.studentassistant.firebaselayer.models.Specializations.INGINERIA_SISTEMELOR_SOFT;
import static com.lonn.studentassistant.firebaselayer.models.Specializations.LINGVISTICA_COMPUTATIONALA;
import static com.lonn.studentassistant.firebaselayer.models.Specializations.OPTIMIZARE_COMPUTATIONALA;
import static com.lonn.studentassistant.firebaselayer.models.Specializations.SECURITATEA_INFORMATIEI;
import static com.lonn.studentassistant.firebaselayer.models.Specializations.SISTEME_DISTRIBUITE;

public enum CycleSpecializations {
    LICENTA_INFORMATICA(LICENTA, INFORMATICA),
    MASTER_INGINERIA_SISTEMELOR_SOFT(MASTER, INGINERIA_SISTEMELOR_SOFT),
    MASTER_LINGVISTICA_COMPUTATIONALA(MASTER, LINGVISTICA_COMPUTATIONALA),
    MASTER_OPTIMIZARE_COMPUTATIONALA(MASTER, OPTIMIZARE_COMPUTATIONALA),
    MASTER_SISTEME_DISTRIBUITE(MASTER, SISTEME_DISTRIBUITE),
    MASTER_SECURITATEA_INFORMATIEI(MASTER, SECURITATEA_INFORMATIEI);

    Cycles cycle;
    Specializations specialization;

    CycleSpecializations(Cycles cycle, Specializations specialization) {
        this.cycle = cycle;
        this.specialization = specialization;
    }

    @Override
    @NonNull
    public String toString() {
        if (cycle == LICENTA) {
            return specialization.toString();
        }
        return cycle.toString() + " " + specialization.toString();
    }
}
