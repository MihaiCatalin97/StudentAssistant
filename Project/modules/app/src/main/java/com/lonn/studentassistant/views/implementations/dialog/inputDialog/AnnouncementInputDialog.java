package com.lonn.studentassistant.views.implementations.dialog.inputDialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AnnouncementViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.PROFESSOR;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.STUDENT;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.EXAM;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.EXAM_ARREARS;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.LABORATORY;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.PARTIAL_ARREARS;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.PARTIAL_EXAM;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.PROJECT;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.PROJECT_ARREARS;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidStudentId;
import static java.lang.Double.parseDouble;
import static lombok.AccessLevel.PRIVATE;

@Accessors(chain = true)
public class AnnouncementInputDialog extends Dialog {
    private EditText titleEditText, messageEditText;
    private CheckBox studentCheckbox, professorCheckbox;
    @Setter
    private Consumer<AnnouncementViewModel> positiveButtonAction;

    public AnnouncementInputDialog(Activity activity) {
        super(activity, R.style.DialogTheme);
    }

    @Override
    public void show() {
        int width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.90);

        if (getWindow() != null) {
            getWindow().setLayout(width, WRAP_CONTENT);
        }

        super.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.announcement_input_dialog);

        titleEditText = findViewById(R.id.announcementInputDialogTitle);
        messageEditText = findViewById(R.id.announcementInputDialogMessage);

        studentCheckbox = findViewById(R.id.announcementInputDialogStudentTarget);
        professorCheckbox = findViewById(R.id.announcementInputDialogProfessorTarget);

        findViewById(R.id.buttonNegative).setOnClickListener((view) -> this.dismiss());
        findViewById(R.id.buttonPositive).setOnClickListener((view) ->
                positiveButtonAction.consume(parse())
        );
    }

    private AnnouncementViewModel parse() {
        String title = titleEditText.getText().toString();
        String message = messageEditText.getText().toString();
        List<AccountType> targetedGroups = new LinkedList<>();

        if(studentCheckbox.isChecked()){
            targetedGroups.add(STUDENT);
        }
        if(professorCheckbox.isChecked()){
            targetedGroups.add(PROFESSOR);
        }

        return new AnnouncementViewModel()
                .setTitle(title)
                .setMessage(message)
                .setDate(new Date())
                .setTargetedGroups(targetedGroups);
    }
}
