package com.glass.brandwatch.asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.lang3.SerializationUtils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.glass.brandwatch.cards.CardBundleActivity;
import com.glass.brandwatch.utils.PropertiesManager;
import com.glass.brandwatch.utils.StreamUtils;

public class RequestBrandDataBluetoothTask extends AsyncTask<Void, Void, ArrayList<String>> {
	private static final String TAG = RequestBrandDataBluetoothTask.class.getSimpleName();

	private BluetoothDevice device;
	private String query;
	private Context context;

	public RequestBrandDataBluetoothTask(Context context, BluetoothDevice device, String query) {
		this.device = device;
		this.query = query;
		this.context = context;
	}

	@Override
	protected ArrayList<String> doInBackground(Void... params) {
		Log.i(TAG, String.format("Sending '%s' to '%s'", query, device.getName()));

		byte[] value = null;
		BluetoothSocket socket = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		
		try {
			// Connect to the socket
			UUID uuid = UUID.fromString(PropertiesManager.getProperty("bluetooth_uuid"));
			socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
			socket.connect();
			
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			
			// Send query to server
			StreamUtils.writeToSocket(outputStream, query);
			
			// Get response
			value = StreamUtils.readFromSocket(inputStream);
			
			// Notify the server that we are done
			StreamUtils.writeToSocket(outputStream, "completed");

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

		// close streams and sockets
		finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
			}

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
			}

			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}
		
		// Deserialise the data we got bake from the server. For now an assumption is made that it is
		// an array list of strings
		ArrayList<String> results = (ArrayList<String>) SerializationUtils.deserialize(value);
		return results;
	}

	@Override
	protected void onPostExecute(ArrayList<String> data) {
		if (data != null) {
			Log.i(TAG, String.format("Received brand data for '%s' from device '%s'", query, device.getName()));
			showCardsActivity(data);
		} else {
			Log.i(TAG, String.format("Failed to query brand data for '%s' from device '%s'", device.getName()));
		}
	}

	// Start the new activity and pass the data array as parameters
	private void showCardsActivity(ArrayList<String> data) {
		Intent intent = new Intent(context, CardBundleActivity.class);
		intent.putStringArrayListExtra("data", data);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}