package com.lonn.studentassistant.firebaselayer.entities.abstractions;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class ScheduleClass extends BaseEntity {
    private Integer startHour;
    private Integer endHour;
    private Integer pack;
    private String type;
    private String parity;

    private String discipline;
    private List<String> rooms = new LinkedList<>();
    private List<String> professors = new LinkedList<>();
    private List<String> groups = new LinkedList<>();

    public void addRoom(String roomKey) {
        if (!rooms.contains(roomKey)) {
            rooms.add(roomKey);
        }
    }

    public void addProfessor(String professorKey) {
        if (!professors.contains(professorKey)) {
            professors.add(professorKey);
        }
    }

    public void addGroup(String groupKey) {
        if (!groups.contains(groupKey)) {
            groups.add(groupKey);
        }
    }
}
