package apps.jizzu.simpletodo.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import apps.jizzu.simpletodo.R;

import static android.content.Intent.ACTION_VIEW;

/**
 * Fragment which contains information about used open source libraries.
 */
public class LicensesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about_licences);
        addLicenses();
    }

    private void addLicenses() {
        findPreference("vega_layout_manager").setOnPreferenceClickListener(
                createPreferenceClickListener("https://github.com/xmuSistone/VegaLayoutManager")
        );
        findPreference("circular_anim").setOnPreferenceClickListener(
                createPreferenceClickListener("https://github.com/XunMengWinter/CircularAnim")
        );
    }

    private Preference.OnPreferenceClickListener createPreferenceClickListener(
            final String uriString
    ) {
        return new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            }
        };
    }
}
