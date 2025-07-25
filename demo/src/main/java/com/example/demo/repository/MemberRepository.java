package com.example.demo.repository;

import com.example.demo.model.members.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
