package com.mikevanbreepxl.demobackend.api.request;

import com.mikevanbreepxl.demobackend.domain.ArchiveMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ArchiveRequest(
        @NotBlank String name,
        @Positive long documentClassId,
        @NotNull List<ArchiveMetadata> metadata
) { }
