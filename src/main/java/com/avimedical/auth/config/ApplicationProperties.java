package com.avimedical.auth.config;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ConfigProperties(prefix = "auth")
public class ApplicationProperties {

    @NotNull @Valid FirebaseProperties firebase;

    @Getter
    @Setter
    public static class FirebaseProperties {

        @NotNull String serviceKey;

    }
}
