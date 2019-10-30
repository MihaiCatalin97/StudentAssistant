package com.lonn.studentassistant.firebaselayer.models;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Grade extends BaseEntity {
    private int value;
    private Date date;
    private String studentId;
    private String courseId;

    @Override
    public String computeKey() {
        return (studentId + "_" + courseId).replace(".", "~");
    }
}
