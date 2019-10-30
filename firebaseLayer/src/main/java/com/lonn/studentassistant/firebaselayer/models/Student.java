package com.lonn.studentassistant.firebaselayer.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Student extends BaseEntity implements Cloneable {
    public String studentId;
    public String lastName;
    public String firstName;
    public String fatherInitial;
    public String email;
    public String phoneNumber;
    public String group;
    public int year;

    public String accountId;
    public List<String> otherActivities = new ArrayList<>();
    public List<String> optionalCourses = new ArrayList<>();
    public List<String> grades = new ArrayList<>();

    @Override
    public String computeKey() {
        return studentId;
    }
}
