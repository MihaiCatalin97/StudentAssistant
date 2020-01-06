package com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.OtherActivityEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.OtherActivityAdapter;

import java.util.List;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.OTHER_ACTIVITIES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;
import static java.util.Arrays.asList;

public class OtherActivityFirebaseDispatcher extends Dispatcher<OtherActivity> {
    private OtherActivityEntityActivityLayoutBinding binding;

    private OtherActivityAdapter adapter;

    OtherActivityFirebaseDispatcher(OtherActivityEntityActivity entityActivity) {
        super(entityActivity);
        this.binding = entityActivity.binding;

        adapter = new OtherActivityAdapter(entityActivity);
    }

    protected List<Object> getAggregatedItemsToCheck() {
        return asList(binding.getOtherActivity().getOneTimeClasses(),
                binding.getOtherActivity().getRecurringClasses(),
                binding.getOtherActivity().getProfessors());
    }

    void loadAll() {
        if (shouldLoad()) {
            firebaseConnection.execute(new GetRequest<OtherActivity>()
                    .databaseTable(OTHER_ACTIVITIES)
                    .predicate(where(ID).equalTo(binding.getOtherActivity().getKey()))
                    .onSuccess(receivedEntities -> {
                        binding.setOtherActivity(adapter.adapt(receivedEntities.get(0)));
                    })
                    .onError(error -> {
                        Log.e("Loading other activity", error.getMessage());
                        entityActivity.showSnackBar("An error occurred while loading activity.");
                    }));
        }
    }
}
