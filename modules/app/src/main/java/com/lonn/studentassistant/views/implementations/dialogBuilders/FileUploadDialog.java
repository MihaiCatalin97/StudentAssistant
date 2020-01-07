package com.lonn.studentassistant.views.implementations.dialogBuilders;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.requests.DeleteByIdRequest;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.utils.file.CustomFileReader;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.createChooser;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_CONTENT;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_METADATA;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public class FileUploadDialog extends Dialog {
    private static final int SELECT_FILE_REQUEST_CODE = 123;
    private static final int SNACK_BAR_ERROR_DURATION = 1000;
    private static final Logger LOGGER = Logger.ofClass(FileUploadDialog.class);
    private FirebaseConnectedActivity firebaseConnectedActivity;
    private String aggregatedEntityKey;
    private FileMetadata fileMetadata;
    private FileContent fileContent;
    private EditText fileTitleEditText;
    private EditText fileDescriptionEditText;

    public FileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
                            String aggregatedEntityKey) {
        super(firebaseConnectedActivity);
        this.firebaseConnectedActivity = firebaseConnectedActivity;
        this.aggregatedEntityKey = aggregatedEntityKey;
    }

    @Override
    public void show() {
        super.show();

        if (getWindow() != null) {
            int desiredWidth = firebaseConnectedActivity.getResources().getDisplayMetrics().widthPixels * 9 / 10;
            int desiredHeight = firebaseConnectedActivity.getResources().getDisplayMetrics().heightPixels / 2;
            getWindow().setLayout(desiredWidth, desiredHeight);
        }
    }

    public void setFile(int requestCode, int resultCode, Intent data) {
        CustomFileReader customFileReader = new CustomFileReader(firebaseConnectedActivity.getContentResolver());
        if (requestCode == SELECT_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            try {
                fileMetadata = fileMetadata
                        .setFileSize(customFileReader.getFileSize(fileUri))
                        .setFileName(customFileReader.getFileName(fileUri))
                        .setFileType(customFileReader.getFileType(fileUri));

                fileContent = fileContent
                        .setFileContentBase64(customFileReader.readBase64(fileUri));

                ((TextView) findViewById(R.id.fileInputDialogChosenFileName)).setText(fileMetadata.getFullFileName());
            } catch (IOException exception) {
                logAndShowException("An error occurred while reading the file", exception);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.file_input_dialog);

        findViewById(R.id.fileInputDialogChooseButton).setOnClickListener((v) -> {
            Intent intent = new Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT);

            firebaseConnectedActivity.startActivityForResult(createChooser(intent, "Select a file"),
                    SELECT_FILE_REQUEST_CODE);
        });

        findViewById(R.id.fileInputDialogPositiveButton).setOnClickListener((v) -> dismiss());

        findViewById(R.id.fileInputDialogPositiveButton).setOnClickListener((v) -> {
            fileMetadata.setFileDescription(fileDescriptionEditText.getText().toString());
            fileMetadata.setFileTitle(fileTitleEditText.getText().toString());

            if (fileMetadata.getFileTitle() == null ||
                    fileMetadata.getFileTitle().length() == 0) {
                Toast.makeText(getContext(), "Please enter a file title", Toast.LENGTH_SHORT).show();
            }
            else if (fileContent.getFileContentBase64() == null ||
                    fileContent.getFileContentBase64().length() == 0) {
                Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
            }
            else {
                saveFile(fileContent, fileMetadata);
                dismiss();
            }
        });

        init();
    }

    private void init() {
        fileMetadata = new FileMetadata();

        fileContent = new FileContent()
                .setFileMetadataKey(fileMetadata.getKey());

        fileMetadata.setFileContentKey(fileContent.getKey());

        fileTitleEditText = findViewById(R.id.fileInputDialogFileTitle);
        fileDescriptionEditText = findViewById(R.id.fileInputDialogFileDescription);
    }

    private void saveFile(FileContent fileContent, FileMetadata fileMetadata) {
        firebaseConnectedActivity.getFirebaseConnection().execute(new SaveRequest<FileContent>()
                .entity(fileContent)
                .databaseTable(FILE_CONTENT)
                .onSuccess(() -> saveFileMetadata(fileMetadata))
                .onError((error) ->
                        logAndShowException("An error occurred while uploading the file", error)
                ));
    }

    private void saveFileMetadata(FileMetadata fileMetadata) {
        firebaseConnectedActivity.getFirebaseConnection().execute(new SaveRequest<FileMetadata>()
                .entity(fileMetadata)
                .databaseTable(FILE_METADATA)
                .onSuccess(() -> addMetadataToCourse(aggregatedEntityKey, fileMetadata))
                .onError((error) -> {
                    logAndShowException("An error occurred while uploading the file", error);
                    deleteFileContent(fileMetadata.getFileContentKey());
                }));
    }

    private void deleteFileMetadata(String fileMetadataKey) {
        firebaseConnectedActivity.getFirebaseConnection().execute(new DeleteByIdRequest()
                .databaseTable(FILE_METADATA)
                .key(fileMetadataKey));
    }

    private void deleteFileContent(String fileContentKey) {
        firebaseConnectedActivity.getFirebaseConnection().execute(new DeleteByIdRequest()
                .databaseTable(FILE_CONTENT)
                .key(fileContentKey));
    }

    private void logAndShowException(String errorMessage, Exception exception) {
        firebaseConnectedActivity.showSnackBar(errorMessage, SNACK_BAR_ERROR_DURATION);
        LOGGER.error(errorMessage, exception);
    }

    private void addMetadataToCourse(String courseKey, FileMetadata fileMetadata) {
        firebaseConnectedActivity.getFirebaseConnection().execute(new GetRequest<Course>()
                .databaseTable(COURSES)
                .predicate(where(ID).equalTo(courseKey))
                .onSuccess((courses) -> {
                    Course course = courses.get(0);
                    course.getFilesMetadata().add(fileMetadata.getKey());

                    firebaseConnectedActivity.showSnackBar("Uploading ");

                    firebaseConnectedActivity.getFirebaseConnection().execute(new SaveRequest<Course>()
                            .databaseTable(COURSES)
                            .entity(course)
                            .onSuccess(() ->
                                    firebaseConnectedActivity.showSnackBar(
                                            "Successfully uploaded " + fileMetadata.getFullFileName(),
                                            1000))
                            .onError((exception) -> {
                                logAndShowException("An error occurred while linking the file to the course",
                                        exception);

                                deleteFileContent(fileMetadata.getFileContentKey());
                                deleteFileMetadata(fileMetadata.getKey());
                            }));
                })
                .subscribe(false));
    }
}
