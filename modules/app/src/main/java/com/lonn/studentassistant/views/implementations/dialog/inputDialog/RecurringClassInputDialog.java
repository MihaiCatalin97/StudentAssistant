package com.lonn.studentassistant.views.implementations.dialog.inputDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.SingleChoiceListAdapter;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.entities.enums.WeekDay;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear.forCycleSpecialization;
import static com.lonn.studentassistant.utils.Utils.displayWidth;
import static java.util.Collections.sort;

public class RecurringClassInputDialog extends Dialog {
	@Getter
	private Button positiveButton, negativeButton;
	@Getter
	private TextView professorsTextView, roomsTextView, groupsTextView, dayTextView;
	@Getter
	private EditText startHourEditText, endHourEditText, classTypeEditText;
	@Getter
	private RadioButton allWeeksRadio, evenWeeksRadio, oddWeeksRadio;
	@Setter
	private Runnable positiveButtonAction;

	private String parity;
	private WeekDay day;
	private List<ProfessorViewModel> professors;
	private List<String> rooms;
	private List<String> groups;

	public RecurringClassInputDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recurring_class_input_dialog);

		if (getWindow() != null) {
			getWindow().setLayout((int) (displayWidth * 0.9), WRAP_CONTENT);
		}

		groups = new LinkedList<>();
		rooms = new LinkedList<>();
		professors = new LinkedList<>();

		positiveButton = findViewById(R.id.buttonPositive);
		negativeButton = findViewById(R.id.buttonNegative);

		professorsTextView = findViewById(R.id.recurringClassDialogProfessors);
		roomsTextView = findViewById(R.id.recurringClassDialogRooms);
		groupsTextView = findViewById(R.id.recurringClassDialogGroups);
		dayTextView = findViewById(R.id.recurringClassDialogDay);
		startHourEditText = findViewById(R.id.recurringClassDialogStartHourInput);
		endHourEditText = findViewById(R.id.recurringClassDialogEndHourInput);
		classTypeEditText = findViewById(R.id.recurringClassDialogClassTypeInput);

		allWeeksRadio = findViewById(R.id.recurringClassDialogAllWeeks);
		evenWeeksRadio = findViewById(R.id.recurringClassDialogEvenWeeks);
		oddWeeksRadio = findViewById(R.id.recurringClassDialogOddWeeks);

		negativeButton.setOnClickListener((view) -> this.dismiss());
		positiveButton.setOnClickListener((view) -> positiveButtonAction.run());

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

		findViewById(R.id.recurringClassDialogGroupAddButton).setOnClickListener(view -> {
			SingleChoiceListAdapter<CycleSpecialization> specializationAdapter = new SingleChoiceListAdapter<>(getContext(), CycleSpecialization.values());

			new AlertDialog.Builder(getContext(), R.style.DialogTheme)
					.setTitle("Select study cycle")
					.setAdapter(specializationAdapter, (dialog, which) -> {
						SingleChoiceListAdapter<CycleSpecializationYear> yearAdapter = new SingleChoiceListAdapter<>(getContext(),
								forCycleSpecialization(specializationAdapter.getItem(which)));

						new AlertDialog.Builder(getContext(), R.style.DialogTheme)
								.setTitle("Select year")
								.setAdapter(yearAdapter, (dialog1, which1) -> {
									SingleChoiceListAdapter<String> groupAdapter = new SingleChoiceListAdapter<>(getContext(),
											yearAdapter.getItem(which1).getGroups());

									new AlertDialog.Builder(getContext(), R.style.DialogTheme)
											.setTitle("Select group")
											.setAdapter(groupAdapter, (dialog2, which2) -> {
												addGroup(groupAdapter.getItem(which2));
											})
											.create()
											.show();
								})
								.create()
								.show();
					})
					.create()
					.show();
		});

		findViewById(R.id.recurringClassDialogRoomAddButton).setOnClickListener(view -> {
			new StringInputDialog(getContext())
					.setTitle("Room input")
					.setInputHint("Enter room")
					.setInputTitle("Room:")
					.setPositiveButtonAction(this::addRoom)
					.setPositiveButtonText("Add")
					.show();
		});

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

	private void addRoom(String room) {
		room = room.toUpperCase();

		if (!rooms.contains(room)) {
			rooms.add(room);
			sort(rooms, String::compareTo);

			roomsTextView.setText(listToString(rooms));
		}
	}

	private void addGroup(String group) {
		if (!groups.contains(group)) {
			groups.add(group);
			sort(groups, String::compareTo);

			groupsTextView.setText(listToString(groups));
		}
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
