package com.example.locationtrack2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import com.example.locationtrack.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;
import android.provider.Settings.Secure;



public class MainActivity extends ActionBarActivity {

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";



	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    public void displayMessage(View view){
    	
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm:ss");
        
        String time = "" +dateFormat.format(cal.getTime());
        String phoneId = "" + Secure.getString(MainActivity.this.getContentResolver(),
                Secure.ANDROID_ID);

        TextView t = (TextView)findViewById(R.id.wifi_display); 
        ConnInfo connInfo = getWifiStats();
        t.setText("IP: " + connInfo.getIp() + "\n"
        		+ "MAC: " + connInfo.getMAC() + "\n"
        		+ "Quality: \n"
        		+ "Link: " + connInfo.getQuality().getLink() + "\n"
        		+ "Level: " + connInfo.getQuality().getLevel() + "\n"
        		+ "Noise: " + connInfo.getQuality().getNoise() + "\n");
        


        ////Skicka upp kod
        String url = "http://loc-track.herokuapp.com/new/id="+phoneId+"&time="+time+"&bssid="+connInfo.getMAC();
        Intent i = new Intent(Intent.ACTION_VIEW);  
        i.setData(Uri.parse(url ));  
        startActivity(i); 
        
    }
    
 
    
    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }
    
    private ConnInfo getWifiStats(){
  		String ip = "hej", mac = "nej";
		int qlink = 0, qlevel = 0, qnoise = 0;

    	try {
    		
			BufferedReader arpBuff = new BufferedReader (new FileReader(new File("/proc/net/arp")));
			BufferedReader wifiBuff = new BufferedReader (new FileReader(new File("/proc/net/wireless")));
			String line ="";
			while((line = wifiBuff.readLine()) != null){
				
				String[] words = line.split("[ ]+");
				
				if (words[1].equals("wlan0:")) {
					qlink = Integer.parseInt(words[3].replace(".", ""));
					qlevel = Integer.parseInt(words[4].replace(".", ""));
					qnoise = Integer.parseInt(words[5].replace(".", ""));
				}			}
			wifiBuff.close();
			
			while((line = arpBuff.readLine()) != null){
				String[] words = line.split("[ ]+");
				if (!words[0].equals("IP")) {
					ip = words[0];
					mac = words[3];
				}
			}
			arpBuff.close();
			
			return new ConnInfo(ip, mac, qlink, qlevel, qnoise); 
			
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    	return new ConnInfo(ip, mac, qlink, qlevel, qnoise); 
		
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
