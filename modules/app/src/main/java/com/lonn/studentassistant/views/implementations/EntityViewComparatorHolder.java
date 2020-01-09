package com.lonn.studentassistant.views.implementations;

import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.functionalIntefaces.Comparator;

public class EntityViewComparatorHolder {
    public static Comparator<EntityView> ASCENDING_TITLE_COMPARATOR = (entityView1, entityView2) ->
            entityView1.getModel().field1.compareTo(entityView2.getModel().field1);

    public static Comparator<EntityView> ASCENDING_PROFESSOR_NAME_COMPARATOR = (entityView1, entityView2) -> {
        ProfessorViewModel professor1 = (ProfessorViewModel) entityView1.getEntityViewModel();
        ProfessorViewModel professor2 = (ProfessorViewModel) entityView2.getEntityViewModel();

        String professor1Name = professor1.getLastName() + professor1.getFirstName();
        String professor2Name = professor2.getLastName() + professor2.getFirstName();

        return professor1Name.compareTo(professor2Name);
    };

    public static Comparator<EntityView> ASCENDING_FIELD1_COMPARATOR = (entityView1, entityView2) ->
            entityView1.getModel().field1.compareTo(entityView2.getModel().field2);
    ;
}
