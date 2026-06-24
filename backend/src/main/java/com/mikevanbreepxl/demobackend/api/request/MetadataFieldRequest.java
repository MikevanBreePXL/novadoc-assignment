package com.mikevanbreepxl.demobackend.api.request;

import com.mikevanbreepxl.demobackend.domain.MetadataType;

public record MetadataFieldRequest(
        String name,
        MetadataType type
) { }
