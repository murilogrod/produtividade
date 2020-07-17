package br.gov.caixa.simtr.modelo.enumerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TipoTratamentoProcessoEnum {

    CONSULTA_DOSSIE(1),
    TRATAR_DOSSIE(2),
    CRIAR_DOSSIE(4),
    PRIORIZAR_DOSSIE(8);

    private final int valor;

    TipoTratamentoProcessoEnum(Integer valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public static List<TipoTratamentoProcessoEnum> decompoeValor(String padrao) {
        //TODO Construir logica para decompor o valor textual na lista de tipos
        return Arrays.asList(TipoTratamentoProcessoEnum.values());
    }
    
    public static List<TipoTratamentoProcessoEnum> decompoeValor(int valor) {
        List<TipoTratamentoProcessoEnum> tipos = new ArrayList<>();
        Integer indice = 0;
        Integer resultado = 1;

        //Percorre as potencias procurando o valor limite para exponencia de 2 sobre o valor total
        while (resultado <= valor) {
            resultado = (int) Math.pow(2, ++indice);
        }
        
        //Realiza as potencias, decrementa o valor da potencia do valor total e inclui o ojeto refente ao valor da potencia.
        // Ao valor chegar em 0 o resultado da potencia pow(2,0) será 1 e consequentemente maior que o acumulado que será 0
        // Neste momento o fluxo é interrompido e os valores encontrados devolvidos
        do {
            resultado = (int) Math.pow(2, --indice);
            if (resultado <= valor) {
                valor -= resultado;
                tipos.add(TipoTratamentoProcessoEnum.valueOf(resultado));
            }
        } while (valor > 0);
        return tipos;
    }

    public static int compoeValor(TipoTratamentoProcessoEnum... tiposTratamentoProcesso) {
        int total = 0;
        for (TipoTratamentoProcessoEnum tipoTratamento : tiposTratamentoProcesso) {
            total += tipoTratamento.getValor();
        }
        return total;
    }

    public static TipoTratamentoProcessoEnum valueOf(int valor) {
        switch (valor) {
            case 1:
                return TipoTratamentoProcessoEnum.CONSULTA_DOSSIE;
            case 2:
                return TipoTratamentoProcessoEnum.TRATAR_DOSSIE;
            case 4:
                return TipoTratamentoProcessoEnum.CRIAR_DOSSIE;
            case 8:
                return TipoTratamentoProcessoEnum.PRIORIZAR_DOSSIE;
        }
        return null;
    }
}
