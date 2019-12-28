package com.lonn.studentassistant.viewModels.entities;

import android.view.View;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.Utils;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ScheduleClassViewModel extends EntityViewModel<ScheduleClass> {
    public int day, startHour, endHour;
    public List<String> rooms;
    public List<ProfessorViewModel> professors = new ArrayList<>();
    public Discipline discipline;

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
        String minuteString = Integer.toString(startHour % 100);

        if (minuteString.length() == 1) {
            minuteString = "0" + minuteString;
        }

        return startHour / 100 + ":" + minuteString;
    }

    @Bindable
    public String getEndHour() {
        String minuteString = Integer.toString(endHour % 100);

        if (minuteString.length() == 1) {
            minuteString = "0" + minuteString;
        }

        return endHour / 100 + ":" + minuteString;
    }

    @Bindable
    public String getDay() {
        return Utils.dayToString(day);
    }

    @Bindable
    public String getDisciplineName() {
        if (discipline == null) {
            return "";
        }
        return discipline.getDisciplineName();
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
}
