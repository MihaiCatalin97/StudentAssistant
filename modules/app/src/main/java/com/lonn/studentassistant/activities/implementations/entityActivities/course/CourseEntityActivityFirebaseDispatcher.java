package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.CourseAdapter;

import java.util.List;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;
import static java.util.Arrays.asList;

public class CourseEntityActivityFirebaseDispatcher extends Dispatcher<Course> {
    private CourseEntityActivityLayoutBinding binding;

    private CourseAdapter courseAdapter;

    CourseEntityActivityFirebaseDispatcher(CourseEntityActivity entityActivity) {
        super(entityActivity);
        this.binding = entityActivity.binding;

        courseAdapter = new CourseAdapter(entityActivity);
    }

    protected List<Object> getAggregatedItemsToCheck() {
        return asList(binding.getCourse().getOneTimeClasses(),
                binding.getCourse().getRecurringClasses(),
                binding.getCourse().getProfessors());
    }

    void loadAll() {
        if (shouldLoad()) {
            firebaseConnection.execute(new GetRequest<Course>()
                    .databaseTable(COURSES)
                    .predicate(where(ID).equalTo(binding.getCourse().getKey()))
                    .onSuccess(receivedEntities -> {
                        binding.setCourse(courseAdapter.adapt(receivedEntities.get(0)));
                    })
                    .onError(error -> {
                        Log.e("Loading course", error.getMessage());
                        entityActivity.showSnackBar("An error occurred while loading the course.");
                    }));
        }
    }
}
