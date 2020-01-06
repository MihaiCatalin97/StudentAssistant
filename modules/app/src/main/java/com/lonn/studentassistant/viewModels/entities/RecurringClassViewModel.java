package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.utils.Utils;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.viewModels.entities.abstractions.ScheduleClassViewModel;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RecurringClassViewModel extends ScheduleClassViewModel<RecurringClass> {
    public int day;

    @Bindable
    public String getDay() {
        return Utils.dayToString(day);
    }
}
