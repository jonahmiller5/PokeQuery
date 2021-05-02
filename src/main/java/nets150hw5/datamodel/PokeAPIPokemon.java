package nets150hw5.datamodel;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@ToString
public class PokeAPIPokemon {
    private final int base_experience;
    private final String name;
    private final List<String> types;
    private final List<PokeAPIPokemonStat> stats;
    // abilities? TODO
     private final int weight;

    public PokeAPIPokemon(final int base_experience,
                          final String name,
                          final List<String> types,
                          final List<PokeAPIPokemonStat> stats,
                          final int weight) {
        this.base_experience = base_experience;
        this.name = checkNotNull(name);
        this.types = checkNotNull(types);
        this.stats = checkNotNull(stats);
        this.weight = weight;
    }
}
