package com.mikevanbreepxl.demobackend.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;

@Embeddable
public class MetadataField {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private MetadataType type;

    protected MetadataField() {
        // JPA only
    }

    public MetadataField(String name, MetadataType type)
    {
        this.name = name;
        this.type = type;
    }
}
