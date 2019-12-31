package com.lonn.studentassistant.viewModels.adapters;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.viewModels.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.viewModels.entities.StudentViewModel;

import static com.lonn.studentassistant.viewModels.entities.StudentViewModel.builder;

public class StudentAdapter extends ViewModelAdapter<Student, StudentViewModel> {
    public StudentAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
        super(firebaseConnectedActivity);
    }

    public StudentViewModel adaptOne(Student student) {
        return (StudentViewModel) builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .group(student.getGroup())
                .year(student.getYear())
                .cycleSpecialization(student.getCycleAndSpecialization())
                .build()
                .setKey(student.getKey());
    }

    protected StudentViewModel resolveLinks(StudentViewModel studentViewModel, Student student) {
        return studentViewModel;
    }
}
