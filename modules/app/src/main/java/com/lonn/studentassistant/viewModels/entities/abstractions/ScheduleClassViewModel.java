package com.lonn.studentassistant.viewModels.entities.abstractions;

import android.view.View;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.Utils;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;

import java.util.List;
import java.util.StringJoiner;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static com.lonn.studentassistant.Utils.hourToString;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public abstract class ScheduleClassViewModel<T extends ScheduleClass> extends EntityViewModel<T> {
    public int startHour, endHour;
    public List<String> rooms;
    public List<ProfessorViewModel> professors;
    public Discipline discipline;

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

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
        this.disciplineName = discipline.getDisciplineName();
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
