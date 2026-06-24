package com.mikevanbreepxl.demobackend.api.dto;

import com.mikevanbreepxl.demobackend.domain.ArchiveMetadata;
import lombok.Getter;

import java.util.List;

@Getter
public class ArchiveDto {
    private final String name;
    private final long documentClassId;
    private final List<ArchiveMetadata> metadata;

    public ArchiveDto(String name, long documentClassId, List<ArchiveMetadata> metadata) {
        this.name = name;
        this.documentClassId = documentClassId;
        this.metadata = metadata;
    }
}
