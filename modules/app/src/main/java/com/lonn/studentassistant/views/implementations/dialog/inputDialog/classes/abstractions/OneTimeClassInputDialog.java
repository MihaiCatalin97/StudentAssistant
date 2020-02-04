package com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.abstractions;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.DisciplineViewModel;
import com.lonn.studentassistant.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.Getter;

import static android.widget.Toast.LENGTH_LONG;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public abstract class OneTimeClassInputDialog<T extends DisciplineViewModel> extends ClassInputDialog<T, OneTimeClassViewModel> {
	@Getter
	private TextView dayTextView;

	private Date date;

	public OneTimeClassInputDialog(Context context, T discipline,
								   List<ProfessorViewModel> disciplineProfessors) {
		super(context, discipline, disciplineProfessors);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Calendar currentDate = getInstance();

		dayTextView = findViewById(R.id.classDialogDay);

		findViewById(R.id.classDialogDaySelectButton).setOnClickListener(view -> {
			new DatePickerDialog(getContext(),
					R.style.DatePickerTheme,
					(datePicker, year, month, dayOfMonth) -> {
						Calendar selectedDate = getInstance();
						selectedDate.set(year, month, dayOfMonth);
						setDate(selectedDate.getTime());
					},
					currentDate.get(YEAR),
					currentDate.get(MONTH),
					currentDate.get(DAY_OF_MONTH))
					.show();
		});
	}

	private void setDate(@Nullable Date date) {
		this.date = date;

		if (date != null) {
			dayTextView.setText(Utils.dateToStringDate(date));
		}
		else {
			dayTextView.setText("(none)");
		}
	}

	@Override
	protected OneTimeClassViewModel parseEntity(OneTimeClassViewModel oneTimeClass) {
		if (date == null) {
			Toast.makeText(getContext(), "Please select a date", LENGTH_LONG).show();
			return null;
		}

		try {
			Calendar classDateTime = getInstance();
			classDateTime.setTime(date);
			classDateTime.set(HOUR_OF_DAY, getStartHour() / 100);
			classDateTime.set(MINUTE, getStartHour() % 100);

			if (classDateTime.getTimeInMillis() < getInstance().getTimeInMillis()) {
				Toast.makeText(getContext(), "You can't create a schedule class in the past", LENGTH_LONG).show();
				return null;
			}
		}
		catch (Exception exception) {
			Toast.makeText(getContext(), "Invalid start hour", LENGTH_LONG).show();
		}

		return super.parseEntity(OneTimeClassViewModel.builder()
				.date(date)
				.build());
	}

	protected int getLayoutId() {
		return R.layout.one_time_class_input_dialog;
	}
}
