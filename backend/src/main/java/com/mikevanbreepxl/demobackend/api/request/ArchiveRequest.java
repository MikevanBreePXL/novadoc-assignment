package com.mikevanbreepxl.demobackend.api.request;

import com.mikevanbreepxl.demobackend.domain.ArchiveMetadata;

import java.util.List;

public record ArchiveRequest(
        String name,
        long documentClassId,
        List<ArchiveMetadata> metadata
) { }
