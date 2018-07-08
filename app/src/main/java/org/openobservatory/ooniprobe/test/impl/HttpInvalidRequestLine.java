package org.openobservatory.ooniprobe.test.impl;

import android.support.annotation.NonNull;

import org.openobservatory.ooniprobe.activity.AbstractActivity;
import org.openobservatory.ooniprobe.model.JsonResult;
import org.openobservatory.ooniprobe.model.Result;
import org.openobservatory.ooniprobe.test.AbstractTest;

import static org.openobservatory.ooniprobe.model.Measurement.MeasurementState.measurementFailed;

public class HttpInvalidRequestLine extends AbstractTest {
	public static final String NAME = "http_invalid_request_line";

	public HttpInvalidRequestLine(AbstractActivity activity, Result result) {
		super(activity, NAME, new org.openobservatory.measurement_kit.nettests.HttpInvalidRequestLineTest(), result);
	}

	/*
         onEntry method for http invalid request line test, check "tampering" key
         null => failed
         true => anomalous
     */
	@Override public void onEntry(@NonNull JsonResult json) {
		JsonResult.TestKeys keys = json.test_keys;
		if (keys.failure != null)
			measurement.state = measurementFailed;
		//TODO
		//else if (Boolean.valueOf(keys.tampering))
		//    measurement.anomaly = true;
		super.onEntry(json);
	}
}
