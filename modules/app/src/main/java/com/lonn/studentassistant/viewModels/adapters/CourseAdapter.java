package com.lonn.studentassistant.viewModels.adapters;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.viewModels.adapters.abstractions.DisciplineAdapter;
import com.lonn.studentassistant.viewModels.entities.CourseViewModel;

public class CourseAdapter extends DisciplineAdapter<Course, CourseViewModel> {

    public CourseAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
        super(firebaseConnectedActivity);
    }

    public CourseViewModel adaptOne(Course course) {
        return super.adaptOne(CourseViewModel.builder()
                .pack(course.getPack())
                .cycleSpecialization(course.getCycleAndSpecialization())
                .build(), course);
    }
}
