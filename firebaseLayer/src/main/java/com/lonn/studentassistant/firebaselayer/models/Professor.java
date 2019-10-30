package com.lonn.studentassistant.firebaselayer.models;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Professor extends BaseEntity {
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
    public String computeKey() {
        return (level + "_" + firstName + "_" + lastName).replace(".", "~");
    }
}
