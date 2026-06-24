package com.mikevanbreepxl.demobackend.api.request;

import com.mikevanbreepxl.demobackend.domain.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DocumentRequest(
    @NotBlank String name,
    @NotNull DocumentType documentType,
    @NotNull List<MetadataFieldRequest> metadataFields
) { }
