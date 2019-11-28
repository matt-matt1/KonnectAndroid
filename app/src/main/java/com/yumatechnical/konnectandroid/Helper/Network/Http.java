package com.yumatechnical.konnectandroid.Helper.Network;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yumatechnical.konnectandroid.MainActivity;
import com.yumatechnical.konnectandroid.Model.MicrosoftGraph.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.yumatechnical.konnectandroid.Helper.MSALActivity.MSGRAPH_URL;


public class Http /*extends AppCompatActivity*/ {

	private RequestQueue requestQueue;
	private static Http mInstance;
/*
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
*/

	public static synchronized Http getInstance() {
		if (null == mInstance)
			mInstance = new Http();
		return mInstance;
	}

	/*
	Create a getRequestQueue() method to return the instance of
	RequestQueue.This kind of implementation ensures that
	the variable is instatiated only once and the same
	instance is used throughout the application
	*/
	public RequestQueue getRequestQueue(Context context) {
		if (requestQueue == null)
			requestQueue = Volley.newRequestQueue(context);
		return requestQueue;
	}

	/*
	public method to add the Request to the the single
	instance of RequestQueue created above.Setting a tag to every
	request helps in grouping them. Tags act as identifier
	for requests and can be used while cancelling them
	*/
	public void addToRequestQueue(Context context, Request request, String tag) {
		request.setTag(tag);
		getRequestQueue(context).add(request);
	}

	/*
	Cancel all the requests matching with the given tag
	*/
	public void cancelAllRequests(Context context, String tag) {
		getRequestQueue(context).cancelAll(tag);
	}

	public static JsonObjectRequest makeRequest(int method, String url,
	                                            @Nullable JSONObject jsonObject,//<--parameters
	                                            Response.Listener<JSONObject> listener,
	                                            @Nullable Response.ErrorListener errorListener,
	                                            Map<String, String> headers,
	                                            RetryPolicy retryPolicy) {
		JsonObjectRequest request =
				new JsonObjectRequest(method, url, jsonObject, listener, errorListener) {
					@Override
					public Map<String, String> getHeaders() {
						return headers;
					}
				};
		request.setRetryPolicy(retryPolicy);
//		request.setRetryPolicy(new DefaultRetryPolicy(
//				3000,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		return request;
//		try {
//			Http.getInstance().addToRequestQueue(request, request.getUrl());//"MSKeyValue");
//		} catch (Exception e) {
//			queue.add(request);
//		}
	}
}
