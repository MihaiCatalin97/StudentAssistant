package com.lonn.studentassistant.utils.file;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

public class FileUtils {
	public static String getFileTypeFromMime(String mime) {
		String result = mime.split("/")[0];

		if (result.equals("*"))
			return "file";
		return result;
	}

	public static String getMimeFromUri(@NonNull Context context,
										@NonNull Uri fileUri) {
		String mimeType;

		if (ContentResolver.SCHEME_CONTENT.equals(fileUri.getScheme())) {
			ContentResolver cr = context.getContentResolver();
			mimeType = cr.getType(fileUri);
		}
		else {
			String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri
					.toString());
			mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
					fileExtension.toLowerCase());
		}

		return mimeType;
	}
}
