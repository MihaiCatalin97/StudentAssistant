package com.lonn.studentassistant.firebaselayer.viewModels;

import com.lonn.studentassistant.firebaselayer.entities.Grade;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class GradeViewModel extends EntityViewModel<Grade> {
    private String studentId;
    private String studentKey;
    private int grade;
    private String laboratoryKey;

    @Override
    public GradeViewModel setKey(String key) {
        super.setKey(key);
        return this;
    }
}
