package com.mikevanbreepxl.demobackend.service;

import com.mikevanbreepxl.demobackend.api.dto.DocumentDto;
import com.mikevanbreepxl.demobackend.api.request.DocumentRequest;
import com.mikevanbreepxl.demobackend.api.request.MetadataFieldRequest;
import com.mikevanbreepxl.demobackend.domain.DocumentClass;
import com.mikevanbreepxl.demobackend.domain.DocumentType;
import com.mikevanbreepxl.demobackend.domain.MetadataField;
import com.mikevanbreepxl.demobackend.exceptions.DocumentNotFoundException;
import com.mikevanbreepxl.demobackend.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    public List<DocumentDto> getAllDocuments() {
        List<DocumentClass> documentClasses = documentRepository.findAll();
        return documentClasses.stream()
                .map(documentClass -> new DocumentDto(
                        documentClass.getId(),
                        documentClass.getName(),
                        toTypeString(documentClass.getType()),
                        documentClass.getMetadata() == null ? Collections.emptyList() : documentClass.getMetadata()))
                .collect(toList());
    }

    public Long saveDocument(DocumentRequest request) {
        DocumentClass newDocumentClass = new DocumentClass(request.name(), request.documentType(), convertMetadataFieldRequestToMetadataField(request.metadataFields()));
        return documentRepository.saveAndFlush(newDocumentClass).getId();
    }

    public DocumentDto UpdateDocument(Long documentId, DocumentRequest request) {
        Optional<DocumentClass> documentQuery = documentRepository.findById(documentId);
        if (documentQuery.isEmpty()) {
            throw new DocumentNotFoundException("Document with id " + documentId + " not found");
        }
        DocumentClass documentClass = documentQuery.get();

        documentClass.setName(request.name());
        documentClass.setType(request.documentType());
        documentClass.setMetadata(convertMetadataFieldRequestToMetadataField(request.metadataFields()));
        DocumentClass entity = documentRepository.saveAndFlush(documentClass);
        return new DocumentDto(
                entity.getId(),
                entity.getName(),
                toTypeString(entity.getType()),
                entity.getMetadata() == null ? Collections.emptyList() : entity.getMetadata()
        );
    }

    public void deleteDocumentById(Long id) {
        documentRepository.deleteById(id);
        documentRepository.flush();
    }

    private List<MetadataField> convertMetadataFieldRequestToMetadataField(List<MetadataFieldRequest> request) {
        return request.stream()
                .map(metadataFieldRequest -> new MetadataField(metadataFieldRequest.name(), metadataFieldRequest.type()))
                .toList();
    }

    private String toTypeString(DocumentType type) {
        return type == null ? null : type.toString();
    }
}
