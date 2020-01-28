package com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.abstractions;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.SingleChoiceListAdapter;
import com.lonn.studentassistant.firebaselayer.entities.enums.ScheduleClassType;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.DisciplineViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;
import com.lonn.studentassistant.utils.Utils;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.StringInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.selectionDialog.ProfessorSelectionDialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.Toast.LENGTH_LONG;
import static com.lonn.studentassistant.firebaselayer.Utils.padWithZeroesToSize;
import static com.lonn.studentassistant.utils.Utils.displayWidth;
import static com.lonn.studentassistant.utils.Utils.hourToString;
import static java.lang.Integer.parseInt;
import static java.util.Collections.sort;
import static java.util.UUID.randomUUID;

public abstract class ClassInputDialog<T extends DisciplineViewModel, V extends ScheduleClassViewModel> extends Dialog {
	@Getter
	private Button positiveButton, negativeButton;
	@Getter
	private TextView professorsTextView, roomsTextView, groupsTextView, startHourText, endHourText, classTypeTextView;
	@Setter
	private Consumer<V> positiveButtonAction;

	@Getter
	private T discipline;
	private int startHour = 0;
	private int endHour = 0;
	private ScheduleClassType type;
	private List<ProfessorViewModel> professors;
	private List<String> rooms;
	private List<String> groups;
	private List<ProfessorViewModel> disciplineProfessors;

