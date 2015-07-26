package org.ydor.xmlrpcclientoid;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by rodolfoap on 7/21/15.
 */
public class MyPreferencesFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
