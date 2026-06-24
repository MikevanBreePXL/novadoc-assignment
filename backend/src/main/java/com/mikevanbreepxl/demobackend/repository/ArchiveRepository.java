package com.mikevanbreepxl.demobackend.repository;

import com.mikevanbreepxl.demobackend.domain.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {
}
