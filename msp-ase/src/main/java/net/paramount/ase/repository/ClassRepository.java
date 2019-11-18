package net.paramount.ase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.paramount.ase.entity.MemberClass;

public interface ClassRepository extends JpaRepository<MemberClass, Long>, JpaSpecificationExecutor {
    List<MemberClass> findAllByNameContainsIgnoreCase(String searchString);
}
