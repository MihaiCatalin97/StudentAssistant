package com.lonn.studentassistant.firebaselayer.entities.enums;

import androidx.annotation.NonNull;

public enum Specialization {
	INFORMATICA("Informatica"),
	INGINERIA_SISTEMELOR_SOFT("Ingineria sistemelor soft"),
	LINGVISTICA_COMPUTATIONALA("Lingvistica computationala"),
	OPTIMIZARE_COMPUTATIONALA("Optimizare computationala"),
	SISTEME_DISTRIBUITE("Sisteme distribuite"),
	SECURITATEA_INFORMATIEI("Securitatea informatiei");

	String specializationName;

	Specialization(String specializationName) {
		this.specializationName = specializationName;
	}

	@Override
	@NonNull
	public String toString() {
		return specializationName;
	}
}
