package com.yumatechnical.konnectandroid.Helper;

import com.yumatechnical.konnectandroid.Model.KeyStrValueStr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static java.sql.DriverManager.println;

public class OKHTTPcommon {

	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

	public OKHTTPcommon() throws IOException {
	}


	public static Request buildRequestWithHeaders(String url, ArrayList<KeyStrValueStr> headers) {
		Request request = new Request.Builder()
				.url(url).build();
//		Request.Builder builder = new Request.Builder();
		Request.Builder builder = request.newBuilder();
		for (KeyStrValueStr header : headers) {
			builder.addHeader(header.getKey(), header.getValue());
		}
		return builder.build();
	}


	public static class GET {

		OkHttpClient client = new OkHttpClient();


		String run(String url) throws IOException {
			Request request = new Request.Builder()
					.url(url).build();
			try (Response response = client.newCall(request).execute()) {
				return response.body().string();
			}
		}

		public String run(Request request) throws IOException {
			try (Response response = client.newCall(request).execute()) {
				return response.body().string();
			}
		}

		//example use:
		public static void main(String[] args) throws IOException {
			OKHTTPcommon.GET example = new GET();
//			String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
			ArrayList<KeyStrValueStr> params = new ArrayList<>(), headers = new ArrayList<>();
//			params.add(new KeyStrValueStr("key", "value"));
//			headers.add(new KeyStrValueStr("key", "value"));
			String response = example.run(
					OKHTTPcommon.buildRequestWithHeaders(
							Tools.buildURLwithParams("https://raw.github.com/square/okhttp/master/README.md",
									params),
							headers));
			System.out.println(response);
		}
	}


	public static class POST {

		OkHttpClient client = new OkHttpClient();


		String post(String url, String json) throws IOException {
			RequestBody body = RequestBody.create(json, JSON);
			Request request = new Request.Builder()
					.url(url)
					.post(body)
					.build();
			try (Response response = client.newCall(request).execute()) {
				return response.body().string();
			}
		}

		String bowlingJson(String player1, String player2) {
			return "{'winCondition':'HIGH_SCORE',"
					+ "'name':'Bowling',"
					+ "'round':4,"
					+ "'lastSaved':1367702411696,"
					+ "'dateStarted':1367702378785,"
					+ "'players':["
					+ "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
					+ "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
					+ "]}";
		}

		//example use:
		public static void main(String[] args) throws IOException {
			OKHTTPcommon.POST example = new OKHTTPcommon.POST();
			String json = example.bowlingJson("Jesse", "Jake");
			String response = example.post("http://www.roundsapp.com/post", json);
			System.out.println(response);
		}
	}


	public static class AsyncGET {

		private final OkHttpClient client = new OkHttpClient();


		public void run(String url) throws Exception {
			Request request = new Request.Builder()
					.url(url)
					.build();

			client.newCall(request).enqueue(new Callback() {
				@Override public void onFailure(Call call, IOException e) {
					e.printStackTrace();
				}

				@Override public void onResponse(Call call, Response response) throws IOException {
					try (ResponseBody responseBody = response.body()) {
						if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

						Headers responseHeaders = response.headers();
						for (int i = 0, size = responseHeaders.size(); i < size; i++) {
							System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
						}

						System.out.println(responseBody.string());
					}
				}
			});
		}

		//example use
//		public static void main(String[] args) throws IOException {
//			OKHTTPcommon.AsyncGET example = new OKHTTPcommon.AsyncGET();
//			Response response = example.run("http://publicobject.com/helloworld.txt")
//			System.out.println(response);
//		}
	}

}
