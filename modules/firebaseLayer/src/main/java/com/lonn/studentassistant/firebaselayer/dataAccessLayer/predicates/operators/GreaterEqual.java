package com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.operators;

import com.lonn.studentassistant.firebaselayer.Utils;

import java.math.BigInteger;

public class GreaterEqual<V> extends Operator<V> {
	public OperatorFilter apply(V value) {
		if (value instanceof BigInteger) {
			return (dbQuery) -> dbQuery.startAt(Utils.padWithZeroesToSize(value.toString()));
		}
		if (value instanceof Number) {
			return (dbQuery) -> dbQuery.startAt(((Number) value).doubleValue());
		}
		if (value instanceof Boolean) {
			return (dbQuery) -> dbQuery.startAt((Boolean) value);
		}

		return (dbQuery) -> dbQuery.startAt(value.toString());
	}
}