package com.lonn.studentassistant.firebaselayer.models;

import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    private List<String> professors = new ArrayList<>();
    private List<String> scheduleClasses = new LinkedList<>();
}
