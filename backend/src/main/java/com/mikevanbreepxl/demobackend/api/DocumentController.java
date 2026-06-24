package com.mikevanbreepxl.demobackend.api;

import com.mikevanbreepxl.demobackend.api.dto.DocumentDto;
import com.mikevanbreepxl.demobackend.api.request.DocumentRequest;
import com.mikevanbreepxl.demobackend.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping
    public List<DocumentDto> getAllDocuments() {
        return documentService.getAllDocuments();
    }

//    @GetMapping("{id}")
//    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Long id) {
//        return ResponseEntity.ok(documentService.getDocumentById(id));
//    }

    @PostMapping
    public ResponseEntity<Void> saveDocument(@Valid @RequestBody DocumentRequest request) {
        Long createdId = documentService.saveDocument(request);
        return ResponseEntity.created(URI.create("/documents/" + createdId)).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<DocumentDto> updateDocument(@PathVariable Long id, @Valid @RequestBody DocumentRequest request) {
        DocumentDto updatedDocument = documentService.UpdateDocument(id, request);
        return ResponseEntity.ok(updatedDocument);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocumentById(id);
        return ResponseEntity.noContent().build();
    }

}
