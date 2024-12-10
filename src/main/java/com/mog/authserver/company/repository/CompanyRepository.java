package com.mog.authserver.company.repository;

import com.mog.authserver.company.domain.CompanyEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    Optional<CompanyEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
