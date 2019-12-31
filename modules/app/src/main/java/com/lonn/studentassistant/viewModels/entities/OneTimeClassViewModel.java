package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.viewModels.entities.abstractions.ScheduleClassViewModel;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OneTimeClassViewModel extends ScheduleClassViewModel<OneTimeClass> {
    @Bindable
    public Date date;
}
