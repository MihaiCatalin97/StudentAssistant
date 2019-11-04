package com.lonn.studentassistant.firebaselayer.models;

import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ScheduleClass extends BaseEntity {
    private int day;
    private int startHour;
    private int endHour;
    private int pack;
    private String type;
    private String parity;

    private String courseKey;
    private List<String> rooms;
    private List<String> professorKeys;
    private List<String> groups;

    @Override
    public String getKey() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < rooms.size(); i++) {
            result.append(rooms.get(i));

            if (i + 1 < rooms.size()) {
                result.append("-");
            }
        }

        return result.toString() + "_" + day + "_" + startHour;
    }
}
