package com.lonn.scheduleparser.parsing.abstractions;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Repository<T extends BaseEntity> {
    protected List<T> entities;

    protected Repository() {
        this.entities = new LinkedList<>();
    }

    public boolean add(T entity) {
        if (!containsKey(entity.getKey())) {
            entities.add(entity);
            return true;
        }
        return false;
    }

    public boolean addAll(List<T> entities) {
        boolean result = true;

        if (entities == null) {
            return false;
        }

        for (T entity : entities) {
            result &= add(entity);
        }

        return result;
    }

    public boolean update(T entity) {
        int entityPosition = getPositionOfElementWithKey(entity.getKey());
        if (entityPosition != -1) {
            this.entities.set(entityPosition, entity);
            return true;
        }
        return false;
    }

    public T findByKey(String key) {
        int entityPosition = getPositionOfElementWithKey(key);

        if (entityPosition != -1) {
            return entities.get(entityPosition);
        }
        return null;
    }

    public boolean containsKey(String key) {
        return getPositionOfElementWithKey(key) != -1;
    }

    public List<T> getAll() {
        return new ArrayList<>(entities);
    }

    public abstract T findByScheduleLink(String scheduleLink);

    public void clearAll() {
        entities.clear();
    }

    private int getPositionOfElementWithKey(String key) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }
}
