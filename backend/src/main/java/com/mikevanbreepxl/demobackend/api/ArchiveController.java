package com.mikevanbreepxl.demobackend.api;

import com.mikevanbreepxl.demobackend.api.dto.ArchiveDto;
import com.mikevanbreepxl.demobackend.api.request.ArchiveRequest;
import com.mikevanbreepxl.demobackend.service.ArchiveService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/archive")
@RestController
@RequiredArgsConstructor
public class ArchiveController {
    private final ArchiveService archiveService;

    @GetMapping
    public ResponseEntity<List<ArchiveDto>> getArchives(@Nullable @RequestParam Long typeId) {
        if (typeId != null) {
            return ResponseEntity.ok(
                    archiveService.getAllArchives().stream()
                    .filter(archiveDto -> archiveDto.getDocumentClassId() == typeId)
                    .toList()
            );
        }
        return ResponseEntity.ok(archiveService.getAllArchives());
    }

    @PostMapping
    public ResponseEntity<Void> saveArchive(ArchiveRequest archiveRequest) {
        Long createdId = archiveService.saveArchive(archiveRequest);
        return ResponseEntity.created(URI.create("/archive/" + createdId)).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ArchiveDto> getArchive(@PathVariable Long id) {
        return archiveService.getArchiveById(id);
    }
}
