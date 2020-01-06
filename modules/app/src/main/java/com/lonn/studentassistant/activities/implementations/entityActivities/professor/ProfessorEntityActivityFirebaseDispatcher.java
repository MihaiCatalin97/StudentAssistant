package com.lonn.studentassistant.activities.implementations.entityActivities.professor;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.ProfessorAdapter;

import java.util.List;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;
import static java.util.Arrays.asList;

class ProfessorEntityActivityFirebaseDispatcher extends Dispatcher<Professor> {
    private ProfessorEntityActivityLayoutBinding binding;

    private ProfessorAdapter adapter;

    ProfessorEntityActivityFirebaseDispatcher(ProfessorEntityActivity entityActivity) {
        super(entityActivity);
        this.binding = entityActivity.binding;

        adapter = new ProfessorAdapter(entityActivity);
    }


    protected List<Object> getAggregatedItemsToCheck() {
        return asList(binding.getProfessor().getOneTimeClasses(),
                binding.getProfessor().getRecurringClasses(),
                binding.getProfessor().getCourses(),
                binding.getProfessor().getOtherActivities());
    }

    void loadAll() {
        if (shouldLoad()) {
            firebaseConnection.execute(new GetRequest<Professor>()
                    .databaseTable(PROFESSORS)
                    .predicate(where(ID).equalTo(binding.getProfessor().getKey()))
                    .onSuccess(receivedProfessors -> {
                        binding.setProfessor(adapter.adapt(receivedProfessors.get(0)));
                    })
                    .onError(error -> {
                        Log.e("Loading professor", error.getMessage());
                        entityActivity.showSnackBar("An error occurred while loading the professor.");
                    }));
        }
    }
}
