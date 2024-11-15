import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class APIHandler {
    public <T extends AstralObject> String callAPI(T astralObject, String endpoint, String requestType) {
        int maxRetries = 5;
        int retryCount = 0;
        int waitTime = 1000;
        StringBuilder content = new StringBuilder();
        //handle retry for cases like rate-limiting or server error
        while (retryCount < maxRetries) {
            try {
                StringBuilder domain = new StringBuilder(ApiConfig.ASTRAL_URL);
                if(endpoint.equals(ApiConfig.GOAL)){
                    domain.append("map/").append(ApiConfig.CANDIDATE_ID).append("/").append(endpoint);
                } else
                    domain.append(endpoint);


                URL url = new URL(domain.toString());
                System.out.println("URL being called" + url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(requestType);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                Map<String, Object> jsonMap;

                if(!endpoint.equals(ApiConfig.GOAL)) {
                    Utils utils = new Utils();
                    jsonMap = utils.buildJsonMap(astralObject, endpoint);

                    Gson gson = new Gson();
                    String jsonInputString = gson.toJson(jsonMap);
                    System.out.println("JSON Input String" + jsonInputString);

                    // Send the request
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }

                // Check the response code and process the response
                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                if (responseCode == 500 || responseCode == 429) {
                    String retryAfter = connection.getHeaderField("Retry-After");
                    if (retryAfter != null) {
                        waitTime = Integer.parseInt(retryAfter) * 1000;
                    }
                    System.out.println("Rate limit hit or server error, retrying after " + (waitTime / 1000) + " seconds.");
                    Thread.sleep(waitTime); // Wait before retrying
                    waitTime *= 2; // Exponential backoff
                    retryCount++;
                }
                else if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }

                    in.close();

                    System.out.println("JSON Response: " + content);
                    System.out.println("Request was successful.");
                    break;
                } else {
                    System.out.println("Request failed." + responseCode);
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (retryCount == maxRetries) {
            System.out.println("Max retries reached. Unable to complete the request.");
        }
        return content.toString();
    }

}