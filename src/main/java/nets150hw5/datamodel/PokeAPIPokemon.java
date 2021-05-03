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
    private final int weight;

    private final double rawStrength;

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
        this.rawStrength = this.determineRawStrength();
    }

    private double determineRawStrength() {
        double rawStrength = 0.0;

        rawStrength += base_experience * 0.25;

        for (PokeAPIPokemonStat stat : stats) {
            rawStrength += (double) stat.getBase_stat() / (double) stats.size();
        }

        if (this.weight > 150) rawStrength++;
        if (this.weight > 300) rawStrength++;
        if (this.weight > 450) rawStrength++;
        if (this.weight > 600) rawStrength++;

        return rawStrength;
    }
}
