package nets150hw5.datamodel;

import lombok.Getter;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@ToString
public class PokeAPIPokemonStat {
    private final String name;
    private final String url;
    private final int base_stat;
    private final int effort;

    public PokeAPIPokemonStat(final String name,
                              final String url,
                              final int base_stat,
                              final int effort) {
        this.name = checkNotNull(name);
        this.url = checkNotNull(url);
        this.base_stat = base_stat;
        this.effort = effort;
    }
}
