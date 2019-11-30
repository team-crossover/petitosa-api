package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.Servico;
import com.crossover.petitosa.business.enums.StatusServico;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .pendentes(statusToServico.get(StatusServico.PENDENTE).toArray(new ServicoDto[0]))
                .aceitos(statusToServico.get(StatusServico.ACEITO).toArray(new ServicoDto[0]))
                .emAndamento(statusToServico.get(StatusServico.EM_ANDAMENTO).toArray(new ServicoDto[0]))
                .terminados(statusToServico.get(StatusServico.TERMINADO).toArray(new ServicoDto[0]))
                .rejeitados(statusToServico.get(StatusServico.REJEITADO).toArray(new ServicoDto[0]))
                .desistidos(statusToServico.get(StatusServico.DESISTIDO).toArray(new ServicoDto[0]))
                .build();
    }

}
