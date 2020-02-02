package com.lonn.studentassistant.firebaselayer.services;

import com.google.firebase.functions.FirebaseFunctions;
import com.lonn.studentassistant.firebaselayer.api.Future;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.Accessors;

@Accessors(chain = true)
public class EmailService {
	private FirebaseFunctions mFunctions;
	private static EmailService instance;

	public static final EmailService getInstance() {
		if (instance == null) {
			instance = new EmailService();
		}

		return instance;
	}

	private EmailService() {
		mFunctions = FirebaseFunctions.getInstance();
	}

	public Future<Void, Exception> sendEmail(String dest, String accountType, String token, String expiresAt) {
		Future<Void, Exception> result = new Future<>();

		Map<String, Object> data = new HashMap<>();

		data.put("dest", dest);
		data.put("accountType", accountType);
		data.put("token", token);
		data.put("expiresAt", expiresAt);
		data.put("push", true);

		mFunctions.getHttpsCallable("sendMail")
				.call(data)
				.continueWith(task -> {
					Map<String, String> resultData = (Map<String, String>) task.getResult().getData();

					if (resultData != null) {
						String resultDataResult = resultData.get("result");

						if (resultDataResult != null &&
								resultDataResult.equals("success")) {
							result.complete(null);
						}
						else {
							result.completeExceptionally(new Exception(resultData.get("error")));
						}
					}
					else {
						result.completeExceptionally(new Exception("No response"));
					}

					return null;
				});

		return result;
	}
}
