package nets150hw5.datamodel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class PokeAPILocation {
    private final String name;
    private final String url;

    @Setter
    private List<PokeAPILocationArea> locationAreas = new ArrayList<>();
    @Setter
    private List<String> pokemon = new ArrayList<>();

    /**
     * Constructor.
     * @param name  The location name
     * @param url   The location's PokeAPI url
     */
    public PokeAPILocation(final String name, final String url) {
        this.name = name;
        this.url = url;
    }
}
