import { Injectable } from '@angular/core';
import { ApplicationService } from './application/application.service';
import { LOCAL_STORAGE_CONSTANTS } from '../constants/constants';
import { ProcessoPatriarca } from './model/processo-patriarca.model';
import { TipoDocumentoTipologia } from './model/tipo-documento-tipologia.model';
import { FuncaoDocumentalTipologia } from './model/funcao-documental-tipologia.model';


@Injectable({
    providedIn: 'root'
})
export class LocalStorageDataService {

    constructor(private appService: ApplicationService) { }

    /**
     * Busca todos os processo gera dossie dos macro processos
     */
    buscarProcesosGeraDossiePeloMacroProcesso(): Array<ProcessoPatriarca> {
        const processos: Array<ProcessoPatriarca> = new Array<ProcessoPatriarca>();
        const processoPatriarca: any = this.buscarProcessoPatriarcaCompleto();
        processoPatriarca.forEach(pp => {
            this.getProcessosFilhos(true, pp, processos);
        });
        return processos;
    }

    /**
     * Busca o processo gera dossie por id
     * @param idProcessoGeraDossie 
     */
    buscarProcessoGeraDossiePorId(idProcessoGeraDossie: number): ProcessoPatriarca {
        let processos: Array<ProcessoPatriarca> = this.buscarProcesosGeraDossiePeloMacroProcesso();
        return processos.length > 0 ? processos.find(processoGeraDossie => processoGeraDossie.id == idProcessoGeraDossie) : null;
    }

    /**
     * Busca todos os processo fase de um determinado processo gera dossie
     * @param id 
     */
    buscarProcessosFasePassandoIdProcessoGeraDossie(id: number): Array<ProcessoPatriarca> {
        const processos: Array<ProcessoPatriarca> = new Array<ProcessoPatriarca>();
        let fasesPorProcessoPai: any[];
        const processoPatriarca: any = this.buscarProcessoPatriarcaCompleto();
        patriarca: for (let i = 0; i < processoPatriarca.length; i++) {
            for (let x = 0; x < processoPatriarca[i].processos_filho.length; x++) {
                const fases: any[] = processoPatriarca[i].processos_filho.filter(p => p.id == id);
                fasesPorProcessoPai = fases.length > 0 ? fases[0].processos_filho : [];
                break patriarca;
            }
        }
        fasesPorProcessoPai.forEach(fase => {
            this.getProcessosFilhos(false, fase, processos);
        });
        return processos;
    }

    /**
     * Busca o processo fase pelo id
     * @param idProcesso 
     * @param idFase 
     */
    buscarProcessoFasePorId(idProcesso: number, idFase: number): ProcessoPatriarca {
        let processos: Array<ProcessoPatriarca> = this.buscarProcessosFasePassandoIdProcessoGeraDossie(idProcesso);
        return processos.length > 0 ? processos.find(processoFase => processoFase.id == idFase) : null;
    }

    /**
     * Busca todos os tipos de documentos da tipologia
     */
    buscarTiposDocumentosTipologia(): Array<TipoDocumentoTipologia> {
        const tiposDocumentos: Array<TipoDocumentoTipologia> = new Array<TipoDocumentoTipologia>();
        const tipologia: any = this.buscarTipologiaCompleta();
        tipologia.tipos_documento.forEach(td => {
            const tipoDocumento: TipoDocumentoTipologia = new TipoDocumentoTipologia();
            tipoDocumento.id = td.id;
            tipoDocumento.nome = td.nome;
            tiposDocumentos.push(tipoDocumento);
        });
        return tiposDocumentos;
    }

    /**
     * Busca o tipo documento por id
     * @param idTipoDocumento 
     */
    buscarTipoDocumentoPorId(idTipoDocumento: number): TipoDocumentoTipologia {
        let tipoDocumentos: Array<TipoDocumentoTipologia> = this.buscarTiposDocumentosTipologia();
        return tipoDocumentos.length > 0 ? tipoDocumentos.find(tipoDocumento => tipoDocumento.id == idTipoDocumento) : null;
    }

    /**
     * Busca todos os tipos de funcoes documentais da tipologia
     */
    buscarFuncoesDocumentaisTipologia(): Array<FuncaoDocumentalTipologia> {
        const funcoesDocumentais: Array<FuncaoDocumentalTipologia> = new Array<FuncaoDocumentalTipologia>();
        const tipologia: any = this.buscarTipologiaCompleta();
        tipologia.funcoes_documentais.forEach(fd => {
            const funcaoDocumentalTipologia: FuncaoDocumentalTipologia = new FuncaoDocumentalTipologia();
            funcaoDocumentalTipologia.id = fd.id;
            funcaoDocumentalTipologia.nome = fd.nome;
            funcoesDocumentais.push(funcaoDocumentalTipologia);
        });
        return funcoesDocumentais;
    }

    /**
     * Busca a funcao documental por id
     * @param idFuncaoDocumental 
     */
    buscarFuncaoDocumentalPorId(idFuncaoDocumental: number): FuncaoDocumentalTipologia {
        let funcoesDocumentais: Array<FuncaoDocumentalTipologia> = this.buscarFuncoesDocumentaisTipologia();
        return funcoesDocumentais.length > 0 ? funcoesDocumentais.find(funcaoDocumentaal => funcaoDocumentaal.id == idFuncaoDocumental) : null;
    }

    /**
     * Retorna os processos filhos: Podenso ser: Macro processo e Gera Dossie.
     * @param geraDossie 
     * @param obj 
     * @param processos 
     */
    private getProcessosFilhos(geraDossie: boolean, obj: any, processos: ProcessoPatriarca[]) {
        if (geraDossie) {
            obj.processos_filho.forEach(pf => {
                this.extrairTipoCompativelObjetoPatriarca(geraDossie, obj, pf, processos);
            });
        } else {
            this.extrairTipoCompativelObjetoPatriarca(geraDossie, obj, undefined, processos);
        }
    }

    /**
     * Verifica qual o objeto deve ser inserido la lista de retorno: geraDossie ou fase
     * @param geraDossie 
     * @param obj 
     * @param pf 
     * @param processos 
     */
    private extrairTipoCompativelObjetoPatriarca(geraDossie: boolean, obj: any, pf: any, processos: ProcessoPatriarca[]) {
        const processo: ProcessoPatriarca = new ProcessoPatriarca();
        if (geraDossie) {
            processo.id = pf.id;
            processo.nome = pf.nome;
            processo.unidade_autorizada = pf.unidade_autorizada;
        } else {
            processo.id = obj.id;
            processo.nome = obj.nome;
            processo.unidade_autorizada = obj.unidade_autorizada;
        }
        processos.push(processo);
    }

    /**
     * Busca todo processo patriarca no local storage
     */
    private buscarProcessoPatriarcaCompleto(): any {
        return JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoPatriarca));
    }

    /**
     * Busca toda a tipologia no local storage
     */
    private buscarTipologiaCompleta(): any {
        return JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoTipologia));
    }

    public buscarProcessoPorNome(nomeProcesso: string) : ProcessoPatriarca {
        const processos: Array<ProcessoPatriarca> = new Array<ProcessoPatriarca>();
        const processoPatriarca: any = this.buscarProcessoPatriarcaCompleto();
        processoPatriarca.forEach(pp => {
            this.getProcessosFilhos(true, pp, processos);
        });
        let processoFilho = processos.find((processo) => processo.nome == nomeProcesso);
        return processoFilho;       
    }

}