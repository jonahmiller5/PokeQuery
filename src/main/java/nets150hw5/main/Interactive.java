package nets150hw5.main;

import java.io.InputStreamReader;
import java.util.Scanner;

import nets150hw5.businesslogic.LocationInfoRetriever;
import nets150hw5.businesslogic.PokemonComparerForLocation;
import nets150hw5.businesslogic.PokemonInfoRetriever;
import nets150hw5.businesslogic.TypeMatchupGraphs;
import nets150hw5.datamodel.PokeAPILocation;
import nets150hw5.datamodel.PokeAPIPokemon;

public class Interactive {
    
    public Interactive() {
        
    }
    
    public void initInteractive() {
        String command = "";
        TypeMatchupGraphs types = new TypeMatchupGraphs();
        PokemonInfoRetriever pInfoRetriever = new PokemonInfoRetriever();
        Scanner sc = new Scanner(new InputStreamReader(System.in));
        System.out.println("Please type in a region from the following list:");
        System.out.println("kanto");
        System.out.println("jhoto");
        System.out.println("hoen");
        System.out.println("sinnoh");
        System.out.println("unova");
        System.out.println("kalos");
        System.out.println("alola");
        System.out.println("galar");
        System.out.println("");
        String region = sc.nextLine();
        System.out.println("Loading... This may take a few minutes");
        LocationInfoRetriever lInfoRetriever = new LocationInfoRetriever(region);
        while (!command.equals("quit")) {
            for (String loc : lInfoRetriever.getAllLocationNames()) {
                System.out.println(loc);
            }
            System.out.println("Type a location from the list above");
            System.out.println();
            String locationName = sc.nextLine();
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
