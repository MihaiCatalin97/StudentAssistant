package com.lonn.scheduleparser.mergers;

import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

import java.util.LinkedList;
import java.util.List;

public abstract class Merger<T extends BaseEntity> {
    public List<T> merge(List<T> listToMerge) {
        List<T> result = new LinkedList<>();

        for (T entityToMerge : listToMerge) {
            addByMerging(result, entityToMerge);
        }

        return result;
    }

    public void addByMerging(List<T> existingElements, T newElement) {
        boolean merged = false;

        for (T existingElement : existingElements) {
            if (mergingCondition(existingElement, newElement)) {
                mergingFunction(existingElement, newElement);
                merged = true;
                break;
            }
        }

        if (!merged) {
            existingElements.add(newElement);
        }
    }

    protected abstract boolean mergingCondition(T entity1, T entity2);

    protected abstract void mergingFunction(T entity1, T entity2);
}
