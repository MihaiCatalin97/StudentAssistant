package com.lonn.scheduleparser.parsingServices.otherActivities;

import com.lonn.scheduleparser.parsingServices.abstractions.Repository;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;

public class OtherActivityRepository extends Repository<OtherActivity> {
    private static OtherActivityRepository instance;

    private OtherActivityRepository() {
    }

    public static OtherActivityRepository getInstance() {
        if (instance == null) {
            instance = new OtherActivityRepository();
        }
        return instance;
    }

    public OtherActivity findByScheduleLink(String scheduleLink) {
        for (OtherActivity otherActivity : entities) {
            if (otherActivity.getScheduleLink() != null &&
                    otherActivity.getScheduleLink().equals(scheduleLink)) {
                return otherActivity;
            }
        }

        return null;
    }
}
