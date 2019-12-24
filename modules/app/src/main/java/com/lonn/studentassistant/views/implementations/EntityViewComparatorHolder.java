package com.lonn.studentassistant.views.implementations;

import com.lonn.studentassistant.common.interfaces.Comparator;
import com.lonn.studentassistant.firebaselayer.entities.Professor;

public class EntityViewComparatorHolder {
    public static Comparator<EntityView> ASCENDING_TITLE_COMPARATOR = (entityView1, entityView2) ->
            entityView1.getModel().field1.compareTo(entityView2.getModel().field1);

    public static Comparator<EntityView> ASCENDING_PROFESSOR_NAME_COMPARATOR = (entityView1, entityView2) -> {
        Professor professor1 = (Professor) entityView1.getEntity();
        Professor professor2 = (Professor) entityView2.getEntity();

        String professor1Name = professor1.getLastName() + professor1.getFirstName();
        String professor2Name = professor2.getLastName() + professor2.getFirstName();

        return professor1Name.compareTo(professor2Name);
    };
}
