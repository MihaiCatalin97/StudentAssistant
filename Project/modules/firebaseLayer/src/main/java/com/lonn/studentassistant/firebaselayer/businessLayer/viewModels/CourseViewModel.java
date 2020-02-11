package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.DisciplineViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.CycleSpecializationYear;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class CourseViewModel extends DisciplineViewModel<Course> {
    private int pack;
    private List<CycleSpecializationYear> cycleSpecializationYears;
    private List<String> laboratories;
    private List<String> grades;

    @Bindable
    public String getCourseType() {
        if (pack == 0) {
            return "Mandatory discipline";
        }
        else {
            return "Optional discipline (Pack " + pack + ")";
        }
    }

    @Override
    public CourseViewModel setKey(String key) {
        super.setKey(key);
        return this;
    }

    public boolean isForCycleAndYearAndSemester(CycleSpecializationYear cycleSpecializationYear,
                                                int semester) {
        for (CycleSpecializationYear cSY : cycleSpecializationYears) {
            if (cSY.equals(cycleSpecializationYear) && this.semester == semester) {
                return true;
            }
        }

        return false;
    }

    @Bindable
    public String getCyclesAndSpecializations() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < cycleSpecializationYears.size(); i++) {
            stringBuilder.append(cycleSpecializationYears.get(i).toString());

            if (i + 1 < cycleSpecializationYears.size()) {
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public CourseViewModel clone() {
        return (CourseViewModel) super.clone();
    }

    public CourseViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public CourseViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public CourseViewModel setWebsite(String website) {
        this.website = website;
        return this;
    }

    public CourseViewModel setSemester(int semester) {
        this.semester = semester;
        return this;
    }
}
