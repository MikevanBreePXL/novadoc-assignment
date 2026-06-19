package com.mikevanbreepxl.demobackend.repository;

import com.mikevanbreepxl.demobackend.domain.DocumentClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentClass, Long> {
}
