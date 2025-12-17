package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
@Getter
@Setter
@Accessors(fluent = true)

public class DuckIdProperties {

    @JsonProperty("id")
    private String id;
    @JsonProperty("color")
    private String colorId;
    @JsonProperty("height")
    private double heightId;
    @JsonProperty("material")
    private String materialId;
    @JsonProperty("sound")
    private String soundId;
    @JsonProperty("wingsState")
    private String wingsStateId;
}
