package com.lonn.studentassistant.firebaselayer.database.contexts;

import com.google.android.gms.tasks.Task;
import com.lonn.studentassistant.firebaselayer.config.FirebaseConfig;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer;
import com.lonn.studentassistant.firebaselayer.entities.IdentificationHash;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.HashableEntity;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;
import com.lonn.studentassistant.firebaselayer.predicates.fields.IdentificationHashField;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseContextWithIdentification<T extends HashableEntity> extends DatabaseContext<T> {
	private DatabaseContext<IdentificationHash> entityIdentificationDatabaseContext;

	public DatabaseContextWithIdentification(FirebaseConfig config,
											 String databaseTableReference,
											 Class<T> modelClass) {
		super(databaseTableReference, modelClass);
		entityIdentificationDatabaseContext =
				new DatabaseContext<>(
						config.getTableReference(DatabaseTableContainer.IDENTIFICATION_HASHES),
						IdentificationHash.class);
	}

	@Override
	public void saveOrUpdate(T entity,
							 Consumer<Void> onSuccess,
							 Consumer<Exception> onError) {
		if (getDatabase() != null) {
			Task<Void> dbTask = getDatabase().child(entity
					.getKey())
					.setValue(entity);

			addListenersToTask(dbTask, onSuccess, onError);
		}

		IdentificationHash identificationHash = IdentificationHash.of(entity);
		entityIdentificationDatabaseContext.saveOrUpdate(identificationHash,
				(none) -> log.info("Successfully updated identification " +
						"for entity with key" + entity.getKey()),
				(error) -> log.error("Error updating identification for " +
						"entity with key " + entity.getKey() + ": " + error.getMessage()));
	}

	@Override
	public void delete(String key,
					   Consumer<Void> onSuccess,
					   Consumer<Exception> onError) {
		if (getDatabase() != null) {
			Task<Void> dbTask = getDatabase().child(key).removeValue();

			addListenersToTask(dbTask, onSuccess, onError);
		}

		entityIdentificationDatabaseContext.get((identifications) -> {
					if (identifications.size() > 0) {
						for (IdentificationHash identification : identifications) {
							entityIdentificationDatabaseContext.delete(identification.getKey(),
									(none) -> log.info("Successfully deleted identification " +
											"for entity with key" + key),
									(error) -> log.error("Error deleting identification for " +
											"entity with key " + key + ": " + error.getMessage()));

						}
					}
					else {
						log.error("Could not find identification for entity with key " + key);
					}
				},
				(error) -> {
					log.error("Error deleting identification for " +
							"entity with key " + key + ": " + error.getMessage());
				},
				Predicate.where(IdentificationHashField.ENTITY_KEY)
						.equalTo(key),
				false);
	}
}
