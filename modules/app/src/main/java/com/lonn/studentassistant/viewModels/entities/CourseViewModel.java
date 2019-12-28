package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CourseViewModel extends DisciplineViewModel<Course> {
    public int pack;
    public CycleSpecialization cycleSpecialization;

    @Bindable
    public String getCourseType() {
        if (pack == 0) {
            return "Mandatory discipline";
        }
        else {
            return "Optional discipline (Pack " + pack + ")";
        }
    }
}
