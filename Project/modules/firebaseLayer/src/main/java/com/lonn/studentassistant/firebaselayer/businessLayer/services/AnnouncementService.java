package com.lonn.studentassistant.firebaselayer.businessLayer.services;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.AnnouncementAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AnnouncementViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Announcement;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTableContainer.ANNOUNCEMENTS;

public class AnnouncementService extends Service<Announcement, Exception, AnnouncementViewModel> {
    private static AnnouncementService instance;
    protected FileMetadataService fileMetadataService;

    private AnnouncementService(FirebaseConnection firebaseConnection) {
        super(firebaseConnection);
    }

    public static AnnouncementService getInstance(FirebaseConnection firebaseConnection) {
        if (instance == null) {
            instance = new AnnouncementService(firebaseConnection);
            instance.init();
        }

        return instance;
    }

    protected void init() {
        super.init();
        adapter = new AnnouncementAdapter();
        fileMetadataService = FileMetadataService.getInstance(firebaseConnection);
    }

    @Override
    protected DatabaseTable<Announcement> getDatabaseTable() {
        return ANNOUNCEMENTS;
    }

    protected PermissionLevel getPermissionLevel(Announcement announcement) {
        return authenticationService.getPermissionLevel(announcement);
    }
}