	public ClassInputDialog(Context context, T discipline,
							List<ProfessorViewModel> disciplineProfessors) {
		super(context);
		this.discipline = discipline;
		this.disciplineProfessors = disciplineProfessors;
	}

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutId());

		if (getWindow() != null) {
			getWindow().setLayout((int) (displayWidth * 0.9), WRAP_CONTENT);
		}

		groups = new LinkedList<>();
		rooms = new LinkedList<>();
		professors = new LinkedList<>();

		positiveButton = findViewById(R.id.buttonPositive);
		negativeButton = findViewById(R.id.buttonNegative);

		professorsTextView = findViewById(R.id.classDialogProfessors);
		roomsTextView = findViewById(R.id.classDialogRooms);
		groupsTextView = findViewById(R.id.classDialogGroups);
		startHourText = findViewById(R.id.classDialogStartHour);
		endHourText = findViewById(R.id.classDialogEndHourInput);
		classTypeTextView = findViewById(R.id.classDialogClassTypeInput);

		startHourText.setEnabled(false);
		endHourText.setEnabled(false);
		startHourText.setClickable(true);
		endHourText.setClickable(true);

		negativeButton.setOnClickListener((view) -> this.dismiss());
		positiveButton.setOnClickListener((view) -> {
			startHourText.clearFocus();
			endHourText.clearFocus();
			classTypeTextView.clearFocus();

			V classViewModel = parseEntity(null);

			if (classViewModel != null) {
				positiveButtonAction.consume(classViewModel);
				dismiss();
			}
		});

		findViewById(R.id.classDialogStartHourSelectButton).setOnClickListener(view -> {
			new TimePickerDialog(getContext(),
					R.style.DatePickerTheme,
					(dialog, hour, minute) -> {
						startHour = hour * 100 + minute;

						startHourText.setText(hourToString(startHour));
					},
					startHour / 100,
					startHour % 100,
					true)
					.show();
		});

		findViewById(R.id.classDialogEndHourSelectButton).setOnClickListener(view -> {
			new TimePickerDialog(getContext(),
					R.style.DatePickerTheme,
					(dialog, hour, minute) -> {
						endHour = hour * 100 + minute;

						endHourText.setText(hourToString(endHour));
					},
					endHour / 100,
					endHour % 100,
					true)
					.show();
		});

		findViewById(R.id.classDialogTypeSelectButton).setOnClickListener(view -> {
			showClassTypeSelectionDialog();
		});

		findViewById(R.id.classDialogGroupAddButton).setOnClickListener(view -> showGroupSelectionDialog());

		findViewById(R.id.classDialogRoomAddButton).setOnClickListener(view -> {
			new StringInputDialog(getContext())
					.setTitle("Room input")
					.setInputHint("Enter room")
					.setInputTitle("Room:")
					.setPositiveButtonAction(this::addRoom)
					.setPositiveButtonText("Add")
					.show();
		});

		findViewById(R.id.classDialogProfessorsAddButton).setOnClickListener(view -> {
			ProfessorSelectionDialog dialog = new ProfessorSelectionDialog(getContext());
			List<ProfessorViewModel> selectableProfessors = new ArrayList<>(disciplineProfessors);
			selectableProfessors.removeAll(professors);

			dialog.setTitle("Select professors")
					.setInputHint("Enter professor name")
					.setItems(selectableProfessors)
					.setPositiveButtonAction(selectedProfessors -> {
						for (ProfessorViewModel professor : selectedProfessors) {
							addProfessor(professor);
						}
					})
					.show();
		});

		findViewById(R.id.classDialogParentLayout).setOnClickListener(view -> {
			startHourText.clearFocus();
			endHourText.clearFocus();
			classTypeTextView.clearFocus();
		});
	}

	private void addRoom(String room) {
		room = room.toUpperCase();

		if (!rooms.contains(room)) {
			rooms.add(room);
			sort(rooms, String::compareTo);

			roomsTextView.setText(listToString(rooms));
		}
	}

	protected void addGroup(String group) {
		if (!groups.contains(group)) {
			groups.add(group);
			sort(groups, String::compareTo);

			groupsTextView.setText(listToString(groups));
		}
	}

	private void addProfessor(ProfessorViewModel professor) {
		if (!professors.contains(professor)) {
			professors.add(professor);

			professorsTextView.setText(listToString(professors));
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

	protected V parseEntity(V entity) {
		Integer startHour;
		int endHour;
		String classType = classTypeTextView.getText().toString();

		startHour = getStartHour();

		if (startHour == null) {
			return null;
		}

		try {
			endHour = parseInt(endHourText.getText().toString().replace(":", ""));

			if (endHour < 100 || endHour > 2359) {
				throw new Exception();
			}
		}
		catch (Exception e) {
			Toast.makeText(getContext(), "Invalid end hour!", LENGTH_LONG).show();
			return null;
		}

		if (endHour <= startHour) {
			Toast.makeText(getContext(), "Start hour must be greater than end hour!", LENGTH_LONG).show();
			return null;
		}

		if (classType.length() == 0) {
			Toast.makeText(getContext(), "Please enter a class type", LENGTH_LONG).show();
			return null;
		}

		if (rooms.size() == 0) {
			Toast.makeText(getContext(), "At least one room should be selected", LENGTH_LONG).show();
			return null;
		}

		List<String> selectedProfessorKeys = new ArrayList<>();

		for (ProfessorViewModel professor : professors) {
			selectedProfessorKeys.add(professor.getKey());
		}

		return (V) entity.setDiscipline(discipline.getKey())
				.setDisciplineName(discipline.getName())
				.setStartHour(startHour)
				.setEndHour(endHour)
				.setGroups(groups)
				.setRooms(rooms)
				.setType(type)
				.setProfessors(selectedProfessorKeys)
				.setKey(randomUUID().toString());
	}

	protected Integer getStartHour() {
		try {
			Integer startHour = parseInt(startHourText.getText().toString().replace(":", ""));

			if (startHour < 100 || startHour > 2359) {
				throw new Exception();
			}

			return startHour;
		}
		catch (Exception e) {
			Toast.makeText(getContext(), "Invalid start hour!", LENGTH_LONG).show();
			return null;
		}
	}

	protected Integer getEndHour() {
		try {
			Integer endHour = parseInt(endHourText.getText().toString().replace(":", ""));

			if (endHour < 100 || endHour > 2359) {
				throw new Exception();
			}

			return endHour;
		}
		catch (Exception e) {
			Toast.makeText(getContext(), "Invalid start hour!", LENGTH_LONG).show();
			return null;
		}
	}

	private void showClassTypeSelectionDialog() {
		SingleChoiceListAdapter<ScheduleClassType> scheduleTypeAdapter = new SingleChoiceListAdapter<>(getContext(),
				ScheduleClassType.values());

		new AlertDialog.Builder(getContext(), R.style.DialogTheme)
				.setTitle("Select class type")
				.setAdapter(scheduleTypeAdapter, (dialog, which) -> this.setClassType(scheduleTypeAdapter.getItem(which)))
				.create()
				.show();
	}

	private void setClassType(@Nullable ScheduleClassType type) {
		this.type = type;

		if (type == null) {
			classTypeTextView.setText("(none)");
		}
		else {
			classTypeTextView.setText(type.toString());
		}
	}

	protected abstract int getLayoutId();

	protected abstract void showGroupSelectionDialog();
}
