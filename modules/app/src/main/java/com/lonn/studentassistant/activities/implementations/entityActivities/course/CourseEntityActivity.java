package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FileManagingActivity;
import com.lonn.studentassistant.activities.implementations.entityActivities.laboratory.LaboratoryInputActivity;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.FileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations.CourseFileUploadDialog;

public class CourseEntityActivity extends FileManagingActivity<CourseViewModel> {
    private static final Logger LOGGER = Logger.ofClass(CourseEntityActivity.class);
    CourseEntityActivityLayoutBinding binding;
    private CourseEntityActivityFirebaseDispatcher dispatcher;

    protected void loadAll(String entityKey) {
        dispatcher.loadAll(entityKey);
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.course_entity_activity_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dispatcher = new CourseEntityActivityFirebaseDispatcher(this);
        ((ScrollViewCategory) findViewById(R.id.laboratoriesCategory)).setOnAddAction(() -> {
            Intent laboratoryInputActivityIntent = new Intent(this,
                    LaboratoryInputActivity.class);

            laboratoryInputActivityIntent.putExtra("courseName", binding.getCourse().getName());
            laboratoryInputActivityIntent.putExtra("courseKey", binding.getCourse().getKey());

            startActivity(laboratoryInputActivityIntent);
        });

        loadAll(entityKey);
    }

    protected void deleteFile(String courseKey, String fileMetadataKey) {
        getFirebaseApi().getCourseService().deleteAndUnlinkFile(courseKey, fileMetadataKey);
    }

    protected FileUploadDialog getFileUploadDialogInstance() {
        return new CourseFileUploadDialog(this, entityKey);
    }

    protected void onEditTapped(){
        boolean editing = binding.getEditing() == null? false: binding.getEditing();

        binding.setEditing(!editing);
    }

    protected void onDeleteTapped(){

    }
}
