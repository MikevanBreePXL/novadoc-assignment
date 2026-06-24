package com.mikevanbreepxl.demobackend.api.dto;

import com.mikevanbreepxl.demobackend.api.request.MetadataFieldRequest;
import com.mikevanbreepxl.demobackend.domain.MetadataField;
import lombok.Getter;

import java.util.List;

@Getter
public class DocumentDto {
    private final Long id;
    private final String name;
    private final String type;
    private final List<MetadataFieldRequest> metadataFields;

    public DocumentDto(Long id, String name, String type, List<MetadataField> metadata) {
        this.id = id;
        this.name = name;
        this.type = type;
        metadataFields = metadata.stream()
                .map(metadataField -> new MetadataFieldRequest(metadataField.getName(), metadataField.getType()))
                .toList();
    }
}
