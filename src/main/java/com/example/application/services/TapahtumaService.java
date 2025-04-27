package com.example.application.services;

import com.example.application.data.Jarjestaja;
import com.example.application.data.Paikka;
import com.example.application.data.Tapahtuma;
import com.example.application.repository.JarjestajaRepository;
import com.example.application.repository.PaikkaRepository;
import com.example.application.repository.TapahtumaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TapahtumaService {

    private final TapahtumaRepository tapahtumaRepository;
    private final JarjestajaRepository jarjestajaRepository;
    private final PaikkaRepository paikkaRepository;

    public TapahtumaService(TapahtumaRepository tapahtumaRepository,
                            JarjestajaRepository jarjestajaRepository,
                            PaikkaRepository paikkaRepository) {
        this.tapahtumaRepository = tapahtumaRepository;
        this.jarjestajaRepository = jarjestajaRepository;
        this.paikkaRepository = paikkaRepository;
    }

    public List<Tapahtuma> findAll() {
        return tapahtumaRepository.findAll();
    }

    public Tapahtuma save(Tapahtuma tapahtuma) {
        if (tapahtuma.getJarjestaja() != null) {
            Jarjestaja tallennettuJarjestaja = jarjestajaRepository.save(tapahtuma.getJarjestaja());
            tapahtuma.setJarjestaja(tallennettuJarjestaja);
        }

        if (tapahtuma.getPaikka() != null) {
            Paikka tallennettuPaikka = paikkaRepository.save(tapahtuma.getPaikka());
            tapahtuma.setPaikka(tallennettuPaikka);
        }

        return tapahtumaRepository.save(tapahtuma);
    }

    public void delete(Tapahtuma tapahtuma) {
        tapahtumaRepository.delete(tapahtuma);
    }

    public Tapahtuma findById(Long id) {
        return tapahtumaRepository.findById(id).orElse(null);
    }
}
