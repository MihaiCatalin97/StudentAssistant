package com.lonn.studentassistant.firebaselayer.businessLayer.adapters;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AnnouncementViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Announcement;

public class AnnouncementAdapter extends ViewModelAdapter<Announcement, AnnouncementViewModel> {
    public AnnouncementViewModel adapt(Announcement announcement) {
        return AnnouncementViewModel.builder()
                .administratorKey(announcement.getAdministratorKey())
                .targetedGroups(announcement.getTargetedGroups())
                .title(announcement.getTitle())
                .message(announcement.getMessage())
                .date(announcement.getDate())
                .build()
                .setKey(announcement.getKey());
    }

    public Announcement adapt(AnnouncementViewModel announcementViewModel) {
        Announcement announcement = new Announcement()
                .setAdministratorKey(announcementViewModel.getAdministratorKey())
                .setTargetedGroups(announcementViewModel.getTargetedGroups())
                .setTitle(announcementViewModel.getTitle())
                .setMessage(announcementViewModel.getMessage())
                .setDate(announcementViewModel.getDate());

        if (announcementViewModel.getKey() != null) {
            announcement.setKey(announcementViewModel.getKey());
        }

        return announcement;
    }
}
