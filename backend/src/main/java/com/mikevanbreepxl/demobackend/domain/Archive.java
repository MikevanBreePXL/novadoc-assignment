package com.mikevanbreepxl.demobackend.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
public class Archive {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    private String name;
    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    private DocumentClass documentClass;
    @Getter
    @ElementCollection(fetch = FetchType.EAGER)
    private List<ArchiveMetadata> metadata;

    protected Archive() {
        // JPA Only
    }

    public Archive(String name, DocumentClass documentClass, List<ArchiveMetadata> metadata) {
        this.name = name;
        this.documentClass = documentClass;
        this.metadata = metadata;
    }
}
