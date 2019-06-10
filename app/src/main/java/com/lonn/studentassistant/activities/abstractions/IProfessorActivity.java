package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.views.abstractions.EntityView;

public interface IProfessorActivity extends IEntityActivity<Professor>
{
    @Override
    EntityView<Professor> getEntityViewInstance(Professor entity);
}
