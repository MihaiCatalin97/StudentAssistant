package com.lonn.scheduleparser.repositories;

import com.lonn.scheduleparser.mergers.OtherActivityMerger;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;

public class OtherActivityRepository extends Repository<OtherActivity> {
    private static OtherActivityRepository instance;

    private OtherActivityRepository() {
        merger = new OtherActivityMerger();
    }

    public static OtherActivityRepository getInstance() {
        if (instance == null) {
            instance = new OtherActivityRepository();
        }
        return instance;
    }

    public OtherActivity findByScheduleLink(String scheduleLink) {
        for (OtherActivity otherActivity : entities) {
            if (otherActivity.getScheduleLink().equals(scheduleLink)) {
                return otherActivity;
            }
        }

        return null;
    }
}
