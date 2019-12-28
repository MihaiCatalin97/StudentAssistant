package com.lonn.studentassistant.viewModels.adapters;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;

import java.util.ArrayList;

import static com.lonn.studentassistant.BR._all;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;
import static com.lonn.studentassistant.viewModels.entities.ProfessorViewModel.builder;

public class ProfessorAdapter extends ViewModelAdapter<Professor, ProfessorViewModel> {
    public ProfessorAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
        super(firebaseConnectedActivity);
    }

    public ProfessorViewModel adaptOne(Professor professor) {
        return (ProfessorViewModel) builder()
                .firstName(professor.getFirstName())
                .lastName(professor.getLastName())
                .cabinet(professor.getCabinet())
                .email(professor.getEmail())
                .phoneNumber(professor.getPhoneNumber())
                .professorImage(professor.getProfessorImage())
                .rank(professor.getLevel())
                .website(professor.getWebsite())
                .courses(new ArrayList<>())
                .build()
                .setKey(professor.getKey());
    }

    protected ProfessorViewModel resolveLinks(ProfessorViewModel professorViewModel, Professor professor) {
        CourseAdapter courseAdapter = new CourseAdapter(firebaseConnectedActivity);

        for (String courseId : professor.getCourses()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<Course>()
                            .databaseTable(COURSES)
                            .predicate(where(ID).equalTo(courseId))
                            .onSuccess(courses -> {
                                professorViewModel.courses.addAll(courseAdapter.adapt(courses, false));
                                professorViewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }

        return professorViewModel;
    }
}
