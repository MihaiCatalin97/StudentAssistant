package com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.abstractions;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.DisciplineViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;
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
import static com.lonn.studentassistant.utils.Utils.displayWidth;
import static java.util.Collections.sort;
import static java.util.UUID.randomUUID;

public abstract class ClassInputDialog<T extends DisciplineViewModel, V extends ScheduleClassViewModel> extends Dialog {
	@Getter
	private Button positiveButton, negativeButton;
	@Getter
	private TextView professorsTextView, roomsTextView, groupsTextView;
	@Getter
	private EditText startHourEditText, endHourEditText, classTypeEditText;
	@Getter
	private RadioButton allWeeksRadio, evenWeeksRadio, oddWeeksRadio;
	@Setter
	private Consumer<V> positiveButtonAction;

	@Getter
	private T discipline;
	private String parity = "";
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

		professorsTextView = findViewById(R.id.recurringClassDialogProfessors);
		roomsTextView = findViewById(R.id.recurringClassDialogRooms);
		groupsTextView = findViewById(R.id.recurringClassDialogGroups);
		startHourEditText = findViewById(R.id.recurringClassDialogStartHourInput);
		endHourEditText = findViewById(R.id.recurringClassDialogEndHourInput);
		classTypeEditText = findViewById(R.id.recurringClassDialogClassTypeInput);

		allWeeksRadio = findViewById(R.id.recurringClassDialogAllWeeks);
		evenWeeksRadio = findViewById(R.id.recurringClassDialogEvenWeeks);
		oddWeeksRadio = findViewById(R.id.recurringClassDialogOddWeeks);

		View.OnFocusChangeListener hourFocusListener = (view, hasFocus) -> {
			if (!hasFocus) {
				String hourText = ((EditText) view).getText().toString();

				if (hourText.length() > 0) {
					String[] columnSplit = hourText.split(":");

					if (columnSplit.length > 0 && columnSplit.length < 3) {
						String hourString = columnSplit[0];
						String minuteString = null;

						if (columnSplit.length == 2) {
							minuteString = columnSplit[1];
						}

						if (hourString.length() == 0) {
							hourString = "08";
						}
						else if (hourString.length() == 1) {
							hourString = "0" + hourString;
						}

						if (minuteString == null || minuteString.length() == 0) {
							minuteString = "00";
						}
						else if (minuteString.length() == 1) {
							minuteString = minuteString + "0";
						}

						((EditText) view).setText(hourString + ":" + minuteString);
					}
				}
			}
		};

		startHourEditText.setOnFocusChangeListener(hourFocusListener);
		endHourEditText.setOnFocusChangeListener(hourFocusListener);

		negativeButton.setOnClickListener((view) -> this.dismiss());
		positiveButton.setOnClickListener((view) -> {
			startHourEditText.clearFocus();
			endHourEditText.clearFocus();
			classTypeEditText.clearFocus();

			V classViewModel = parseEntity(null);

			if (classViewModel != null) {
				positiveButtonAction.consume(classViewModel);
				dismiss();
			}
		});

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


		findViewById(R.id.recurringClassDialogGroupAddButton).setOnClickListener(view -> showGroupSelectionDialog());

		findViewById(R.id.recurringClassDialogRoomAddButton).setOnClickListener(view -> {
			new StringInputDialog(getContext())
					.setTitle("Room input")
					.setInputHint("Enter room")
					.setInputTitle("Room:")
					.setPositiveButtonAction(this::addRoom)
					.setPositiveButtonText("Add")
					.show();
		});

		findViewById(R.id.recurringClassDialogProfessorsAddButton).setOnClickListener(view -> {
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

		findViewById(R.id.recurringClassParentLayout).setOnClickListener(view -> {
			startHourEditText.clearFocus();
			endHourEditText.clearFocus();
			classTypeEditText.clearFocus();
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
		int startHour;
		int endHour;
		String classType = classTypeEditText.getText().toString();

		try {
			startHour = Integer.parseInt(startHourEditText.getText().toString().replace(":", ""));

			if (startHour < 100 || startHour > 2359) {
				throw new Exception();
			}
		}
		catch (Exception e) {
			Toast.makeText(getContext(), "Invalid start hour!", LENGTH_LONG).show();
			return null;
		}

		try {
			endHour = Integer.parseInt(endHourEditText.getText().toString().replace(":", ""));

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
				.setParity(parity)
				.setType(classType)
				.setProfessors(selectedProfessorKeys)
				.setKey(randomUUID().toString());
	}

	protected abstract int getLayoutId();

	protected abstract void showGroupSelectionDialog();
}
