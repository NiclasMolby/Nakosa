package dk.sdu.mmmi.nakosa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Settings extends PreferenceFragmentCompat {

    public Settings() {

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        Preference button = getPreferenceManager().findPreference("logOut");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                System.out.println("Clicked!");
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("Logout", true);
                startActivity(intent);
                return true;
            }
        });
    }
}
