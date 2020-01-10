package com.lonn.studentassistant.firebaselayer.entities;

import com.google.firebase.database.Exclude;
import com.lonn.studentassistant.firebaselayer.Utils;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.HashableEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IdentificationHash extends BaseEntity {
	@Exclude
	private String hash;
	private String entityKey;

	public static <T extends HashableEntity> IdentificationHash of(T hashableEntity) {
		return generateHash(hashableEntity.computeHashingString(),
				hashableEntity.getKey());
	}

	private static IdentificationHash generateHash(String stringToHash, String entityKey) {
		IdentificationHash identificationHash = new IdentificationHash();
		identificationHash.setHash(Utils.generateHashDigest(stringToHash));
		identificationHash.setEntityKey(entityKey);

		return identificationHash;
	}

	@Override
	@Exclude
	public String getKey() {
		return hash;
	}

	@Override
	@Exclude
	public IdentificationHash setKey(String keyString) {
		this.hash = keyString;
		return this;
	}

	@Exclude
	public String getHash() {
		return hash;
	}
}
