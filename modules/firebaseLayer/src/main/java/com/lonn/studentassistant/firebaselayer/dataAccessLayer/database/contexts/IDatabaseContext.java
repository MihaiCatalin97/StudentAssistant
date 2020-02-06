package com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.contexts;


import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.Predicate;

import java.util.List;

public interface IDatabaseContext<T extends BaseEntity> {
	void saveOrUpdate(T entity, Consumer<Void> onSuccess, Consumer<Exception> onError);

	void delete(String key, Consumer<Void> onSuccess, Consumer<Exception> onError);

	void get(Consumer<List<T>> onSuccess, Consumer<Exception> onError, Boolean subscribe);

	void get(Consumer<List<T>> onSuccess, Consumer<Exception> onError, Predicate<? super T, ?> predicate, Boolean subscribe);

	void deleteAll(Consumer<Void> onSuccess, Consumer<Exception> onError);
}
