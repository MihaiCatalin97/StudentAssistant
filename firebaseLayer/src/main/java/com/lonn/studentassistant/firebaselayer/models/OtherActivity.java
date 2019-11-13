package com.lonn.studentassistant.firebaselayer.models;

import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

import java.util.LinkedList;
import java.util.List;

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

    private List<String> professors = new LinkedList<>();
    private List<String> scheduleClasses = new LinkedList<>();

    @Override
    public String getKey() {
        return activityName.replace(".", "~");
    }
}
