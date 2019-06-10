package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.views.abstractions.EntityView;

public interface IEntityActivity<T extends BaseEntity>
{
    EntityView<T> getEntityViewInstance(T entity);
}
