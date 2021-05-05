package nets150hw5.main;

import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import nets150hw5.businesslogic.LocationInfoRetriever;
import nets150hw5.businesslogic.PokemonComparerForLocation;
import nets150hw5.businesslogic.PokemonInfoRetriever;
import nets150hw5.businesslogic.TypeMatchupGraphs;
import nets150hw5.datamodel.PokeAPILocation;
import nets150hw5.datamodel.PokeAPIPokemon;

public class Interactive {

    private static final Set<String> regions = new HashSet<>();
    static {
        regions.add("kanto");
        regions.add("jhoto");
        regions.add("hoenn");
        regions.add("unova");
        regions.add("sinnoh");
        regions.add("kalos");
        regions.add("alola");
    }

    public void initInteractive() {
        String command = "";
        TypeMatchupGraphs types = new TypeMatchupGraphs();
        PokemonInfoRetriever pInfoRetriever = new PokemonInfoRetriever();
        Scanner sc = new Scanner(new InputStreamReader(System.in));

        System.out.println("Please type in a region from the following list:");
        StringBuilder sb = new StringBuilder();
        for (String region : regions) {
            String properRegion = Character.toUpperCase(region.charAt(0)) + region.substring(1, region.length());
            sb.append(properRegion);
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb);

        String region = sc.nextLine().trim().toLowerCase();
        if (!regions.contains(region)) {
            System.out.println("That's not a region! Don't be a jerk. Goodbye!");
            return;
        }

        System.out.println("Loading... This may take a few minutes depending on your internet speed.");
        LocationInfoRetriever lInfoRetriever = new LocationInfoRetriever(region);
        while (!command.equals("quit")) {
            Set<String> locationNames = lInfoRetriever.getAllLocationNames();
            for (String loc : locationNames) {
                System.out.println(loc);
            }
            System.out.println("Type a location from the list above");
            System.out.println();

            String locationName = sc.nextLine();
            if (!locationNames.contains(locationName)) continue;
            PokeAPILocation location = lInfoRetriever.getLocationByName(locationName);
            PokemonComparerForLocation comparer = new PokemonComparerForLocation(types, pInfoRetriever, location);
            for (PokeAPIPokemon pokemon : comparer.rankPokemon()) {
                System.out.println(pokemon.getName());
            }

            System.out.println("");
            System.out.println("To continue, simply press enter. If you wish to quit, type \"quit\"");
            System.out.println("");
            command = sc.nextLine();
        }
    }
}
