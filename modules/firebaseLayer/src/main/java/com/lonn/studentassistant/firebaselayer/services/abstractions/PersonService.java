package com.lonn.studentassistant.firebaselayer.services.abstractions;

import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Person;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.services.FileAssociatedEntityService;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.PersonViewModel;

public abstract class PersonService<T extends Person, V extends PersonViewModel<T>> extends FileAssociatedEntityService<T, Exception, V> {
	protected PersonService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public Future<Void, Exception> addOrReplaceImage(String personId,
													 FileMetadataViewModel fileMetadata,
													 FileContentViewModel fileContent) {
		Future<Void, Exception> result = new Future<>();

		getById(personId, false)
				.onSuccess(person -> {
					String previousImage = person.getImageMetadataKey();
					person.setImageMetadataKey(fileMetadata.getKey());

					save(person)
							.onSuccess(none2 -> {
								fileMetadataService.saveMetadataAndContent(fileMetadata, fileContent)
										.onSuccess(result::complete)
										.onError(result::completeExceptionally);

								if (previousImage != null) {
									fileMetadataService.deleteMetadataAndContent(previousImage);
								}
							})
							.onError(result::completeExceptionally);
				})
				.onError(result::completeExceptionally);

		return result;
	}


	public Future<Void, Exception> deleteImage(String administratorId, String fileMetadataKey) {
		Future<Void, Exception> result = new Future<>();

		fileMetadataService.deleteMetadataAndContent(fileMetadataKey)
				.onSuccess(none -> getById(administratorId, false)
						.onSuccess(administrator -> {
							administrator.setImageMetadataKey(null);

							save(administrator)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						})
						.onError(result::completeExceptionally))
				.onError(result::completeExceptionally);

		return result;
	}
}
