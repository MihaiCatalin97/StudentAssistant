package com.lonn.studentassistant.activities.implementations.student.callbacks;

import com.lonn.studentassistant.activities.abstractions.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.OtherActivity;

public class OtherActivityCallback extends AbstractDatabaseCallback<OtherActivity>
{
    public OtherActivityCallback(StudentActivity activity)
    {
        super(activity);
    }

    public void processResponse(CreateResponse<OtherActivity> response)
    {
        ((StudentActivity)activity).otherActivitiesBaseCategory.addOrUpdate(response.getItems().get(0));
        ((StudentActivity)activity).otherActivitiesProfileCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(DeleteResponse<OtherActivity> response)
    {
        ((StudentActivity)activity).otherActivitiesBaseCategory.delete(response.getItems().get(0));
        ((StudentActivity)activity).otherActivitiesProfileCategory.delete(response.getItems().get(0));
    }

    public void processResponse(EditResponse<OtherActivity> response)
    {
        ((StudentActivity)activity).otherActivitiesBaseCategory.addOrUpdate(response.getItems().get(0));
        ((StudentActivity)activity).otherActivitiesProfileCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetByIdResponse<OtherActivity> response)
    {
        ((StudentActivity)activity).otherActivitiesBaseCategory.addOrUpdate(response.getItems().get(0));
        ((StudentActivity)activity).otherActivitiesProfileCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetAllResponse<OtherActivity> response)
    {
        for (OtherActivity otherActivity : response.getItems())
        {
            ((StudentActivity)activity).otherActivitiesBaseCategory.addOrUpdate(otherActivity);
            ((StudentActivity)activity).otherActivitiesProfileCategory.addOrUpdate(otherActivity);
        }
    }
}
