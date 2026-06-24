package com.mikevanbreepxl.demobackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Entity
public class DocumentClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long Id;
    @Getter
    @Setter
    private String Name;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType Type;
    @Getter
    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    private List<MetadataField> Metadata;

    protected DocumentClass() {
        // JPA Only
    }

    public DocumentClass(String name, DocumentType type,  List<MetadataField> metadata) {
        Name = name;
        Type = type;
        Metadata = metadata;
    }

}
