package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OtherActivityViewModel extends DisciplineViewModel<OtherActivity> {
    @Bindable
    public String type;
}
