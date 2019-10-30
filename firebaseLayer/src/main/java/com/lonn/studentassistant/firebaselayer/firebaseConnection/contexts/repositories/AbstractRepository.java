//package com.lonn.studentassistant.firebaselayer.firebaseConnection.contexts.repositories;
//
//import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//public abstract class AbstractRepository<T extends BaseEntity> implements Repository<T> {
//    private Map<UUID, T> entityMap = new HashMap<>();
//
//    public T save(T entity) {
//        return entityMap.put(entity.getId(), entity);
//    }
//
//    public T getById(UUID uuid) {
//        return entityMap.get(uuid);
//    }
//
//    public T delete(UUID uuid) {
//        return entityMap.remove(uuid);
//    }
//
//    public T update(T entity) {
//        return entityMap.put(entity.getId(), entity);
//    }
//
//    public Collection<T> getAll() {
//        return entityMap.values();
//    }
//
//    public List<T> filter(Map<String, String> predicatesMap) {
//        List<T> result = new LinkedList<>();
//
//        for (T entity : getAll()) {
//            if (checkEntity(entity, predicatesMap)) {
//                result.add(entity);
//            }
//        }
//
//        return result;
//    }
//
//    private boolean checkEntity(T entity, Map<String, String> predicatesMap) {
//        return true;
//    }
//}
