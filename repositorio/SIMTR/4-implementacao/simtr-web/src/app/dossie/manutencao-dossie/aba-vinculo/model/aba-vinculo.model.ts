import { VinculoProduto, VinculoCliente, VinculoGarantia } from "src/app/model";
import { VinculoArvore } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { EventEmitter } from "@angular/core";
import { Socio } from "./socio.model";

export class AbaVinculo {
    garantias: any;
    listaVinculoArvore: Array<VinculoArvore>
    produtoLista: VinculoProduto[]
    processoEscolhido: any
    idProcessoFase: any
    produtosVinculados: any[]
    produtoListaChanged: EventEmitter<VinculoProduto[]>
    listaVinculoArvoreChanged: EventEmitter<Array<VinculoArvore>>
    clienteLista: any;
    novosVinculos: Array<VinculoCliente>;
    cliente: VinculoCliente;
    clienteListaChanged: EventEmitter<VinculoCliente[]>
    primeiroVinculoPessoa: EventEmitter<number>
    qtdVincPessoa: number = 0;
    exibeLoadArvoreChanged: EventEmitter<boolean>
    cnpj: string;
    icTipoPessoa: any;
    listaTipoRelacionamento: any[];
    garantiasChanged: EventEmitter<VinculoGarantia[]>
    showGarantiaProduto: boolean = false;
    produtoGarantiaLista: VinculoProduto[] = new Array<VinculoProduto>();
    sociosReceitaFederal: Array<Socio>;
    selectedSociosReceitaFederal: Array<Socio> = [];
    sociofieldsErrors: Set<string>;
}