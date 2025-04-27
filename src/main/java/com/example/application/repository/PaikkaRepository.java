package com.example.application.repository;

import com.example.application.data.Paikka;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaikkaRepository extends JpaRepository<Paikka, Long> {}
