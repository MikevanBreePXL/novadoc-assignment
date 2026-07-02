package com.mikevanbreepxl.demobackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Embeddable
public class ArchiveMetadata {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private MetadataType type;
    @Getter
    @Setter
    private String content;

    protected ArchiveMetadata() {
        // JPA Only
    }

    public ArchiveMetadata(String name, MetadataType type,  String content) {
        this.name = name;
        this.type = type;
        this.content = content;
    }

    // validation method called during persistence
    @PrePersist
    @PreUpdate
    public void validateContentMatchesType() throws ParseException {
        if (content == null) return;

        switch(type) {
            case INTEGER -> Integer.parseInt(content); // throws NFE if invalid
            case BOOLEAN -> {
                if (!content.equalsIgnoreCase("true") && !content.equalsIgnoreCase("false")) {
                    throw new IllegalArgumentException("Invalid boolean: " + content);
                }
            }
            case DATE -> {
                // validate ISO-8601 or your date format
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateFormat.parse(content); // throws ParseException if invalid
            }
            case STRING -> {} // always valid
        }
    }
}
