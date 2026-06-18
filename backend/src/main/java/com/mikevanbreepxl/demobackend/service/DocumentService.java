package com.mikevanbreepxl.demobackend.service;

import com.mikevanbreepxl.demobackend.api.dto.DocumentDto;
import com.mikevanbreepxl.demobackend.api.request.DocumentRequest;
import com.mikevanbreepxl.demobackend.domain.Document;
import com.mikevanbreepxl.demobackend.exceptions.DocumentNotFoundException;
import com.mikevanbreepxl.demobackend.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.FileSystemNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public DocumentDto getDocumentById(Long id) {
        Document entity = documentRepository.findById(id).orElseThrow(() -> new FileSystemNotFoundException("Document not found"));
        return new DocumentDto(entity.getName(), entity.getType().toString());
    }

    public Long saveDocument(DocumentRequest request) {
        Document newDocument = new Document(request.getName(), request.getType());
        return documentRepository.saveAndFlush(newDocument).getId();
    }

    public DocumentDto UpdateDocument(Long documentId, DocumentRequest request) throws DocumentNotFoundException {
        Optional<Document> documentQuery = documentRepository.findById(documentId);
        if (documentQuery.isEmpty()) {
            throw new DocumentNotFoundException("Document with id " + documentId + " not found");
        }
        Document document = documentQuery.get();

        document.setName(request.getName());
//        document.setMetadataFields(request.getMetadataFields());
        Document entity = documentRepository.saveAndFlush(document);
        return new DocumentDto(entity.getName(), entity.getType().toString());
    }

    public void deleteDocumentById(Long id) {
        documentRepository.deleteById(id);
        documentRepository.flush();
    }
}
