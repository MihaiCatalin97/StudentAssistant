package com.lonn.studentassistant.firebaselayer.businessLayer.services;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.FileContentAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.Future;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileContentViewModel;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTableContainer.FILE_CONTENT;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.fields.FileContentFields.FILE_METADATA_KEY;

public class FileContentService extends Service<FileContent, Exception, FileContentViewModel> {
    private static FileContentService instance;

    private FileContentService(FirebaseConnection firebaseConnection) {
        super(firebaseConnection);
    }

    public static FileContentService getInstance(FirebaseConnection firebaseConnection) {
        if (instance == null) {
            instance = new FileContentService(firebaseConnection);
            instance.init();
        }

        return instance;
    }

    protected void init(){
        super.init();
        adapter = new FileContentAdapter();
    }

    public Future<Void, Exception> deleteByMetadataKey(String metadataKey) {
        Future<Void, Exception> result = new Future<>();

        getByMetadataKey(metadataKey, false)
                .onSuccess(fileContent -> {
                    deleteById(fileContent.getKey())
                            .onSuccess(result::complete)
                            .onError(result::completeExceptionally);
                })
                .onError(result::completeExceptionally);

        return result;
    }

    public Future<FileContentViewModel, Exception> getByMetadataKey(String metadataKey, boolean subscribe) {
        Future<FileContentViewModel, Exception> result = new Future<>();

        firebaseConnection.execute(new GetRequest<FileContent, Exception>()
                .databaseTable(FILE_CONTENT)
                .subscribe(subscribe)
                .predicate(where(FILE_METADATA_KEY).equalTo(metadataKey))
                .onSuccess(fileContent -> {
                    if (fileContent == null || fileContent.size() != 1) {
                        result.completeExceptionally(new Exception("File content not found"));
                        return;
                    }

                    result.complete(transform(fileContent.get(0)));
                }));

        return result;
    }

    @Override
    public DatabaseTable<FileContent> getDatabaseTable() {
        return FILE_CONTENT;
    }

    protected PermissionLevel getPermissionLevel(FileContent fileContent) {
        return authenticationService.getPermissionLevel(fileContent);
    }
}
