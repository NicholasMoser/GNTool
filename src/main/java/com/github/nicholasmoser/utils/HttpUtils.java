package com.github.nicholasmoser.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.json.JSONArray;
import java.net.http.HttpClient;

public class HttpUtils {

  /**
   * Download a JSONArray from a URL via a GET HTTP call.
   *
   * @param url The URL to call.
   * @return A JSONArray.
   * @throws IOException If there is a failure downloading or parsing the JSONArray.
   */
  public static JSONArray getJSON(String url) throws IOException {
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder(URI.create(url))
          .header("accept", "application/json")
          .build();
      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      String body = response.body();
      return new JSONArray(body);
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
}
