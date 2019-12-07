package com.crossover.petitosa.data.config;

import com.crossover.petitosa.business.entity.ContaBancaria;
import com.crossover.petitosa.business.enums.EspecieAnimal;
import com.crossover.petitosa.business.enums.PorteAnimal;
import com.crossover.petitosa.business.enums.TipoConta;
import com.crossover.petitosa.business.network.PoderosasBankNetwork;
import com.crossover.petitosa.business.service.*;
import com.crossover.petitosa.presentation.dto.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Log4j2
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private EnderecoService endercoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ContratanteService contratanteService;

    @Autowired
    private PrestadorService prestadorService;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private PoderosasBankNetwork poderosasBankNetwork;

    private boolean lodadedInitialData = false;

    private ContaBancaria contaPetitosa;

    public ContaBancaria getContaPetitosa() {
        return contaPetitosa;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!lodadedInitialData)
            loadInitialData();
    }

    private void loadInitialData() {
        findContaPetitosa();
        createInitialUsers();
        lodadedInitialData = true;
    }

    private void findContaPetitosa() {
        String username = "petitosa";
        String senha = "123";

        ContaBancariaDto contaPetitosaDto = poderosasBankNetwork.getContaByUsuario("petitosa", "123");
        contaPetitosa = ContaBancaria.builder()
                .id(contaPetitosaDto.getId())
                .tipo((contaPetitosaDto.getTipo()))
                .numero(contaPetitosaDto.getNumero())
                .agencia(contaPetitosaDto.getAgencia())
                .digito(contaPetitosaDto.getDigito())
                .build();
        log.warn("CONTA BANCÁRIA DA PETITOSA: " + contaPetitosa);
    }

    private void createInitialUsers() {
        if (usuarioService.findByEmail("c@c.com") != null)
            return;

        NovoEnderecoDto novoEnderecoDto = NovoEnderecoDto.builder()
                .cep(74303360)
                .logradouro("Rua C 95")
                .build();

        NovoEnderecoDto novoEnderecoDto2 = NovoEnderecoDto.builder()
                .cep(74565570)
                .logradouro("Rua Aimorés")
                .build();

        NovoEnderecoDto novoEnderecoDto3 = NovoEnderecoDto.builder()
                .cep(74303370)
                .logradouro("Rua C 96")
                .build();

        ContaBancariaDto contaBancariaDto = ContaBancariaDto.builder()
                .tipo(TipoConta.CORRENTE)
                .numero("01234")
                .digito("5")
                .agencia("6789")
                .build();

        NovoPrestadorDto novoPrestadorDto = NovoPrestadorDto.builder()
                .email("p@p.com")
                .senha("123")
                .nome("Tudo Lopes")
                .dataNascimento(LocalDate.of(1998, 12, 31))
                .genero("M")
                .endereco(novoEnderecoDto)
                .descricao("Adoro animais e trabalho com eles desde que nasci")
                .servicosPrestados(new Boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true})
                .precos(Arrays.stream(new Double[]{15.0, 30.0, 10.0, 20.0, 35.0, 15.0, 25.0, 40.0, 20.0, 20.0, 35.0, 25.0, 40.0, 30.0, 45.0}).map(BigDecimal::new).toArray(BigDecimal[]::new))
                .contaBancaria(contaBancariaDto)
                .build();
        prestadorService.add(novoPrestadorDto);

        novoPrestadorDto.setEmail("ptcm@p.com");
        novoPrestadorDto.setNome("Tudo Caro Martins");
        novoPrestadorDto.setServicosPrestados(new Boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true});
        novoPrestadorDto.setPrecos(Arrays.stream(new Double[]{25.0, 40.0, 20.0, 30.0, 45.0, 25.0, 35.0, 50.0, 30.0, 30.0, 45.0, 35.0, 50.0, 40.0, 55.0}).map(BigDecimal::new).toArray(BigDecimal[]::new));
        prestadorService.add((novoPrestadorDto));

        novoPrestadorDto.setEmail("pcm@p.com");
        novoPrestadorDto.setNome("Cachorros Moraes");
        novoPrestadorDto.setServicosPrestados(new Boolean[]{true, true, true, true, true, true, true, true, true, false, false, false, false, false, false});
        novoPrestadorDto.setPrecos(Arrays.stream(new Double[]{15.0, 30.0, 10.0, 20.0, 35.0, 15.0, 25.0, 40.0, 20.0, 20.0, 35.0, 25.0, 40.0, 30.0, 45.0}).map(BigDecimal::new).toArray(BigDecimal[]::new));
        prestadorService.add((novoPrestadorDto));

        novoPrestadorDto.setEmail("pgv@p.com");
        novoPrestadorDto.setNome("Gatos Vieira");
        novoPrestadorDto.setServicosPrestados(new Boolean[]{false, false, false, false, false, false, false, false, false, true, true, true, true, true, true});
        novoPrestadorDto.setPrecos(Arrays.stream(new Double[]{15.0, 30.0, 10.0, 20.0, 35.0, 15.0, 25.0, 40.0, 20.0, 20.0, 35.0, 25.0, 40.0, 30.0, 45.0}).map(BigDecimal::new).toArray(BigDecimal[]::new));
        prestadorService.add((novoPrestadorDto));

        novoPrestadorDto.setEmail("pbc@p.com");
        novoPrestadorDto.setNome("Banhos Chyevena");
        novoPrestadorDto.setServicosPrestados(new Boolean[]{true, false, false, true, false, false, true, false, false, true, false, true, false, true, false});
        novoPrestadorDto.setPrecos(Arrays.stream(new Double[]{15.0, 30.0, 10.0, 20.0, 35.0, 15.0, 25.0, 40.0, 20.0, 20.0, 35.0, 25.0, 40.0, 30.0, 45.0}).map(BigDecimal::new).toArray(BigDecimal[]::new));
        prestadorService.add((novoPrestadorDto));

        novoPrestadorDto.setEmail("pts@p.com");
        novoPrestadorDto.setNome("Tosas Siqueira");
        novoPrestadorDto.setServicosPrestados(new Boolean[]{false, true, false, false, true, false, false, true, false, false, true, false, true, false, true});
        novoPrestadorDto.setPrecos(Arrays.stream(new Double[]{15.0, 30.0, 10.0, 20.0, 35.0, 15.0, 25.0, 40.0, 20.0, 20.0, 35.0, 25.0, 40.0, 30.0, 45.0}).map(BigDecimal::new).toArray(BigDecimal[]::new));
        prestadorService.add((novoPrestadorDto));

        novoPrestadorDto.setEmail("ppv@p.com");
        novoPrestadorDto.setNome("Passeios Viana");
        novoPrestadorDto.setServicosPrestados(new Boolean[]{false, false, true, false, false, true, false, false, true, false, false, false, false, false, false});
        novoPrestadorDto.setPrecos(Arrays.stream(new Double[]{15.0, 30.0, 10.0, 20.0, 35.0, 15.0, 25.0, 40.0, 20.0, 20.0, 35.0, 25.0, 40.0, 30.0, 45.0}).map(BigDecimal::new).toArray(BigDecimal[]::new));
        prestadorService.add((novoPrestadorDto));

        novoPrestadorDto.setEmail("ptla@p.com");
        novoPrestadorDto.setNome("Tudo Longe Arruda");
        novoPrestadorDto.setServicosPrestados(new Boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true});
        novoPrestadorDto.setPrecos(Arrays.stream(new Double[]{15.0, 30.0, 10.0, 20.0, 35.0, 15.0, 25.0, 40.0, 20.0, 20.0, 35.0, 25.0, 40.0, 30.0, 45.0}).map(BigDecimal::new).toArray(BigDecimal[]::new));
        novoPrestadorDto.setEndereco(novoEnderecoDto2);
        prestadorService.add((novoPrestadorDto));

        CartaoCreditoDto cartaoCreditoDto = CartaoCreditoDto.builder()
                .numero("5555666677778888")
                .validade("20/2020")
                .cvv("999")
                .build();

        NovoContratanteDto novoContratanteDto = NovoContratanteDto.builder()
                .email("c@c.com")
                .senha("123")
                .nome("Contratante da Silva")
                .dataNascimento(LocalDate.of(1998, 12, 31))
                .genero("F")
                .endereco(novoEnderecoDto3)
                .cartaoCredito(cartaoCreditoDto)
                .build();
        ContratanteDto contratanteDto = contratanteService.add(novoContratanteDto);
        NovoAnimalDto novoAnimalDto = NovoAnimalDto.builder()
                .apelido("Milu")
                .dataNascimento(LocalDate.of(2010, 12, 31))
                .especie(EspecieAnimal.GATO)
                .observacoes("Linda demais")
                .porte(PorteAnimal.GRANDE)
                .raca("Libanês")
                .build();
        animalService.add(contratanteDto.getId(), novoAnimalDto);
        novoAnimalDto = NovoAnimalDto.builder()
                .apelido("Jonitos")
                .dataNascimento(LocalDate.of(2011, 12, 31))
                .especie(EspecieAnimal.CACHORRO)
                .observacoes("Cego porém feliz")
                .porte(PorteAnimal.PEQUENO)
                .raca("Buldog francês")
                .build();
        animalService.add(contratanteDto.getId(), novoAnimalDto);

        novoContratanteDto.setEmail("cnsh@c.com");
        novoContratanteDto.setNome("Naty Sá Hadda");
        contratanteDto = contratanteService.add(novoContratanteDto);
        novoAnimalDto.setApelido("James");
        novoAnimalDto.setObservacoes("Perfeição");
        novoAnimalDto.setRaca("Vira lata");
        novoAnimalDto.setPorte(PorteAnimal.MEDIO);
        animalService.add(contratanteDto.getId(), novoAnimalDto);

        novoContratanteDto.setEmail("cjp@c.com");
        novoContratanteDto.setNome("Jonas Peters");
        contratanteDto = contratanteService.add(novoContratanteDto);
        novoAnimalDto.setApelido("Sujeira");
        novoAnimalDto.setObservacoes("Sempre sujo");
        novoAnimalDto.setRaca("Vira lata");
        novoAnimalDto.setPorte(PorteAnimal.MEDIO);
        animalService.add(contratanteDto.getId(), novoAnimalDto);
    }

}
