package com.lonn.studentassistant.firebaselayer.models;

import com.lonn.studentassistant.firebaselayer.Utils;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Student extends BaseEntity implements Cloneable {
    private String studentId;
    private String lastName;
    private String firstName;
    private String fatherInitial;
    private String email;
    private String phoneNumber;
    private String group;
    private int year;
    private String identificationHash;
    private String accountId;
    private List<String> otherActivities = new ArrayList<>();
    private List<String> optionalCourses = new ArrayList<>();
    private List<String> grades = new ArrayList<>();

    public Student setStudentId(String studentId) {
        this.studentId = studentId;
        updateIdentificationHash();
        return this;
    }

    public Student setLastName(String lastName) {
        this.lastName = lastName;
        updateIdentificationHash();
        return this;
    }

    public Student setFirstName(String firstName) {
        this.firstName = firstName;
        updateIdentificationHash();
        return this;
    }

    public Student setFatherInitial(String fatherInitial) {
        this.fatherInitial = fatherInitial;
        updateIdentificationHash();
        return this;
    }

    public Student setEmail(String email) {
        this.email = email;
        updateIdentificationHash();
        return this;
    }

    public Student setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        updateIdentificationHash();
        return this;
    }

    public Student setGroup(String group) {
        this.group = group;
        updateIdentificationHash();
        return this;
    }

    public Student setYear(int year) {
        this.year = year;
        updateIdentificationHash();
        return this;
    }

    @Override
    public String computeKey() {
        return studentId;
    }

    private void updateIdentificationHash() {
        String hashingString = getStudentId() + getLastName() + getFirstName() +
                getFatherInitial() + getEmail() + getPhoneNumber() + getGroup() + getYear();
        this.identificationHash = Utils.generateHashDigest(hashingString);
    }
}
