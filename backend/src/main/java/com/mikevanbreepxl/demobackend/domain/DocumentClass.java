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
    @Enumerated(EnumType.STRING)
    private DocumentType Type;
    @OneToMany
    private List<MetadataField> Metadata;

    public DocumentClass() {
        // JPA Only!
    }

    public DocumentClass(String name, DocumentType type) {
        Name = name;
        Type = type;
    }

    /*
    public void addMetadataField(MetadataType metadataField) {
        MetadataFields.add(metadataField);
    }

    public boolean removeMetadataField(MetadataType metadataField) {
        return MetadataFields.remove(metadataField);
    }
     */
}
