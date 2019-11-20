package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.Endereco;
import com.crossover.petitosa.business.network.CepNetwork;
import com.crossover.petitosa.data.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;

@Service
@Transactional
public class EnderecoService extends CrudService<Endereco, Long, EnderecoRepository> {

    @Autowired
    private CepNetwork cepNetwork;

    public Endereco findEnderecoByCep(Integer cep) {
        Endereco endereco = cepNetwork.findEnderecoByCep(cep);
        if (endereco.getLatitude() == null || endereco.getLongitude() == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        return endereco;
    }

    public double calculateDistanceInMeters(Endereco end1, Endereco end2) {
        return calculateDistanceInMeters(end1.getLatitude(), end1.getLongitude(), end2.getLatitude(), end2.getLongitude());
    }

    public double calculateDistanceInMeters(double lat1, double long1, double lat2, double long2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(long2 - long1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = earthRadius * c;

        // round up to nearest multiple of 100 and dont allow below 0
        return Math.max(0.0, Math.ceil(d / 100.0) * 100.0);
    }
}
