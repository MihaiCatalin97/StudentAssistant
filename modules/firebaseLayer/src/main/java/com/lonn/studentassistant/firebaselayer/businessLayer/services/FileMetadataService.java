package com.lonn.studentassistant.firebaselayer.businessLayer.services;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.FileMetadataAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.Future;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTableContainer.FILE_METADATA;

public class FileMetadataService extends Service<FileMetadata, Exception, FileMetadataViewModel> {
	private static FileMetadataService instance;
	private FileContentService fileContentService;

	private FileMetadataService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public static FileMetadataService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new FileMetadataService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		super.init();
		adapter = new FileMetadataAdapter();
		fileContentService = FileContentService.getInstance(firebaseConnection);
	}

	public Future<Void, Exception> deleteMetadataAndContent(String fileMetadataKey) {
		Future<Void, Exception> result = new Future<>();

		deleteById(fileMetadataKey)
				.onSuccess(none -> {
					fileContentService.deleteByMetadataKey(fileMetadataKey)
							.onSuccess(result::complete)
							.onError(result::completeExceptionally);
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> saveMetadataAndContent(FileMetadataViewModel fileMetadata,
														  FileContentViewModel fileContent) {
		Future<Void, Exception> result = new Future<>();

		fileContentService.save(fileContent)
				.onSuccess(none -> save(fileMetadata)
						.onSuccess(result::complete)
						.onError(error -> {
							fileContentService.deleteById(fileContent.getKey());
							deleteById(fileMetadata.getKey());
							result.completeExceptionally(error);
						}))
				.onError(result::completeExceptionally);

		return result;
	}

	@Override
	protected DatabaseTable<FileMetadata> getDatabaseTable() {
		return FILE_METADATA;
	}

	protected PermissionLevel getPermissionLevel(FileMetadata fileMetadata) {
		return authenticationService.getPermissionLevel(fileMetadata);
	}
}