package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.FileAssociatedEntity;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.FileAssociatedEntityViewModel;

public abstract class FileAssociatedEntityService<T extends FileAssociatedEntity, V extends Exception, U extends FileAssociatedEntityViewModel<T>>
        extends Service<T, V, U> {
    protected FileMetadataService fileMetadataService;
    protected FileContentService fileContentService;

    protected FileAssociatedEntityService(FirebaseConnection firebaseConnection) {
        super(firebaseConnection);
        fileMetadataService = FileMetadataService.getInstance(firebaseConnection);
        fileContentService = FileContentService.getInstance(firebaseConnection);
    }

    public Future<Void, Exception> deleteAndUnlinkFile(String entityKey, String fileMetadataKey) {
        Future<Void, Exception> result = new Future<>();

        getById(entityKey, false)
                .onSuccess(entity -> {
                    if (entity.getFileMetadataKeys().contains(fileMetadataKey)) {
                        entity.getFileMetadataKeys().remove(fileMetadataKey);
                        save(entity)
                                .onSuccess(none -> fileMetadataService.deleteMetadataAndContent(fileMetadataKey)
                                        .onSuccess(result::complete)
                                        .onError(result::completeExceptionally)
                                )
                                .onError(result::completeExceptionally);
                    }
                    else {
                        result.complete(null);
                    }
                })
                .onError(result.onError());

        return result;
    }

    public Future<Void, Exception> createAndLinkFile(String entityKey,
                                                     FileMetadataViewModel fileMetadata,
                                                     FileContentViewModel fileContent) {
        Future<Void, Exception> result = new Future<>();

        fileContentService.save(fileContent)
                .onSuccess(none -> fileMetadataService.save(fileMetadata)
                        .onSuccess(none2 -> {
                            getById(entityKey, false)
                                    .onSuccess(entity -> {
                                        linkFile(entity, fileMetadata.getKey())
                                                .onSuccess(result::complete)
                                                .onError(result::completeExceptionally);
                                    })
                                    .onError(error -> revertFileCreation(error, fileMetadata.getKey(), result));
                        })
                        .onError(error -> revertFileCreation(error, fileMetadata.getKey(), result)))
                .onError(result::completeExceptionally);

        return result;
    }

    private Future<Void, Exception> linkFile(U viewModel, String fileMetadataKey) {
        Future<Void, Exception> result = new Future<>();

        if (!viewModel.getFileMetadataKeys().contains(fileMetadataKey)) {
            viewModel.getFileMetadataKeys().add(fileMetadataKey);
            save(viewModel)
                    .onSuccess(result::complete)
                    .onError(error -> revertFileCreation(error,
                            fileMetadataKey, result));
        }
        else {
            result.complete(null);
        }

        return result;
    }

    private void revertFileCreation(Exception exception,
                                    String fileMetadataKey,
                                    Future<Void, Exception> future) {
        fileMetadataService.deleteMetadataAndContent(fileMetadataKey);
        future.completeExceptionally(exception);
    }
}
