package nets150hw5.businesslogic;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.*;
import lombok.Getter;
import nets150hw5.datamodel.PokeAPILocation;
import nets150hw5.datamodel.PokeAPILocationArea;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocationInfoRetriever {

    private static final String LOCATION_ENDPOINT = "https://pokeapi.co/api/v2/location/";
    private static final String REGION_PREFIX_ENDPOINT = "https://pokeapi.co/api/v2/region/";
    private static final String OFFSET_PARAMS = "?offset=0&limit=796";

    private final PokeAPICaller pokeCaller;
    private final JsonParser jsonParser;
    private final String region;

    private final Map<String, List<String>> regionMap = new HashMap<>();
    private final Map<String, PokeAPILocation> locationByNameMap = new HashMap<>();
    private final List<PokeAPILocation> locationList;

    /**
     * Constructor to retrieve relevant info about Pokemon locations.
     */
    public LocationInfoRetriever(final String region) {
        this.pokeCaller = new PokeAPICaller();
        this.jsonParser = new JsonParser();
        this.region = checkNotNull(region);
        this.locationList = this.generateLocations();
    }

    /**
     * Method to retrieve location data from the API to parse and use.
     * @return  {@link List} of {@link PokeAPILocation} objects
     */
    public List<PokeAPILocation> generateLocations() {
        String apiResponse = null;
        try {
            apiResponse = pokeCaller.getJsonResponse(REGION_PREFIX_ENDPOINT + region);
        } catch (IOException e) {
            System.err.printf("Problem receiving API response from endpoint %s. Check file LocationInfoRetriever.java", LOCATION_ENDPOINT);
            return null;
        }

        final JsonArray locationsArr = jsonParser.parse(apiResponse)
                .getAsJsonObject()
                .get("locations")
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

            this.populateLocation(pokeLocation);
            if (pokeLocation.getLocationAreas().isEmpty() || pokeLocation.getPokemon().isEmpty()) continue;

//            System.out.println(pokeLocation);
            pokeLocations.add(pokeLocation);
            this.locationByNameMap.put(pokeLocation.getName(), pokeLocation);
        }

        return Collections.unmodifiableList(pokeLocations);
    }

    /**
     * Method to add to region-location map
     * @param region    region, key
     * @param location  {@link PokeAPILocation} location, value
     */
    private void addLocationToRegion(final String region, final String location) {
        if (!this.regionMap.containsKey(checkNotNull(region))) {
            this.regionMap.put(region, new ArrayList<>());
        }

        this.regionMap.get(region).add(checkNotNull(location));
    }

    /**
     * Getter to find the location namess for a region
     * @param region    Region desired
     * @return  List of Strings for that region
     */
    public List<String> getLocationsByRegion(final String region) {
        return this.regionMap.get(checkNotNull(region));
    }

    /**
     * Getter to get Location metadata by the location's names
     * @param name  The name of the location
     * @return  The corresponding {@link PokeAPILocation} object
     */
    public PokeAPILocation getLocationByName(final String name) {
        return this.locationByNameMap.get(checkNotNull(name));
    }
    
    public List<String> getAllLocationNames() {
        return this.locationList.stream().map(PokeAPILocation::getName).collect(Collectors.toList());
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

        final JsonObject jObjParent = jsonParser.parse(apiResponse)
                .getAsJsonObject();

        try {
            final JsonNull regionObj = jObjParent
                    .get("region")
                    .getAsJsonNull();
            return;
        } catch (IllegalStateException e) {}

        final JsonArray locationAreasArr = jObjParent
                .get("areas")
                .getAsJsonArray();
        if (locationAreasArr.size() == 0) return;

        final String region = jObjParent
                .get("region")
                .getAsJsonObject()
                .get("name")
                .getAsString();

        this.addLocationToRegion(region, location.getName());


        final List<PokeAPILocationArea> locationAreas = new ArrayList<>();
        for (JsonElement jElt : locationAreasArr) {
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
