package com.lonn.studentassistant.firebaselayer.models;

import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Course extends BaseEntity {
    private Integer year;
    private Integer semester;
    private Integer pack;
    private String courseName;
    private String description = "";
    private String website;
    private String scheduleLink;

    private CycleSpecializations cycleAndSpecialization;

    private Set<String> professors = new HashSet<>();
    private Set<String> scheduleClasses = new HashSet<>();
}
