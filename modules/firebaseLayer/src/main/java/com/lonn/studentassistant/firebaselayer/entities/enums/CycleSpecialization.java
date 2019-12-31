package com.lonn.studentassistant.firebaselayer.entities.enums;

import androidx.annotation.NonNull;

import static com.lonn.studentassistant.firebaselayer.entities.enums.Cycle.LICENTA;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Cycle.MASTER;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specialization.INFORMATICA;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specialization.INGINERIA_SISTEMELOR_SOFT;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specialization.LINGVISTICA_COMPUTATIONALA;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specialization.OPTIMIZARE_COMPUTATIONALA;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specialization.SECURITATEA_INFORMATIEI;
import static com.lonn.studentassistant.firebaselayer.entities.enums.Specialization.SISTEME_DISTRIBUITE;

public enum CycleSpecialization {
    LICENTA_INFORMATICA(LICENTA, INFORMATICA),
    MASTER_INGINERIA_SISTEMELOR_SOFT(MASTER, INGINERIA_SISTEMELOR_SOFT),
    MASTER_LINGVISTICA_COMPUTATIONALA(MASTER, LINGVISTICA_COMPUTATIONALA),
    MASTER_OPTIMIZARE_COMPUTATIONALA(MASTER, OPTIMIZARE_COMPUTATIONALA),
    MASTER_SISTEME_DISTRIBUITE(MASTER, SISTEME_DISTRIBUITE),
    MASTER_SECURITATEA_INFORMATIEI(MASTER, SECURITATEA_INFORMATIEI);

    Cycle cycle;
    Specialization specialization;

    CycleSpecialization(Cycle cycle, Specialization specialization) {
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

    public Cycle getCycle() {
        return cycle;
    }

    public String getInitials() {
        if (cycle.equals(MASTER)) {
            StringBuilder result = new StringBuilder(5);
            result.append("M");

            for (String specializationWord : specialization.toString().split(" ")) {
                result.append(specializationWord.toUpperCase().charAt(0));
            }
            return result.toString();
        }
        else {
            return "I";
        }
    }
}
