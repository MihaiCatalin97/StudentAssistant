package com.lonn.studentassistant.views.implementations.dialog.inputDialog.disciplines;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OtherActivityViewModel;

import lombok.experimental.Accessors;

import static android.widget.Toast.LENGTH_LONG;

@Accessors(chain = true)
public class ActivityInputDialog extends DisciplineInputDialog<OtherActivityViewModel> {

    public ActivityInputDialog(Context context) {
        super(context);
    }

    @Override
    protected OtherActivityViewModel parseEntity(OtherActivityViewModel activity) {
        String type = ((TextView) findViewById(R.id.disciplineInputDialogType)).getText().toString();

        if (type.length() == 0) {
            Toast.makeText(getContext(), "Please enter a type!", LENGTH_LONG).show();
            return null;
        }

        return super.parseEntity(new OtherActivityViewModel()
                .setType(type));
    }

    protected int getLayoutId() {
        return R.layout.activity_input_dialog;
    }
}
