package com.example.demo.repository;

import com.example.demo.model.lists.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Lists tablosu için temel CRUD işlemlerini sağlayan repository arayüzü
@Repository
public interface ListsRepository extends JpaRepository<Lists, Integer> {
    // Ek sorgular gerekiyorsa buraya ekleyebilirsin
}