package com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.CycleSpecializationYear;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class Course extends Discipline {
    private Integer pack;
    private List<String> laboratories = new LinkedList<>();
    private List<String> grades = new LinkedList<>();

    private List<CycleSpecializationYear> cycleSpecializationYears = new ArrayList<>();

    public Course addCycleSpecializationYear(CycleSpecializationYear cycleSpecializationYear) {
        this.cycleSpecializationYears.add(cycleSpecializationYear);
        return this;
    }

    public Course setFileMetadataKeys(List<String> fileMetadataKeys) {
        this.fileMetadataKeys = fileMetadataKeys;
        return this;
    }
}
