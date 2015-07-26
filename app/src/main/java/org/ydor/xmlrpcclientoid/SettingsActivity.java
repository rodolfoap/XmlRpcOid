package org.ydor.xmlrpcclientoid;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferencesFragment()).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        setupSimplePreferencesScreen();
    }



//    private void setupSimplePreferencesScreen() {
//        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
//        addPreferencesFromResource(R.xml.preferences);
//        bindPreferenceSummaryToValue(findPreference("service_url"));
//        bindPreferenceSummaryToValue(findPreference("command_1"));
//        bindPreferenceSummaryToValue(findPreference("command_2"));
//        bindPreferenceSummaryToValue(findPreference("command_3"));
//        bindPreferenceSummaryToValue(findPreference("command_4"));
//    }
//
//    private static void bindPreferenceSummaryToValue(Preference preference) {
//        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()+":"+preference.toString()); /* RODOLFO */
//        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
//        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
//                PreferenceManager
//                        .getDefaultSharedPreferences(preference.getContext())
//                        .getString(preference.getKey(), ""));
//    }
//
//    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
//        @Override
//        public boolean onPreferenceChange(Preference preference, Object value) {
//        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
//        String stringValue = value.toString();
//        return true;
//        }
//    };
}
