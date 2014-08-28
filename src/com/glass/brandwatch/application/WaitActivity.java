package com.glass.brandwatch.application;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.glass.brandwatch.R;
import com.glass.brandwatch.asynctask.RequestBrandDataBluetoothTask;
import com.glass.brandwatch_shared.interfaces.WaitActivityInterface;

public class WaitActivity extends Activity implements WaitActivityInterface {
	static final private String TAG = WaitActivity.class.getSimpleName();
	static final private int ENABLE_BLUETOOTH = 1;

	private BluetoothAdapter bluetoothAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wait);
		initialiseBluetooth();
	}

	private void initialiseBluetooth() {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (bluetoothAdapter == null) {
			Log.w(TAG, "Bluetooth not supported on this device");
		} else {
			if (bluetoothAdapter.isEnabled() == false) {
				enableBluetooth();
			} else {
				sendProductInfo();
			}
		}
	}

	private void enableBluetooth() {
		Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == ENABLE_BLUETOOTH) {
			sendProductInfo();
		}
	}

	private void sendProductInfo() {
		Intent intent = getIntent();
		String query = intent.getStringExtra("query");

		Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();

		if (bluetoothDevices.size() == 0) {
			Log.w(TAG, "Glass is not connected to any bluetooth device.");
			return;
		}

		for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
			// For now we assume that the first device is the Android phone with
			// the server on it
			new RequestBrandDataBluetoothTask(getApplicationContext(), device, query).execute();
			break;
		}
	}
}
