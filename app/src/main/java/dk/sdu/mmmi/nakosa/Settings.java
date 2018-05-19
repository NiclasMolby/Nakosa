package dk.sdu.mmmi.nakosa;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.preference.PreferenceFragmentCompat;

public class Settings extends PreferenceFragment {

    public Settings() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference button = getPreferenceManager().findPreference("logOut");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                System.out.println("Clicked!");
                Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                intent.putExtra("Logout", true);
                startActivity(intent);
                return true;
            }
        });
    }
}
