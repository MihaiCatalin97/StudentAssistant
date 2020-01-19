package com.lonn.studentassistant.activities.implementations.entityActivities.laboratory;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.databinding.LaboratoryEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;

import java.util.List;

import static com.lonn.studentassistant.BR.files;

public class LaboratoryEntityActivityFirebaseDispatcher extends Dispatcher {
	private LaboratoryEntityActivityLayoutBinding binding;
	private BindableHashMap<FileMetadataViewModel> fileMap;

	LaboratoryEntityActivityFirebaseDispatcher(LaboratoryEntityActivity activity) {
		super(activity);
		this.binding = activity.binding;
		fileMap = new BindableHashMap<>(binding, files);
	}

	void loadAll(String laboratoryKey) {
		firebaseApi.getLaboratoryService()
				.getById(laboratoryKey, true)
				.onSuccess(laboratory -> {
					binding.setLaboratory(laboratory);

					removeNonExistingEntities(fileMap, laboratory.getFileMetadataKeys());
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
}
