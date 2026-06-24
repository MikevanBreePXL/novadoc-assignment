package com.mikevanbreepxl.demobackend.service;

import com.mikevanbreepxl.demobackend.api.dto.ArchiveDto;
import com.mikevanbreepxl.demobackend.api.request.ArchiveRequest;
import com.mikevanbreepxl.demobackend.domain.Archive;
import com.mikevanbreepxl.demobackend.exceptions.DocumentNotFoundException;
import com.mikevanbreepxl.demobackend.repository.ArchiveRepository;
import com.mikevanbreepxl.demobackend.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArchiveService {
    private final ArchiveRepository archiveRepository;
    private final DocumentRepository documentRepository;

    public List<ArchiveDto> getAllArchives() {
        return archiveRepository.findAll().parallelStream()
                .map(this::convertArchiveToDto)
                .toList();
    }

    public Long saveArchive(ArchiveRequest request) {
        Archive archive = new Archive(
                request.name(),
                documentRepository.findById(request.documentClassId()).orElseThrow(() -> new DocumentNotFoundException("Document class not found with id: " + request.documentClassId())),
                request.metadata()
        );
        return archiveRepository.saveAndFlush(archive).getId();
    }

    public ResponseEntity<ArchiveDto> getArchiveById(Long id) {
        Archive archive = archiveRepository.findById(id).orElseThrow(() -> new DocumentNotFoundException("Could not find an archive with id '"+id+"'"));
        return ResponseEntity.ok(convertArchiveToDto(archive));
    }

    private ArchiveDto convertArchiveToDto(Archive archive) {
        return new ArchiveDto(
                archive.getName(),
                archive.getDocumentClass().getId(),
                archive.getMetadata()
        );
    }
}
