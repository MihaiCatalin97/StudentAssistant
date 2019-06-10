package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.views.entityViews.EntityView;

public interface IOtherActivityActivity extends IEntityActivity<OtherActivity>
{
    @Override
    EntityView<OtherActivity> getEntityViewInstance(OtherActivity entity);
}
