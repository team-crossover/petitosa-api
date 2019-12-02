package com.crossover.petitosa.business.network;

import com.crossover.petitosa.business.entity.Endereco;
import lombok.extern.log4j.Log4j2;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Log4j2
@Component
public class CepNetwork {

    private static final String CEPABERTO_CEP_URL = "http://www.cepaberto.com/api/v3/cep?cep=";

    private static final String CEPABERTO_TOKEN = "f30e450093d3d66dd85a2c466a4a5b21";

    private HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
    private RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

    @Transactional
    public Endereco findEnderecoByCep(int cep) {
        String url = CEPABERTO_CEP_URL + String.format("%08d", cep);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token token=" + CEPABERTO_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<CepAbertoEndereco> response = restTemplate.exchange(url, HttpMethod.GET, entity, CepAbertoEndereco.class);
        return response.getBody().toEndereco();
    }

    private static class CepAbertoEndereco {

        public Estado estado;
        public Cidade cidade;
        public String bairro;
        public String logradouro;
        public Integer cep;
        public Double latitude;
        public Double longitude;

        public Endereco toEndereco() {
            return Endereco.builder()
                    .uf(estado == null ? null : estado.sigla)
                    .cidade(cidade == null ? null : cidade.nome)
                    .bairro(bairro)
                    .logradouro(logradouro)
                    .cep(cep)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
        }

        private static class Estado {
            public String sigla;
        }

        private static class Cidade {
            public String nome;
        }
    }
}
