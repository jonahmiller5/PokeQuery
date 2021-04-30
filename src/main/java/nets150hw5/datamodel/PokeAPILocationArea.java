package nets150hw5.datamodel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class PokeAPILocationArea {
    private final String name;
    private final String url;

    public PokeAPILocationArea(final String name, final String url) {
        this.name = name;
        this.url = url;
    }
}
