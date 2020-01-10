package com.lonn.studentassistant.firebaselayer.entities.enums;

import androidx.annotation.NonNull;

import lombok.Getter;

import static com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization.LICENTA_INFORMATICA;
import static com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization.MASTER_INGINERIA_SISTEMELOR_SOFT;
import static com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization.MASTER_LINGVISTICA_COMPUTATIONALA;
import static com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization.MASTER_OPTIMIZARE_COMPUTATIONALA;
import static com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization.MASTER_SECURITATEA_INFORMATIEI;
import static com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization.MASTER_SISTEME_DISTRIBUITE;

@Getter
public enum CycleSpecializationYear {
	LICENTA_INFORMATICA_ANUL_1(LICENTA_INFORMATICA, 1),
	LICENTA_INFORMATICA_ANUL_2(LICENTA_INFORMATICA, 2),
	LICENTA_INFORMATICA_ANUL_3(LICENTA_INFORMATICA, 3),

	MASTER_INGINERIA_SISTEMELOR_SOFT_ANUL_1(MASTER_INGINERIA_SISTEMELOR_SOFT, 1),
	MASTER_INGINERIA_SISTEMELOR_SOFT_ANUL_2(MASTER_INGINERIA_SISTEMELOR_SOFT, 2),

	MASTER_LINGVISTICA_COMPUTATIONALA_ANUL_1(MASTER_LINGVISTICA_COMPUTATIONALA, 1),
	MASTER_LINGVISTICA_COMPUTATIONALA_ANUL_2(MASTER_LINGVISTICA_COMPUTATIONALA, 2),

	MASTER_OPTIMIZARE_COMPUTATIONALA_ANUL_1(MASTER_OPTIMIZARE_COMPUTATIONALA, 1),
	MASTER_OPTIMIZARE_COMPUTATIONALA_ANUL_2(MASTER_OPTIMIZARE_COMPUTATIONALA, 2),

	MASTER_SISTEME_DISTRIBUITE_ANUL_1(MASTER_SISTEME_DISTRIBUITE, 1),
	MASTER_SISTEME_DISTRIBUITE_ANUL_2(MASTER_SISTEME_DISTRIBUITE, 2),

	MASTER_SECURITATEA_INFORMATIEI_ANUL_1(MASTER_SECURITATEA_INFORMATIEI, 1),
	MASTER_SECURITATEA_INFORMATIEI_ANUL_2(MASTER_SECURITATEA_INFORMATIEI, 2);

	CycleSpecialization cycleSpecialization;
	int year;

	CycleSpecializationYear(CycleSpecialization cycleSpecialization, int year) {
		this.cycleSpecialization = cycleSpecialization;
		this.year = year;
	}

	@Override
	@NonNull
	public String toString() {
		return cycleSpecialization.toString() + ", anul " + year;
	}
}
