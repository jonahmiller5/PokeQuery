package nets150hw5.businesslogic;

import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class PokeAPICaller {

    private static final String API_ENDPOINT_PREFIX = "https://pokeapi.co/api/v2/";
    private static final String JSON_MIME_TYPE = "application/json";
    private static final String USER_AGENT_NAME = "JonesAndJonahsMagicalAdventure";

    private final JsonParser jsonParser;

    public PokeAPICaller() {
        this.jsonParser = new JsonParser();
    }

    /**
     * A simple method to get a JSON response from an API endpoint.
     * @param apiEndpoint   The API endpoint from which content is requested
     * @return              The JSON response from the endpoint
     * @throws IOException  If there is an issue connecting to the API endpoint
     */
    public String getJsonResponse(final String apiEndpoint) throws IOException {
        // ensures URL is for ProPublica
        checkArgument(checkNotNull(apiEndpoint).startsWith(API_ENDPOINT_PREFIX),
                String.format("Endpoint %s was not a valid API endpoint. Try again with a better URI.", apiEndpoint));

        // establish HTTPS connection
        final URL url = new URL(apiEndpoint);
        final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setRequestProperty("User-Agent", USER_AGENT_NAME);

        // ensures non-error response code
        final int responseCode = httpsURLConnection.getResponseCode();
        if (responseCode >= 400 && responseCode < 500)
            throw new IllegalArgumentException(
                    String.format("Error code %d returned for response to %s. Try again.", responseCode, apiEndpoint));
        else if (responseCode > 500)
            throw new IllegalStateException(
                    String.format("Error code %d returned for response to %s. Try again.", responseCode, apiEndpoint));

        // ensures JSON content only
        final String contentType = httpsURLConnection.getContentType();
        if (!contentType.startsWith(JSON_MIME_TYPE))
            throw new IllegalArgumentException(
                    String.format("Endpoint %s returned non-JSON content. Try again with a better URI.", apiEndpoint));

        // retrieves actual JSON text
        int len = httpsURLConnection.getContentLength();
        final InputStream in = httpsURLConnection.getInputStream();
        int i = 0;
        final StringBuilder sb = new StringBuilder();
        while (i != -1) {
            len--;

            i = in.read();

            sb.append((char) i);
        }

        final String result = sb.toString();
        System.out.println(len);

        return result;
    }
}
