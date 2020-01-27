package com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.abstractions;

import android.content.Context;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.SingleChoiceListAdapter;
import com.lonn.studentassistant.firebaselayer.entities.enums.WeekDay;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.DisciplineViewModel;

import java.util.List;

import lombok.Getter;

import static android.widget.Toast.LENGTH_LONG;

public abstract class RecurringClassInputDialog<T extends DisciplineViewModel> extends ClassInputDialog<T, RecurringClassViewModel> {
	@Getter
	private TextView dayTextView;
	@Getter
	private RadioButton allWeeksRadio, evenWeeksRadio, oddWeeksRadio;

	private String parity = "";
	private WeekDay day;

	public RecurringClassInputDialog(Context context, T discipline,
									 List<ProfessorViewModel> disciplineProfessors) {
		super(context, discipline, disciplineProfessors);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dayTextView = findViewById(R.id.classDialogDay);

		allWeeksRadio = findViewById(R.id.recurringClassDialogAllWeeks);
		evenWeeksRadio = findViewById(R.id.recurringClassDialogEvenWeeks);
		oddWeeksRadio = findViewById(R.id.recurringClassDialogOddWeeks);

		allWeeksRadio.setOnCheckedChangeListener((button, checked) -> {
			if (checked) {
				parity = "";
				evenWeeksRadio.setChecked(false);
				oddWeeksRadio.setChecked(false);
			}
		});

		evenWeeksRadio.setOnCheckedChangeListener((button, checked) -> {
			if (checked) {
				parity = "Par";
				allWeeksRadio.setChecked(false);
				oddWeeksRadio.setChecked(false);
			}
		});

		oddWeeksRadio.setOnCheckedChangeListener((button, checked) -> {
			if (checked) {
				parity = "Impar";
				evenWeeksRadio.setChecked(false);
				allWeeksRadio.setChecked(false);
			}
		});

		findViewById(R.id.classDialogDaySelectButton).setOnClickListener(view -> {
			SingleChoiceListAdapter<WeekDay> dayAdapter = new SingleChoiceListAdapter<>(getContext(),
					WeekDay.values());

			new AlertDialog.Builder(getContext(), R.style.DialogTheme)
					.setTitle("Select day of the week")
					.setAdapter(dayAdapter, (dialog, which) -> {
						setDay(dayAdapter.getItem(which));
					})
					.create()
					.show();
		});
	}

	private void setDay(@Nullable WeekDay day) {
		this.day = day;

		if (day != null) {
			dayTextView.setText(day.toString());
		}
		else {
			dayTextView.setText("(none)");
		}
	}

	@Override
	protected RecurringClassViewModel parseEntity(RecurringClassViewModel recurringClass) {
		if (day == null) {
			Toast.makeText(getContext(), "Please select a week day", LENGTH_LONG).show();
			return null;
		}

		return super.parseEntity(RecurringClassViewModel.builder()
				.day(day.getDayInt())
				.parity(parity)
				.build());
	}

	protected int getLayoutId() {
		return R.layout.recurring_class_input_dialog;
	}
}
