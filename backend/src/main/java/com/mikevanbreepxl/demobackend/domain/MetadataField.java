package com.mikevanbreepxl.demobackend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class MetadataField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    private MetadataType Type;
    @Getter
    @Setter
    private String content;

    public MetadataField()
    {
        // JPA Only!
    }

    public MetadataField(String content, MetadataType Type)
    {
        this.content = content;
        this.Type = Type;
    }
}
