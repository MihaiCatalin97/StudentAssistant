package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.FileContentAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_CONTENT;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.FileContentFields.FILE_METADATA_KEY;

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
}
