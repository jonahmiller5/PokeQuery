package nets150hw5.businesslogic;

import javafx.util.Pair;
import nets150hw5.datamodel.PokeAPILocation;
import nets150hw5.datamodel.PokeAPIPokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class PokemonComparer {
    private final TypeMatchupGraphs typeGraphs;              // used to include type relationships between pokemon
    private final PokemonInfoRetriever pokemonInfoRetriever; // used to get info about pokemon
    private final Map<PokeAPIPokemon, List<Pair<PokeAPIPokemon, Double>>> network = new HashMap<>();

    public PokemonComparer(final TypeMatchupGraphs typeGraphs, final PokemonInfoRetriever pokemonInfoRetriever) {
        this.typeGraphs = checkNotNull(typeGraphs);
        this.pokemonInfoRetriever = checkNotNull(pokemonInfoRetriever);
    }

    private void createPokemonNetworkForLocation(final PokeAPILocation location) {
        final List<PokeAPIPokemon> allPokemonFromLocation = pokemonInfoRetriever.getPokemonForLocation(location);
        for (PokeAPIPokemon keyPokemon : allPokemonFromLocation) {
            final List<Pair<PokeAPIPokemon, Double>> valueList = new ArrayList<>(allPokemonFromLocation.size()-1);
            for (PokeAPIPokemon valPokemon : allPokemonFromLocation) {
                if (keyPokemon.equals(valPokemon)) continue;

                List<String> keyTypes = keyPokemon.getTypes();
                List<String> valTypes = valPokemon.getTypes();

                double totalEffect = 0.0;
                for (String keyType : keyTypes) {
                    for (String valType : valTypes) {
                        // TODO
                    }
                }

            }
        }
    }

}
