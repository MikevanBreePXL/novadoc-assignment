package com.mikevanbreepxl.demobackend.repository;

import com.mikevanbreepxl.demobackend.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
