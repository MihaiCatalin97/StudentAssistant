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
public class OtherActivity extends BaseEntity {
    private int year;
    private int semester;
    private String activityName;
    private String description = "";
    private String website;
    private String type;
    private String scheduleLink;

    private Set<String> professors = new HashSet<>();
    private Set<String> scheduleClasses = new HashSet<>();
}
