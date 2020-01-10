package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.FileMetadataAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_METADATA;

public class FileMetadataService extends Service<FileMetadata, Exception, FileMetadataViewModel> {
	private static FileMetadataService instance;

	private FileMetadataService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new FileMetadataAdapter();
	}

	public static FileMetadataService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new FileMetadataService(firebaseConnection);
		}

		return instance;
	}

	@Override
	protected DatabaseTable<FileMetadata> getDatabaseTable() {
		return FILE_METADATA;
	}
}