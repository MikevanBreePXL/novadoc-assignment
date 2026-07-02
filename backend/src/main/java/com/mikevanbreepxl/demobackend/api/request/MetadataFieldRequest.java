package com.mikevanbreepxl.demobackend.api.request;

import com.mikevanbreepxl.demobackend.domain.MetadataType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MetadataFieldRequest(
        @NotBlank String name,
        @NotNull MetadataType type
) { }
