package com.lonn.studentassistant.firebaselayer.viewModels.abstractions;

import android.view.View;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static com.lonn.studentassistant.firebaselayer.Utils.hourToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public abstract class ScheduleClassViewModel<T extends ScheduleClass> extends EntityViewModel<T> {
	public int startHour, endHour;
	public List<String> rooms;
	public List<String> professors;
	public String discipline;

	@Bindable
	public String disciplineName = "";
	@Bindable
	public String type, parity;
	@Bindable
	public List<String> groups;

	@Bindable
	public int getParityVisible() {
		return (parity != null && parity.length() > 0) ? View.VISIBLE : View.GONE;
	}

	@Bindable
	public String getStartHour() {
		return hourToString(startHour);
	}

	@Bindable
	public String getEndHour() {
		return hourToString(endHour);
	}

	@Bindable
	public String getHours() {
		return getStartHour() + "\n" + getEndHour();
	}

	@Bindable
	public String getRooms() {
		String result = "";

		for (int i = 0; i < rooms.size(); i++) {
			result += rooms.get(i);

			if (i + 1 < rooms.size()) {
				result += "\n";
			}
		}

		return result;
	}

	@Bindable
	public String getFormattedType() {
		return type.replace(" ", "\n");
	}

	@Bindable
	public int getTypeNumberOfLines() {
		return type.split(" ").length;
	}

	@Bindable
	public int getNumberOfRooms() {
		return rooms.size();
	}

	@Override
	public ScheduleClassViewModel<T> setKey(String key) {
		super.setKey(key);
		return this;
	}
}
