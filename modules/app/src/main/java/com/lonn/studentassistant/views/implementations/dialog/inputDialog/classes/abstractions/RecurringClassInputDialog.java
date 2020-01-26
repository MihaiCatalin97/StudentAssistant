package com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.abstractions;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
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

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.Toast.LENGTH_LONG;
import static com.lonn.studentassistant.utils.Utils.displayWidth;

public abstract class RecurringClassInputDialog<T extends DisciplineViewModel> extends ClassInputDialog<T, RecurringClassViewModel> {
	@Getter
	private TextView dayTextView;

	private WeekDay day;

	public RecurringClassInputDialog(Context context, T discipline,
									 List<ProfessorViewModel> disciplineProfessors) {
		super(context, discipline, disciplineProfessors);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dayTextView = findViewById(R.id.recurringClassDialogDay);

		findViewById(R.id.recurringClassDialogDaySelectButton).setOnClickListener(view -> {
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
				.build());
	}

	protected int getLayoutId() {
		return R.layout.recurring_class_input_dialog;
	}
}
