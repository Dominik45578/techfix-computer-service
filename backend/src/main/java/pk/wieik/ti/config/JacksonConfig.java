package pk.wieik.ti.config;

import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

/** Jedna, wspólna konfiguracja JSON dla całego API. */
final class JacksonConfig {
    private JacksonConfig() {}

    static JsonMapper jsonMapper() {
        return JsonMapper.builder()
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN)
                .build();
    }
}
