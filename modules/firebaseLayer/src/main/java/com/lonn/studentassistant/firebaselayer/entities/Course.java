package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Course extends Discipline {
    private Integer pack;

    private CycleSpecialization cycleAndSpecialization;

    public void setCycleAndSpecialization(String cycleAndSpecialization) {
        this.cycleAndSpecialization = CycleSpecialization.valueOf(cycleAndSpecialization);
    }

    public Course setCycleAndSpecialization(CycleSpecialization cycleAndSpecialization) {
        this.cycleAndSpecialization = cycleAndSpecialization;
        return this;
    }
}
