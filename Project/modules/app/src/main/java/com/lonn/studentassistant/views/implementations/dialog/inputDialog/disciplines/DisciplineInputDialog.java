package com.lonn.studentassistant.views.implementations.dialog.inputDialog.disciplines;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.SingleChoiceListAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.DisciplineViewModel;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static android.widget.Toast.LENGTH_LONG;
import static com.lonn.studentassistant.utils.Utils.displayHeight;
import static com.lonn.studentassistant.utils.Utils.displayWidth;
import static java.util.UUID.randomUUID;

@Accessors(chain = true)
public abstract class DisciplineInputDialog<T extends DisciplineViewModel> extends Dialog {
    @Getter
    private Button positiveButton, negativeButton;
    @Setter
    private Consumer<T> positiveButtonAction;
    @Getter
    private TextView titleTextView, descriptionTextView, websiteTextView, semesterTextView;
    private Integer semester;

    public DisciplineInputDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());

        if (getWindow() != null) {
            getWindow().setLayout((int) (displayWidth * 0.9), (int) (displayHeight * 0.75));
        }

        positiveButton = findViewById(R.id.buttonPositive);
        negativeButton = findViewById(R.id.buttonNegative);

        titleTextView = findViewById(R.id.disciplineInputDialogName);
        descriptionTextView = findViewById(R.id.disciplineInputDialogDescription);
        websiteTextView = findViewById(R.id.disciplineInputDialogWebsite);
        semesterTextView = findViewById(R.id.disciplineInputDialogSemester);

        negativeButton.setOnClickListener((view) -> this.dismiss());
        positiveButton.setOnClickListener((view) -> {
            titleTextView.clearFocus();
            descriptionTextView.clearFocus();
            websiteTextView.clearFocus();

            T disciplineViewModel = parseEntity(null);

            if (disciplineViewModel != null) {
                disciplineViewModel.setKey(randomUUID().toString());
                positiveButtonAction.consume(disciplineViewModel);
                dismiss();
            }
        });

        findViewById(R.id.disciplineInputDialogSemesterSelect).setOnClickListener(view -> {
            SingleChoiceListAdapter<String> semesterAdapter = new SingleChoiceListAdapter<>(getContext(),
                    new String[]{"First semester", "Second semester"});

            new AlertDialog.Builder(getContext(), R.style.DialogTheme)
                    .setTitle("Select semester")
                    .setAdapter(semesterAdapter, (dialog, which) -> {
                        this.semester = which + 1;
                        semesterTextView.setText(semesterAdapter.getItem(which));
                    })
                    .create()
                    .show();
        });
    }

    protected T parseEntity(T discipline) {
        String title = ((TextView) findViewById(R.id.disciplineInputDialogName)).getText().toString();
        String description = ((TextView) findViewById(R.id.disciplineInputDialogDescription)).getText().toString();
        String website = ((TextView) findViewById(R.id.disciplineInputDialogWebsite)).getText().toString();

        if (title.length() == 0) {
            Toast.makeText(getContext(), "Please enter a name!", LENGTH_LONG).show();
            return null;
        }

        if (semester == null) {
            Toast.makeText(getContext(), "Please select a semester!", LENGTH_LONG).show();
            return null;
        }

        return (T) discipline
                .setDescription(description)
                .setSemester(semester)
                .setName(title)
                .setWebsite(website);
    }

    protected abstract int getLayoutId();
}
