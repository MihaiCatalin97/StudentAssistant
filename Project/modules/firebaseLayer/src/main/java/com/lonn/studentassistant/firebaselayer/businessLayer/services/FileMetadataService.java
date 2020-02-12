package com.lonn.studentassistant.firebaselayer.businessLayer.services;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.FileMetadataAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.Future;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.GetRequest;

import java.util.LinkedList;
import java.util.List;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTableContainer.FILE_METADATA;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel.READ_PUBLIC;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.fields.FileMetadataFields.FILE_ASSOCIATED_ENTITY_KEY;

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

    public Future<List<FileMetadataViewModel>, Exception> getByAssociatedEntityKey(String associatedEntityKey, boolean subscribe) {
        Future<List<FileMetadataViewModel>, Exception> result = new Future<>();

        firebaseConnection.execute(new GetRequest<FileMetadata, Exception>()
                .databaseTable(getDatabaseTable())
                .predicate(where(FILE_ASSOCIATED_ENTITY_KEY).equalTo(associatedEntityKey))
                .subscribe(subscribe)
                .onSuccess(entities -> {
                    List<FileMetadata> visibleFiles = new LinkedList<>();

                    if (entities != null) {
                        for (FileMetadata file : entities) {
                            if (getPermissionLevel(file).isAtLeast(READ_PUBLIC)) {
                                visibleFiles.add(file);
                            }
                        }

                        result.complete(transform(visibleFiles));
                    }
                    else {
                        result.completeExceptionally(new Exception("Entity not found"));
                    }
                }));

        return result;
    }

    protected void init() {
        super.init();
        adapter = new FileMetadataAdapter();
        fileContentService = FileContentService.getInstance(firebaseConnection);
    }

    @Override
    protected DatabaseTable<FileMetadata> getDatabaseTable() {
        return FILE_METADATA;
    }

    protected PermissionLevel getPermissionLevel(FileMetadata fileMetadata) {
        return authenticationService.getPermissionLevel(fileMetadata);
    }
}