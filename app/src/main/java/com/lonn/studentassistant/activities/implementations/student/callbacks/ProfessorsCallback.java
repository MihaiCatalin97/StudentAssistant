package com.lonn.studentassistant.activities.implementations.student.callbacks;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.entities.Student;

import java.util.Collections;

public class ProfessorsCallback extends AbstractDatabaseCallback<Professor>
{
    public ProfessorsCallback(StudentActivity activity)
    {
        super(activity);
    }

    public void processResponse(CreateResponse<Professor> response)
    {
        ((StudentActivity)activity).professorsBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(DeleteResponse<Professor> response)
    {
        ((StudentActivity)activity).professorsBaseCategory.delete(response.getItems().get(0));
    }

    public void processResponse(EditResponse<Professor> response)
    {
        ((StudentActivity)activity).professorsBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetByIdResponse<Professor> response)
    {
        ((StudentActivity)activity).professorsBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetAllResponse<Professor> response)
    {
        for(Professor professor : response.getItems())
            ((StudentActivity)activity).professorsBaseCategory.addOrUpdate(professor);
    }
}
