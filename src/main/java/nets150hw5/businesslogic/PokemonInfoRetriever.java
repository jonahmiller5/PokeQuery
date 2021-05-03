package nets150hw5.businesslogic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import nets150hw5.datamodel.PokeAPILocation;
import nets150hw5.datamodel.PokeAPIPokemon;
import nets150hw5.datamodel.PokeAPIPokemonStat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PokemonInfoRetriever {
    private static final String POKEMON_ENDPOINT = "https://pokeapi.co/api/v2/pokemon/";

    private final PokeAPICaller pokeCaller;
    private final JsonParser jsonParser;

    public PokemonInfoRetriever() {
        this.pokeCaller = new PokeAPICaller();
        this.jsonParser = new JsonParser();
    }

    public List<PokeAPIPokemon> getPokemonForLocation(final PokeAPILocation location) {
        final List<String> pokemonNames = location.getPokemon();
        final List<PokeAPIPokemon> result = new ArrayList<>(pokemonNames.size());
        for (String name : pokemonNames) {
            String apiResponse = null;
            try {
                apiResponse = pokeCaller.getJsonResponse(POKEMON_ENDPOINT + name);
            } catch (IOException e) {
                System.err.printf("Problem receiving API response from endpoint %s. Check file PokemonInfoRetriever.java", POKEMON_ENDPOINT + name);
                e.printStackTrace();
                return null;
            }


            final JsonObject jObj = jsonParser.parse(apiResponse)
                    .getAsJsonObject();

            if (jObj == null) {
                System.err.println("JSON OBJECT IS NULL");
                System.err.println("API:");
                throw new RuntimeException(apiResponse);
            }

            // retrieve base experience
            final int base_experience = jObj
                    .get("base_experience")
                    .getAsInt();

            // retrieve pokemon types
            final JsonArray jArrTypes = jObj.get("types")
                    .getAsJsonArray();
            final List<String> types = new ArrayList<>(jArrTypes.size());
            for (JsonElement jEltType : jArrTypes) {
                final String type = jEltType
                        .getAsJsonObject()
                        .get("type")
                        .getAsJsonObject()
                        .get("name")
                        .getAsString();
                types.add(type);
            }

            // retrieve stats
            final JsonArray jArrStats = jObj.get("stats")
                    .getAsJsonArray();
            final List<PokeAPIPokemonStat> stats = new ArrayList<>(jArrStats.size());
            for (JsonElement jEltStat : jArrStats) {
                final JsonObject jObjStat = jEltStat.getAsJsonObject();

                final JsonObject jObjStatInternal = jObjStat
                        .get("stat")
                        .getAsJsonObject();

                final String statName = jObjStatInternal
                        .get("name")
                        .getAsString();

                final String statURL = jObjStatInternal
                        .get("url")
                        .getAsString();

                final int base_stat = jObjStat
                        .get("base_stat")
                        .getAsInt();

                final int effort = jObjStat
                        .get("effort")
                        .getAsInt();

                final PokeAPIPokemonStat pokeStat = new PokeAPIPokemonStat(statName, statURL, base_stat, effort);
                stats.add(pokeStat);
            }

            final int weight = jObj
                    .get("weight")
                    .getAsInt();

            final PokeAPIPokemon pokemon = new PokeAPIPokemon(base_experience, name, types, stats, weight);
            result.add(pokemon);
        }

        return result;
    }

    public static void main(String[] args) {
        LocationInfoRetriever l = new LocationInfoRetriever();
        PokemonInfoRetriever i = new PokemonInfoRetriever();

        List<PokeAPILocation> locations = l.generateLocations();

//        p.stream().forEach(System.out::println);

    }
}
