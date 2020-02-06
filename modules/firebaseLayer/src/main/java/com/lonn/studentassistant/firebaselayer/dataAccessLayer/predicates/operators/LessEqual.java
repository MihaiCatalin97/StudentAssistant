package com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.operators;

import com.lonn.studentassistant.firebaselayer.Utils;

import java.math.BigInteger;

public class LessEqual<V> extends Operator<V> {
	public OperatorFilter apply(V value) {
		if (value instanceof BigInteger) {
			return (dbQuery) -> dbQuery.endAt(Utils.padWithZeroesToSize(value.toString()));
		}
		if (value instanceof Number) {
			return (dbQuery) -> dbQuery.endAt(((Number) value).doubleValue());
		}
		if (value instanceof Boolean) {
			return (dbQuery) -> dbQuery.endAt((Boolean) value);
		}

		return (dbQuery) -> dbQuery.endAt(value.toString());
	}
}