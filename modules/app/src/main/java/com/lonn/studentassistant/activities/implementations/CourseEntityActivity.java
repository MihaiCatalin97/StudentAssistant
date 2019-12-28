package com.lonn.studentassistant.activities.implementations;

import android.os.Bundle;

import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.predicates.fields.RecurringClassField;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.entities.CourseViewModel;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;
import com.lonn.studentassistant.viewModels.entities.ScheduleClassViewModel;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;

import java.util.List;

import static com.lonn.studentassistant.BR._all;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.RECURRING_CLASSES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.ProfessorField.ID;

public class CourseEntityActivity extends FirebaseConnectedActivity {
    public ScrollViewCategory<Professor, ProfessorViewModel> professorBaseCategory;
    public ScrollViewCategory<ScheduleClass, ScheduleClassViewModel> scheduleBaseCategory;
    private CourseViewModel courseViewModel;
    private CourseEntityActivityLayoutBinding binding;
    private boolean loadedProfessors = false;
    private boolean editPrivilege;

    @Override
    public void onStart() {
        super.onStart();

        if (!loadedProfessors) {
            loadedProfessors = true;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FirebaseAuth.getInstance().signOut();
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.course_entity_activity_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            courseViewModel = (CourseViewModel) getIntent().getExtras()
                    .getSerializable("entityViewModel");

            if (courseViewModel != null) {
                binding.setCourse(courseViewModel);
            }
        }
    }

    private void loadProfessors(List<String> professorIds) {
        for (String professorId : professorIds) {
            firebaseConnection.execute(new GetRequest<Professor>()
                    .databaseTable(DatabaseTableContainer.PROFESSORS)
                    .predicate(where(ID)
                            .equalTo(professorId))
                    .onSuccess(professors -> {
//                        courseViewModel.professors.addAll(professors);
                        courseViewModel.notifyPropertyChanged(_all);
                    })
                    .subscribe(false));
        }
    }

    private void loadSchedule(List<String> scheduleClassIds) {
        for (String scheduleClassId : scheduleClassIds) {
            firebaseConnection.execute(new GetRequest<RecurringClass>()
                    .databaseTable(RECURRING_CLASSES)
                    .predicate(where(RecurringClassField.ID)
                            .equalTo(scheduleClassId))
                    .onSuccess(scheduleClasses -> {
//                        courseViewModel.scheduleClasses.addAll(scheduleClasses);
                        courseViewModel.notifyPropertyChanged(_all);
                    })
                    .subscribe(false));
        }
    }
}
