package Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Outlet {

    @JsonProperty("count_n")
    private int count;
}
