package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.ProfessorAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;

public class ProfessorService extends Service<Professor, Exception, ProfessorViewModel> {
	private static ProfessorService instance;

	public static ProfessorService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new ProfessorService(firebaseConnection);
		}

		return instance;
	}

	private ProfessorService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new ProfessorAdapter();
	}

	@Override
	protected DatabaseTable<Professor> getDatabaseTable() {
		return PROFESSORS;
	}
}
