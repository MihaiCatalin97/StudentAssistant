package com.lonn.studentassistant.views.implementations;

import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.LaboratoryViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;
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
			entityView1.getModel().field1.compareTo(entityView2.getModel().field1);

	public static Comparator<EntityView> ASCENDING_LABORATORY_WEEK_COMPARATOR = (entityView1, entityView2) -> {
		LaboratoryViewModel laboratory1 = (LaboratoryViewModel) entityView1.getEntityViewModel();
		LaboratoryViewModel laboratory2 = (LaboratoryViewModel) entityView2.getEntityViewModel();

		return laboratory1.getWeekNumber() - laboratory2.getWeekNumber();
	};

	public static Comparator<EntityView> GRADE_ASCENDING_ID_DATE_GRADE = (entityView1, entityView2) -> {
		GradeViewModel grade1 = (GradeViewModel) entityView1.getEntityViewModel();
		GradeViewModel grade2 = (GradeViewModel) entityView2.getEntityViewModel();

		int idComparison = grade1.getStudentId().compareTo(grade2.getStudentId());
		int dateComparison;

		if (idComparison != 0) {
			return idComparison;
		}

		dateComparison = grade1.getDate().compareTo(grade2.getDate());
		if (dateComparison != 0) {
			return dateComparison;
		}

		return (int) ((grade1.getGrade() - grade2.getGrade()) * 10);
	};
}
