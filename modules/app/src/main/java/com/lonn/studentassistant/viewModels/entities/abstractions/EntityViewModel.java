package com.lonn.studentassistant.viewModels.entities.abstractions;

import androidx.databinding.BaseObservable;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public abstract class EntityViewModel<T extends BaseEntity> extends BaseObservable implements Serializable {
    private String key;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EntityViewModel)) {
            return false;
        }
        return this.key.equals(((EntityViewModel) obj).getKey());
    }
}
