package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.Servico;
import com.crossover.petitosa.business.enums.StatusServico;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.validation.constraints.NotNull;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
public class ServicosPorStatusDto {

    @Builder.Default
    @NotNull
    @ElementCollection
    private ServicoDto[] pendentes = new ServicoDto[0];

    @Builder.Default
    @NotNull
    @ElementCollection
    private ServicoDto[] aceitos = new ServicoDto[0];

    @Builder.Default
    @NotNull
    @ElementCollection
    private ServicoDto[] emAndamento = new ServicoDto[0];

    @Builder.Default
    @NotNull
    @ElementCollection
    private ServicoDto[] terminados = new ServicoDto[0];

    @Builder.Default
    @NotNull
    @ElementCollection
    private ServicoDto[] rejeitados = new ServicoDto[0];

    @Builder.Default
    @NotNull
    @ElementCollection
    private ServicoDto[] desistidos = new ServicoDto[0];


    public static ServicosPorStatusDto fromServicos(Iterable<Servico> servicos) {
        Map<StatusServico, List<ServicoDto>> statusToServico = new HashMap<>();
        for (StatusServico status : StatusServico.values())
            statusToServico.put(status, new ArrayList<>());
        for (Servico servico : servicos)
            statusToServico.get(servico.getStatus()).add(ServicoDto.fromServico(servico));

        return ServicosPorStatusDto.builder()
                .pendentes(statusToServico.get(StatusServico.PENDENTE).stream().sorted(Comparator.comparing(ServicoDto::getDataSolicitacao).reversed()).toArray(ServicoDto[]::new))
                .aceitos(statusToServico.get(StatusServico.ACEITO).stream().sorted(Comparator.comparing(ServicoDto::getDataAceitacao).reversed()).toArray(ServicoDto[]::new))
                .emAndamento(statusToServico.get(StatusServico.EM_ANDAMENTO).stream().sorted(Comparator.comparing(ServicoDto::getDataInicioRealizacao).reversed()).toArray(ServicoDto[]::new))
                .terminados(statusToServico.get(StatusServico.TERMINADO).stream().sorted(Comparator.comparing(ServicoDto::getDataTerminoRealizacao).reversed()).toArray(ServicoDto[]::new))
                .rejeitados(statusToServico.get(StatusServico.REJEITADO).stream().sorted(Comparator.comparing(ServicoDto::getDataRejeicao).reversed()).toArray(ServicoDto[]::new))
                .desistidos(statusToServico.get(StatusServico.DESISTIDO).stream().sorted(Comparator.comparing(ServicoDto::getDataDesistencia).reversed()).toArray(ServicoDto[]::new))
                .build();
    }

}
