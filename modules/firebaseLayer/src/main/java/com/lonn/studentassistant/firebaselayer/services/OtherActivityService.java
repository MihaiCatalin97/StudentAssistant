package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.OneTimeClassAdapter;
import com.lonn.studentassistant.firebaselayer.adapters.OtherActivityAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.OTHER_ACTIVITIES;

public class OtherActivityService extends FileAssociatedEntityService<OtherActivity, Exception, OtherActivityViewModel> {
    private static OtherActivityService instance;

    private OtherActivityService(FirebaseConnection firebaseConnection) {
        super(firebaseConnection);
    }

    public static OtherActivityService getInstance(FirebaseConnection firebaseConnection) {
        if (instance == null) {
            instance = new OtherActivityService(firebaseConnection);
            instance.init();
        }

        return instance;
    }

    protected void init() {
        adapter = new OtherActivityAdapter();
    }

    @Override
    protected DatabaseTable<OtherActivity> getDatabaseTable() {
        return OTHER_ACTIVITIES;
    }
}
