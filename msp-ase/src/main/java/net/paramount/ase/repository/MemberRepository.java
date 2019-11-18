package net.paramount.ase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.paramount.ase.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor { }
