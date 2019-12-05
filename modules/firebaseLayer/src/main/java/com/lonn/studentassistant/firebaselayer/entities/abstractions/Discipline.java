package com.lonn.studentassistant.firebaselayer.entities.abstractions;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class Discipline extends BaseEntity{
    private int year;
    private int semester;
    private String disciplineName;
    private String description = "";
    private String website;
    private String scheduleLink;

    private List<String> professors = new LinkedList<>();
    private List<String> scheduleClasses = new LinkedList<>();

    public void addProfessor(String professorKey) {
        if (!professors.contains(professorKey)) {
            professors.add(professorKey);
        }
    }

    public void addScheduleClass(String scheduleClassKey) {
        if (!scheduleClasses.contains(scheduleClassKey)) {
            scheduleClasses.add(scheduleClassKey);
        }
    }
}
