package com.example.application.repository;

import com.example.application.data.Tapahtuma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TapahtumaRepository extends JpaRepository<Tapahtuma, Long> {}
