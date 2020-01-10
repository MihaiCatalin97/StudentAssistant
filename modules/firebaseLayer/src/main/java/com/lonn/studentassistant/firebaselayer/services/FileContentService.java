package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.FileContentAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_CONTENT;

public class FileContentService extends Service<FileContent, Exception, FileContentViewModel> {
	private static FileContentService instance;

	private FileContentService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new FileContentAdapter();
	}

	public static FileContentService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new FileContentService(firebaseConnection);
		}

		return instance;
	}

	@Override
	public DatabaseTable<FileContent> getDatabaseTable() {
		return FILE_CONTENT;
	}
}
