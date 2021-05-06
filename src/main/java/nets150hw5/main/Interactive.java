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
        regions.add("johto");
        regions.add("hoenn");
        regions.add("unova");
        regions.add("sinnoh");
        regions.add("kalos");
    }

    public void initInteractive() {
        String command = "";
        TypeMatchupGraphs types = new TypeMatchupGraphs();
        PokemonInfoRetriever pInfoRetriever = new PokemonInfoRetriever();
        Scanner sc = new Scanner(new InputStreamReader(System.in));
        System.out.println("Hello and welcome to Pokequery, an easy to use Pokemon querying application");
        System.out.println("You can either get information for a type or, alternatively, view a ranked list of the Pokemon available on any route in any region.");
        System.out.println("If you want type information enter \"type\", if you want route-oriented Pokemon rankings type \"rank\"");
        String mode = "";
        while (!(mode.equals("type") || mode.equals("rank"))) {
            mode = sc.nextLine().trim().toLowerCase();
            if (mode.equals("quit")) return;
            if (!(mode.equals("type") || mode.equals("rank"))) System.out.println("That is not a valid input, please try again.");
        }
        if (mode.equals("type")) {
            while (!command.equals("quit")) {
                for (String t : types.getTypesList()) {
                    System.out.println(t);
                }
                System.out.println("Enter a type from the list above to get information.");
                String currentType = "";
                while (!types.getTypesList().contains(currentType)) {
                    currentType = sc.nextLine().trim().toLowerCase();
                    if (currentType.equals("quit")) return;
                    if (!types.getTypesList().contains(currentType)) System.out.println("That is not a valid input, please try again.");
                }
                System.out.println(currentType + " type is 0x effective against:");
                for (String z : types.getZeroTimes(currentType)) {
                    System.out.println(z);
                }
                System.out.println("");
                System.out.println(currentType + " type is 1/2x effective against:");
                for (String h : types.getHalfTimes(currentType)) {
                    System.out.println(h);
                }
                System.out.println("");
                System.out.println(currentType + " type is 1x effective against:");
                for (String o : types.getOneTimes(currentType)) {
                    System.out.println(o);
                }
                System.out.println("");
                System.out.println(currentType + " type is 2x effective against:");
                for (String t : types.getTwoTimes(currentType)) {
                    System.out.println(t);
                }
                System.out.println("To continue getting type information, press enter. To quit, type \"quit\"");
                command = sc.nextLine().trim().toLowerCase();
            }
        }
        else {
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

            String region = "";
            while (!regions.contains(region)) {
                region = sc.nextLine().trim().toLowerCase();
                if (region.equals("quit")) return;
                if(!regions.contains(region)) System.out.println("That is not a valid input, please try again.");
            }

            System.out.println("Loading... This may take a few minutes depending on your internet speed.");
            LocationInfoRetriever lInfoRetriever = new LocationInfoRetriever(region);
            while (!command.equals("quit")) {
                Set<String> locationNames = lInfoRetriever.getAllLocationNames();
                for (String loc : locationNames) {
                    System.out.println(loc.replaceAll("-", " "));
                }
                System.out.println("Type a location from the list above");
                System.out.println();

                String locationName = sc.nextLine().trim().toLowerCase().replaceAll(" ", "-");
                while (!locationNames.contains(locationName)) {
                    if (locationName.equals("quit")) return;
                    locationName = sc.nextLine().trim().toLowerCase().replaceAll(" ", "-");
                    if (!locationNames.contains(locationName)) System.out.println("That is not a valid input, please try again.");
                }
                PokeAPILocation location = lInfoRetriever.getLocationByName(locationName);
                PokemonComparerForLocation comparer = new PokemonComparerForLocation(types, pInfoRetriever, location);
                int rank = 1;
                for (PokeAPIPokemon pokemon : comparer.rankPokemon()) {
                    System.out.println(rank + ". " + pokemon.getName());
                    rank++;
                }

                System.out.println("");
                System.out.println("To continue, simply press enter. If you wish to quit, type \"quit\"");
                System.out.println("");
                command = sc.nextLine();
            }
        }
    }
}
