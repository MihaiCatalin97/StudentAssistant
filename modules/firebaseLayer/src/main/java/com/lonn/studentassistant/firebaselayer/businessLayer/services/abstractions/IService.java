package com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions;

import com.lonn.studentassistant.firebaselayer.businessLayer.api.Future;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.tasks.FirebaseTask;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;

import java.util.List;

public interface IService<T extends BaseEntity, V extends Exception, U extends EntityViewModel<T>> {
    FirebaseTask<List<U>, V> getAll();

    Future<U, Exception> getById(String id, boolean subscribe);

    Future<Void, Exception> save(U entityViewModel);

    FirebaseTask<Void, V> save(T entity);

    FirebaseTask<Void, Exception> deleteAll();

    Future<Void, Exception> deleteById(String id);

    Future<List<U>, Exception> getByIds(List<String> entityIds, boolean subscribe);
}
