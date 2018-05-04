package kpractice.example.com.gbooks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private EditTextPreference mServerIP;
    private CheckBoxPreference mIsConnectRemoteServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);

        PreferenceManager manager = getPreferenceManager();
        SharedPreferences sp = getPreferenceManager().getSharedPreferences();

        mIsConnectRemoteServer = (CheckBoxPreference) manager.findPreference("is_connect_remote_server");
        mIsConnectRemoteServer.setOnPreferenceChangeListener(this);
        Boolean isConnect = sp.getBoolean("is_connect_remote_server", false);
        mIsConnectRemoteServer.setChecked(isConnect);

        mServerIP = (EditTextPreference) manager.findPreference("server_ip");
        if (isConnect) {
            mServerIP.setOnPreferenceChangeListener(this);
            String IPaddress = sp.getString("server_ip", "0.0.0.1");
            mServerIP.setSummary(IPaddress);
        } else {
            mServerIP.setEnabled(false);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mServerIP) {
            preference.setSummary(newValue.toString());
        }
        if (preference == mIsConnectRemoteServer) {
            mServerIP.setEnabled((Boolean)newValue);
        }
        return true;
    }
}
