package com.lonn.studentassistant.firebaselayer.models;

import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

import java.util.List;

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
    private List<String> professorKeys;
    private List<String> groups;
    private List<String> rooms;

    @Override
    public String getKey() {
        StringBuilder key = new StringBuilder();

        for (String room : rooms)
            key.append(room).append("~");

        key.append("_").append(day).append("_").append(startHour);

        return key.toString();
    }
}
