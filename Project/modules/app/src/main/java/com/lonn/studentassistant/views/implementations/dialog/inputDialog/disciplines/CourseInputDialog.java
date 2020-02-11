package com.lonn.studentassistant.views.implementations.dialog.inputDialog.disciplines;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.SingleChoiceListAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.CycleSpecializationYear;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;

import static android.widget.Toast.LENGTH_LONG;

@Accessors(chain = true)
public class CourseInputDialog extends DisciplineInputDialog<CourseViewModel> {
    @Getter
    private TextView packTextView, yearsTextView;
    private List<CycleSpecializationYear> years = new LinkedList<>();
    private Integer pack;

    public CourseInputDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        packTextView = findViewById(R.id.disciplineInputDialogPack);
        yearsTextView = findViewById(R.id.disciplineInputDialogYears);

        findViewById(R.id.disciplineInputDialogYearAddButton).setOnClickListener(view -> {
            SingleChoiceListAdapter<CycleSpecializationYear> yearAdapter = new SingleChoiceListAdapter<>(getContext(),
                    CycleSpecializationYear.values());

            new AlertDialog.Builder(getContext(), R.style.DialogTheme)
                    .setTitle("Select year to add")
                    .setAdapter(yearAdapter, (dialog, which) -> {
                        this.years.add(yearAdapter.getItem(which));
                        yearsTextView.setText(listToString(years));
                    })
                    .create()
                    .show();
        });

        findViewById(R.id.disciplineInputDialogPackSelect).setOnClickListener(view -> {
            SingleChoiceListAdapter<String> packAdapter = new SingleChoiceListAdapter<>(getContext(),
                    new String[]{"Mandatory discipline",
                            "Optional discipline, first pack",
                            "Optional discipline, second pack",
                            "Optional discipline, third pack",
                            "Optional discipline, fourth pack"});

            new AlertDialog.Builder(getContext(), R.style.DialogTheme)
                    .setTitle("Select pack")
                    .setAdapter(packAdapter, (dialog, which) -> {
                        this.pack = which;
                        packTextView.setText(packAdapter.getItem(which));
                    })
                    .create()
                    .show();
        });
    }

    @Override
    protected CourseViewModel parseEntity(CourseViewModel course) {
        if (pack == null) {
            Toast.makeText(getContext(), "Please select a pack!", LENGTH_LONG).show();
            return null;
        }

        if (years.size() == 0) {
            Toast.makeText(getContext(), "Please select at least an year!", LENGTH_LONG).show();
            return null;
        }

        return super.parseEntity(new CourseViewModel()
                .setPack(pack)
                .setCycleSpecializationYears(years));
    }

    protected int getLayoutId() {
        return R.layout.course_input_dialog;
    }

    private String listToString(Collection<?> list) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object obj : list) {
            stringBuilder.append(obj.toString());
            stringBuilder.append("\n");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }
}
