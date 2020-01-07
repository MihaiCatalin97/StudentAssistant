package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.EntityActivity;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.viewModels.entities.CourseViewModel;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialogBuilders.FileUploadDialog;

public class CourseEntityActivity extends EntityActivity<CourseViewModel> {
    private static final Logger LOGGER = Logger.ofClass(CourseEntityActivity.class);
    CourseEntityActivityLayoutBinding binding;
    private CourseViewModel viewModel;
    private CourseEntityActivityFirebaseDispatcher dispatcher;
    private FileUploadDialog fileUploadDialog;

    protected void loadAll() {
        dispatcher.loadAll();
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.course_entity_activity_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = getEntityFromIntent(this.getIntent());
        binding.setCourse(viewModel);

        dispatcher = new CourseEntityActivityFirebaseDispatcher(this);

        ((ScrollViewCategory) findViewById(R.id.filesCategory)).setOnAddAction(() -> {
            fileUploadDialog = new FileUploadDialog(this, viewModel.getKey());
            fileUploadDialog.show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fileUploadDialog.setFile(requestCode, resultCode, data);
    }
}
