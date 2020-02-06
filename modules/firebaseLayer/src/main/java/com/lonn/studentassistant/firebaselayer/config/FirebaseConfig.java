package com.lonn.studentassistant.firebaselayer.config;

import android.content.Context;

import androidx.annotation.Nullable;

import com.lonn.studentassistant.firebaselayer.R;
import com.lonn.studentassistant.firebaselayer.Utils;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class FirebaseConfig {
	@Getter(AccessLevel.PROTECTED)
	private Context context;

	public String getTableReference(DatabaseTable table) {
		return getDatabaseName() + "/" + getTableName(table);
	}

	String getTableName(@Nullable DatabaseTable databaseTable) {
		if (databaseTable == null) {
			return "UnknownTable";
		}

		return Utils.stringToFirstCapital(databaseTable.getTableName());
	}

	String getDatabaseName() {
		if (getContext() != null && getContext().getResources() != null) {
			return getContext().getResources().getString(R.string.database_name);
		}

		return "unknown";
	}
}
