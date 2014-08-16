package com.glass.brandwatch.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import android.util.Log;

public class StreamUtils {
	private static final String TAG = StreamUtils.class.getSimpleName();
	private static final int EOF = -1;

	public static byte[] readFromSocket(InputStream inputStream) {
		byte[] result = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		try {
			Boolean end = false;
			int read;
			byte[] bytes = new byte[1024];
			while (!end && (read = inputStream.read(bytes)) != -1) {
				// Is it the end of the message?
				if (bytes[read - 1] == EOF) {
					read = read - 1;
					end = true;
				}
				stream.write(bytes, 0, read);
			}

			result = stream.toByteArray();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}

		return result;
	}

	public static void writeToSocket(OutputStream outStream, String message) {
		try {
			outStream.write(message.getBytes(Charset.forName("UTF-8")));
			outStream.write(-1);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}
}