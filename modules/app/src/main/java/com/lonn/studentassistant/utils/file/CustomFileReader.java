package com.lonn.studentassistant.utils.file;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Base64;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.AllArgsConstructor;

import static android.util.Base64.DEFAULT;
import static android.util.Base64.encodeToString;

@AllArgsConstructor
public class CustomFileReader {
	private ContentResolver contentResolver;

	public byte[] readBytes(Uri fileUri) throws IOException {
		if (fileUri != null) {
			InputStream inputStream = contentResolver.openInputStream(fileUri);

			if (inputStream != null) {
				return IOUtils.toByteArray(inputStream);
			}
			else {
				throw new IOException("Error while reading file");
			}
		}
		else {
			throw new IOException("File not found");
		}
	}

	public String readBase64(Uri fileUri) throws IOException {
		byte[] fileContentByteArray = readBytes(fileUri);

		return encodeToString(fileContentByteArray,
				DEFAULT);
	}

	public String readStringContent(Uri fileUri) throws IOException {
		if (fileUri != null) {
			InputStream inputStream = contentResolver.openInputStream(fileUri);

			if (inputStream != null) {
				BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder result = new StringBuilder();

				for (String line; (line = r.readLine()) != null; ) {
					result.append(line).append('\n');
				}

				return result.toString();
			}
			else {
				throw new IOException("Error while reading file");
			}
		}
		else {
			throw new IOException("File not found");
		}
	}

	public long getFileSize(Uri fileUri) {
		Cursor cursor = contentResolver.query(fileUri,
				null, null, null, null);
		long size = -1;

		if (cursor != null) {
			cursor.moveToFirst();
			size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
			cursor.close();
		}

		return size;
	}

	public String getFileName(Uri fileUri) {
		Cursor cursor = contentResolver.query(fileUri,
				null, null, null, null);
		String name = null;

		if (cursor != null) {
			cursor.moveToFirst();
			name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
			cursor.close();
		}

		if (name != null) {
			return name.substring(0, name.lastIndexOf("."));
		}

		return null;
	}
}
