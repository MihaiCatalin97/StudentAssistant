package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.viewModels.entities.abstractions.EntityViewModel;

import java.util.Collection;
import java.util.List;

public abstract class Dispatcher<T extends BaseEntity> {
    protected FirebaseConnection firebaseConnection;
    protected EntityActivity<? extends EntityViewModel<T>> entityActivity;

    protected Dispatcher(EntityActivity<? extends EntityViewModel<T>> entityActivity) {
        this.entityActivity = entityActivity;
        firebaseConnection = entityActivity.getFirebaseConnection();

    }

    protected abstract List<Object> getAggregatedItemsToCheck();

    protected boolean shouldLoad() {
        for (Object objectToCheck : getAggregatedItemsToCheck()) {
            if (objectToCheck instanceof Collection) {
                if (isEmptyOrNull((Collection) objectToCheck)) {
                    return true;
                }
            }
            else if (objectToCheck == null) {
                return true;
            }
        }

        return false;
    }

    private boolean isEmptyOrNull(Collection<?> listOfEntities) {
        return listOfEntities == null ||
                listOfEntities.size() == 0;
    }
}
