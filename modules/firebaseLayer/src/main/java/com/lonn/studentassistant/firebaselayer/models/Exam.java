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
public class Exam extends BaseEntity {
    private int day;
    private int startHour;
    private int endHour;
    private int pack;
    private String date;
    private String type;

    private String courseKey;
    private Set<String> professorKeys = new HashSet<>();
    private Set<String> groups = new HashSet<>();
    private Set<String> rooms = new HashSet<>();
}
