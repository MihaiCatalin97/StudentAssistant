package com.lonn.studentassistant.firebaselayer.models;

import com.lonn.studentassistant.firebaselayer.models.abstractions.HashableEntity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

    private Set<String> courses = new HashSet<>();
    private Set<String> otherActivities = new HashSet<>();
    private Set<String> scheduleClasses = new HashSet<>();

    @Override
    public String computeHashingString() {
        return getFirstName() + getLastName() + getPhoneNumber();
    }
}
