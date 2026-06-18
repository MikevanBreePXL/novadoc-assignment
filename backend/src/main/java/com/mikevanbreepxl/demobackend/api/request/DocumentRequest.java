package com.mikevanbreepxl.demobackend.api.request;

import com.mikevanbreepxl.demobackend.domain.DocumentType;
import lombok.Getter;
import lombok.Setter;

public class DocumentRequest {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private DocumentType type;

    public DocumentRequest(String name, DocumentType type) {
    this.name = name;
    this.type = type;
    }
}
