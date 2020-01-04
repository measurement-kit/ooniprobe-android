package org.openobservatory.ooniprobe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.test.suite.AbstractSuite;
import org.openobservatory.ooniprobe.test.suite.WebsitesSuite;
import org.openobservatory.ooniprobe.test.test.AbstractTest;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.noties.markwon.Markwon;

public class OverviewActivity extends AbstractActivity {
	private static final String TEST = "test";
	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.icon) ImageView icon;
	@BindView(R.id.runtime) TextView runtime;
	@BindView(R.id.lastTime) TextView lastTime;
	@BindView(R.id.desc) TextView desc;
	@BindView(R.id.customUrl) Button customUrl;
	private AbstractSuite testSuite;

	public static Intent newIntent(Context context, AbstractSuite testSuite) {
		return new Intent(context, OverviewActivity.class).putExtra(TEST, testSuite);
	}

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		testSuite = (AbstractSuite) getIntent().getSerializableExtra(TEST);
		setTheme(testSuite.getThemeLight());
		setContentView(R.layout.activity_overview);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(testSuite.getTitle());
		icon.setImageResource(testSuite.getIcon());
		customUrl.setVisibility(testSuite.getName().equals(WebsitesSuite.NAME) ? View.VISIBLE : View.GONE);
		Markwon.setMarkdown(desc, getString(testSuite.getDesc1()));
		Result lastResult = Result.getLastResult(testSuite.getName());
		if (lastResult == null)
			lastTime.setText(R.string.Dashboard_Overview_LastRun_Never);
		else
			lastTime.setText(DateUtils.getRelativeTimeSpanString(lastResult.start_time.getTime()));
	}

	@Override protected void onResume() {
		super.onResume();
		testSuite.setTestList((AbstractTest[]) null);
		testSuite.getTestList(getPreferenceManager());
		//TODO convert seconds to minutes and hours when needed
		//if getRuntime = 0 show one hour
		Integer runTime = testSuite.getRuntime(getPreferenceManager());
		if (runTime == 0)
			runTime = 3600;
		runtime.setText(getString(R.string.twoParam, testSuite.getDataUsage(), getString(R.string.Dashboard_Card_Seconds, runTime.toString())));
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				startActivity(PreferenceActivity.newIntent(this, testSuite.getPref(), null));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@OnClick(R.id.run) void onRunClick() {
		Intent intent = RunningActivity.newIntent(this, testSuite);
		if (intent != null)
			ActivityCompat.startActivity(this, intent, null);
	}

	@OnClick(R.id.customUrl) void customUrlClick() {
		startActivity(new Intent(this, CustomWebsiteActivity.class));
	}
}
