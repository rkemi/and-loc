package com.example.locationtrackscan;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScanActivity extends ActionBarActivity {
	
	WifiManager manager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
	//	manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	//	WifiScanReceiver wifiReciever = new WifiScanReceiver();
	//	registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));  
		
		
      //  TextView t2 = new TextView(this);
      TextView t2 = (TextView)findViewById(R.id.scanned_wifis);
      System.out.println("LOGLOGLOGLOGLOGLOG");
      System.out.println("T2: "+t2);
     	//if (manager.startScan()){
      
      t2.setText("Scan made: ");
		//}
       // t2.setText("No scan made: ");
		//List<ScanResult> wifis = getScanResults();
	//	t2.setText("APA");
		//if (!wifis.equals(null)) { 
	//		t2.setText("wifis!!!!!!: " + wifis.size());
	//	}
	//	else {
	//		t2.setText("wifis-----: JÄVLA NULL");
	//	}
	}
	
	public List<ScanResult> getScanResults () {
		
		List <ScanResult> result = manager.getScanResults();
		return result;
	}
	
	class WifiScanReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_scan, container,
					false);
			return rootView;
		}
	}

}
