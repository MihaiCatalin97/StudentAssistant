package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.HashableEntity;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Professor extends HashableEntity {
    private String professorImage;
    private String scheduleLink;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String level;
    private String website;
    private String cabinet;

    private List<String> courses = new LinkedList<>();
    private List<String> otherActivities = new LinkedList<>();
    private List<String> scheduleClasses = new LinkedList<>();

    @Override
    public String computeHashingString() {
        return getFirstName() + getLastName() + getPhoneNumber();
    }

    public void addCourse(String courseKey) {
        if (!courses.contains(courseKey)) {
            courses.add(courseKey);
        }
    }

    public void addOtherActivity(String otherActivityKey) {
        if (!otherActivities.contains(otherActivityKey)) {
            otherActivities.add(otherActivityKey);
        }
    }

    public void addScheduleClass(String scheduleClassKey) {
        if (!scheduleClasses.contains(scheduleClassKey)) {
            scheduleClasses.add(scheduleClassKey);
        }
    }
}
