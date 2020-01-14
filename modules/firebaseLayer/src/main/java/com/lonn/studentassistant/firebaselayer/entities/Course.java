package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Course extends Discipline {
    private Integer pack;
    private List<String> laboratories = new LinkedList<>();

    private List<CycleSpecializationYear> cycleSpecializationYears = new ArrayList<>();

    public void addCycleSpecializationYear(String cycleSpecializationYear) {
        this.cycleSpecializationYears.add(CycleSpecializationYear.valueOf(cycleSpecializationYear));
    }

    public Course addCycleSpecializationYear(CycleSpecializationYear cycleSpecializationYear) {
        this.cycleSpecializationYears.add(cycleSpecializationYear);
        return this;
    }
}
