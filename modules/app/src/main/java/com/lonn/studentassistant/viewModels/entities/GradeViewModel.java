package com.lonn.studentassistant.viewModels.entities;

import com.lonn.studentassistant.firebaselayer.entities.Grade;
import com.lonn.studentassistant.viewModels.entities.abstractions.EntityViewModel;

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
