package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.viewModels.entities.abstractions.EntityViewModel;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentViewModel extends EntityViewModel<Student> {
    @Bindable
    public String firstName, lastName, email, phoneNumber, website, group;
    @Bindable
    public int studentImage, year;
    public List<CourseViewModel> optionalCourses;
    public List<String> optionalCourseKeys;
    public List<OtherActivityViewModel> otherActivities;
    public List<String> otherActivityKeys;
    public CycleSpecialization cycleSpecialization;
}
