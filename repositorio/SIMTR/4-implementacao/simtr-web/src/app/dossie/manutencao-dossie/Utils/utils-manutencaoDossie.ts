import { ConjuntoMacroProcessoGeraDossieUriParams } from "src/app/model/model-produto/conjunto-macro-processo-gera-dossie-uri-params.model";
import { ActivatedRoute } from "@angular/router";
import { ParametrosManutencaoDossie } from "src/app/model/parametros-manutencao-dossie";
import { SITUACAO_DOSSIE_ATUAL } from "src/app/constants/constants";
import { LoaderService } from "src/app/services";

declare var $: any;

export class UtilsManutencao {
    
    static setarValorParametros(route: ActivatedRoute, loadService: LoaderService) {
        let parametros = new ParametrosManutencaoDossie();
        route.params.subscribe(
            (params: any) => {
              parametros.idDossieParams = params['id'];
              parametros.opcaoParams = params['opcao'];
              parametros.processoParams = params['processo'];
              parametros.etapaParams = params['etapa'];
              parametros.macroProcessoGeraDossieUriParams = new ConjuntoMacroProcessoGeraDossieUriParams();
              parametros.macroProcessoGeraDossieUriParams.idMacroProcesso = params['idMacroProcesso'];
              parametros.macroProcessoGeraDossieUriParams.idProcessoGeraDossie = params['idProcessoGeraDossie'];
              parametros.tipoParams = params['tipo'];
            },
            () => {
              loadService.hide();
            });
        return parametros;
    }

    static tipoSituacao(situacaoAlteracao, situacaoAtual) {
      return !situacaoAlteracao && 
        (situacaoAtual == SITUACAO_DOSSIE_ATUAL.AGUARDANDO_TRATAMENTO 
          || situacaoAtual == SITUACAO_DOSSIE_ATUAL.AGUARDANDO_ALIMENTACAO 
          || situacaoAtual == SITUACAO_DOSSIE_ATUAL.PENDENTE_INFORMACAO
          || situacaoAtual == SITUACAO_DOSSIE_ATUAL.RASCUNHO);
    }

    static dossieSalvo(dossiesalvo) {
      return dossiesalvo && dossiesalvo != "false" && dossiesalvo != "" ;
    }
}