package org.openobservatory.ooniprobe.activity;

import android.support.v7.app.AppCompatActivity;

import org.openobservatory.ooniprobe.common.Application;
import org.openobservatory.ooniprobe.common.PreferenceManager;

public abstract class AbstractActivity extends AppCompatActivity {
	//TODO-TEST I changed to public
	public PreferenceManager getPreferenceManager() {
		return ((Application) getApplication()).getPreferenceManager();
	}
}
