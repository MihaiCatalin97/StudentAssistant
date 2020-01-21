package com.lonn.studentassistant.activities.implementations.entityActivities.laboratory;

import com.lonn.studentassistant.activities.abstractions.EntityActivityDispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.services.LaboratoryService;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.List;

import static com.lonn.studentassistant.BR.files;
import static com.lonn.studentassistant.BR.grades;

public class LaboratoryEntityActivityFirebaseDispatcher extends EntityActivityDispatcher<LaboratoryViewModel> {
	private static final Logger LOGGER = Logger.ofClass(LaboratoryEntityActivityFirebaseDispatcher.class);
	private BindableHashMap<FileMetadataViewModel> fileMap;
	private BindableHashMap<GradeViewModel> gradeMap;
	private LaboratoryEntityActivity entityActivity;

	LaboratoryEntityActivityFirebaseDispatcher(LaboratoryEntityActivity entityActivity) {
		super(entityActivity);
		this.entityActivity = entityActivity;

		fileMap = new BindableHashMap<>(entityActivity.getBinding(), files);
		gradeMap = new BindableHashMap<>(entityActivity.getBinding(), grades);
	}

	void loadAll(String laboratoryKey) {
		firebaseApi.getLaboratoryService()
				.getById(laboratoryKey, true)
				.onSuccess(laboratory -> {
					entityActivity.setActivityEntity(laboratory);

					removeNonExistingEntities(fileMap, laboratory.getFileMetadataKeys());
					removeNonExistingEntities(gradeMap, laboratory.getGradeKeys());

					loadGrades(laboratory.getGradeKeys());
					loadFiles(laboratory.getFileMetadataKeys());
				});
	}

	private void loadFiles(List<String> fileIds) {
		for (String fileId : fileIds) {
			firebaseApi.getFileMetadataService()
					.getById(fileId, true)
					.onSuccess(fileMap::put);
		}
	}

	private void loadGrades(List<String> gradeIds) {
		for (String gradeId : gradeIds) {
			firebaseApi.getGradeService()
					.getById(gradeId, true)
					.onSuccess(gradeMap::put);
		}
	}

	@Override
	public LaboratoryService getService() {
		return firebaseApi.getLaboratoryService();
	}

	@Override
	public String getEntityName() {
		return "laboratory";
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public void update(LaboratoryViewModel laboratory) {
		if (laboratory.getWeekNumber() != entityActivity.getActivityEntity().getWeekNumber()) {
			firebaseApi.getLaboratoryService()
					.getByCourseKeyAndWeek(laboratory.getCourseKey(), laboratory.getWeekNumber(), false)
					.onSuccess(lab -> {
						if (lab == null) {
							super.update(laboratory);
						}
						else {
							entityActivity.showSnackBar("This course already has a laboratory for this week", 2000);
						}
					});
		}
		else {
			super.update(laboratory);
		}
	}
}
