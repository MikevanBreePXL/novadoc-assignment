package com.mikevanbreepxl.demobackend.api.request;

import com.mikevanbreepxl.demobackend.domain.DocumentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DocumentRequest(
    @NotBlank String name,
    @NotNull DocumentType documentType,
    @NotEmpty(message = "At least one metadata field is required") @Valid List<MetadataFieldRequest> metadataFields
) { }
