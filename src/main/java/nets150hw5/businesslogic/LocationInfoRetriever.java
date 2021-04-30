package nets150hw5.businesslogic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import nets150hw5.datamodel.PokeAPILocation;
import nets150hw5.datamodel.PokeAPILocationArea;

import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocationInfoRetriever {

    private static final String LOCATION_ENDPOINT = "https://pokeapi.co/api/v2/location/";
    private static final String OFFSET_PARAMS = "?offset=0&limit=796";

    private final PokeAPICaller pokeCaller;
    private final JsonParser jsonParser;

    /**
     * Constructor to retrieve relevant info about Pokemon locations.
     * @param pokeCaller    API data retrieval tool
     */
    public LocationInfoRetriever(final PokeAPICaller pokeCaller) {
        this.pokeCaller = new PokeAPICaller();
        this.jsonParser = new JsonParser();
    }

    /**
     * Method to retrieve location data from the API to parse and use.
     * @return  {@link List} of {@link PokeAPILocation} objects
     */
    private List<PokeAPILocation> generateLocations() {
        String apiResponse = null;
        try {
            apiResponse = pokeCaller.getJsonResponse(LOCATION_ENDPOINT + OFFSET_PARAMS);
        } catch (IOException e) {
            System.err.printf("Problem receiving API response from endpoint %s. Check file LocationInfoRetriever.java", LOCATION_ENDPOINT);
            return null;
        }

        final JsonArray locationsArr = jsonParser.parse(apiResponse)
                .getAsJsonObject()
                .get("results")
                .getAsJsonArray();

        final List<PokeAPILocation> pokeLocations = new ArrayList<>();
        for (JsonElement locElt: locationsArr) {
            final JsonObject locJsonObj = locElt.getAsJsonObject();

            final String name = locJsonObj
                    .get("name")
                    .getAsString();
            final String url = locJsonObj
                    .get("url")
                    .getAsString();

            final PokeAPILocation pokeLocation = new PokeAPILocation(name, url);
            populateLocation(pokeLocation);
            pokeLocations.add(pokeLocation);
        }

        return Collections.unmodifiableList(pokeLocations);
    }

    /**
     * Method to retrieve location area info for a given {@link PokeAPILocation} object.
     * @param location  The location to retrieve information about
     */
    private void fillLocationAreasFor(final PokeAPILocation location) {
        final String locationURL = checkNotNull(location).getUrl();
        String apiResponse = null;
        try {
            apiResponse = pokeCaller.getJsonResponse(locationURL);
        } catch (IOException e) {
            System.err.printf("Problem receiving API response from endpoint %s. Check file LocationInfoRetriever.java", locationURL);
            return;
        }

        final JsonArray locationsArr = jsonParser.parse(apiResponse)
                .getAsJsonObject()
                .get("areas")
                .getAsJsonArray();

        final List<PokeAPILocationArea> locationAreas = new ArrayList<>();
        for (JsonElement jElt : locationsArr) {
            final JsonObject jObj = jElt.getAsJsonObject();

            final String name = jObj
                    .get("name")
                    .getAsString();
            final String url = jObj
                    .get("url")
                    .getAsString();

            final PokeAPILocationArea locationArea = new PokeAPILocationArea(name, url);
            locationAreas.add(locationArea);
        }
        location.setLocationAreas(locationAreas);
    }

    /**
     * Method to retrieve Pokemon area info for a given {@link PokeAPILocation} object.
     * @param location  The location to retrieve information about
     */
    private void fillPokemonFor(final PokeAPILocation location) {
        if (checkNotNull(location).getLocationAreas().isEmpty()) return;

        List<PokeAPILocationArea> locationAreas = location.getLocationAreas();

        Set<String> pokemonNameSet = new HashSet<>();
        for (PokeAPILocationArea locationArea : locationAreas) {
            final String locationAreaUrl = locationArea.getUrl();
            String apiResponse = null;
            try {
                apiResponse = pokeCaller.getJsonResponse(locationAreaUrl);
            } catch (IOException e) {
                System.err.printf("Problem receiving API response from endpoint %s. Check file LocationInfoRetriever.java", locationAreaUrl);
                return;
            }

            final JsonArray locationsArr = jsonParser.parse(apiResponse)
                    .getAsJsonObject()
                    .get("pokemon_encounters")
                    .getAsJsonArray();

            for (JsonElement jElt : locationsArr) {
                final JsonObject jObj = jElt.getAsJsonObject()
                        .get("pokemon")
                        .getAsJsonObject();

                final String pokemonName = jObj
                        .get("name")
                        .getAsString();

                pokemonNameSet.add(pokemonName);
            }
        }
        List<String> pokemonNameList = Collections.unmodifiableList(new ArrayList<>(pokemonNameSet));
        location.setPokemon(pokemonNameList);
    }

    /**
     * Populates location area and Pokemon info for a given location. Uses above methods in conjunction.
     * @param location  The location to retrieve information about
     */
    private void populateLocation(final PokeAPILocation location) {
        fillLocationAreasFor(location);
        fillPokemonFor(location);
    }

}
