package com.lonn.studentassistant.firebaselayer.models;

import com.lonn.studentassistant.firebaselayer.models.abstractions.HashableEntity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Student extends HashableEntity implements Cloneable {
    private String studentId;
    private String lastName;
    private String firstName;
    private String fatherInitial;
    private String email;
    private String phoneNumber;
    private String group;
    private int year;
    private List<String> otherActivities = new ArrayList<>();
    private List<String> optionalCourses = new ArrayList<>();
    private List<String> grades = new ArrayList<>();

    @Override
    public String computeHashingString() {
        return getStudentId() + getFirstName()
                + getLastName() + getFatherInitial() + getPhoneNumber();
    }
}
