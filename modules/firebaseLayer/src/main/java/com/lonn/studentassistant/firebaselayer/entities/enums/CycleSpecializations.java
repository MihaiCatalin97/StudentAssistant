package com.lonn.studentassistant.firebaselayer.entities.enums;

import androidx.annotation.NonNull;

import static com.lonn.studentassistant.firebaselayer.entities.enums.Cycles.LICENTA;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Cycles.MASTER;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specializations.INFORMATICA;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specializations.INGINERIA_SISTEMELOR_SOFT;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specializations.LINGVISTICA_COMPUTATIONALA;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specializations.OPTIMIZARE_COMPUTATIONALA;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specializations.SECURITATEA_INFORMATIEI;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specializations.SISTEME_DISTRIBUITE;

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
