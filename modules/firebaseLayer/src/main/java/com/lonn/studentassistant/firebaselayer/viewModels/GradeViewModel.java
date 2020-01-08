package com.lonn.studentassistant.firebaselayer.viewModels;

import com.lonn.studentassistant.firebaselayer.entities.Grade;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GradeViewModel extends EntityViewModel<Grade> {
}
