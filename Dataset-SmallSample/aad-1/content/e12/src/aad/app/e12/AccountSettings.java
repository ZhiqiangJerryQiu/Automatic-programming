
package aad.app.e12;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AccountSettings extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.account_settings_preferences);
    }

}
