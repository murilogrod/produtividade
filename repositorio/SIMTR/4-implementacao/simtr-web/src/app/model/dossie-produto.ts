import { Produto } from './produto';
import { Documento } from './documento';
import { Processo, RespostaDossie, DocumentoGED } from '.';
import { VinculoGarantia } from './model-arvore-generica-dossie-produto/vinculos-model/vinculo-garantia';
import { VinculoCliente } from './model-arvore-generica-dossie-produto/vinculos-model/vinculo-cliente';
import { RespostaCampoFormulario } from './resposta-campo-formulario';
import { ElementoConteudo } from './elemento-conteudo';

export class DossieProduto {
    id_dossie_produto?: number;
    processo?: string;
    macroprocesso?: string;
    cgc_criacao?: string;
    cgc_priorizado?: string;
    matricula_priorizado?: string;
    peso_prioridade?: number;
    fase?: number;
    cliente?: VinculoCliente;
    situacao_data?: string;
    situacao_unidade?: number;
    matricula_situacao_atual?: string;
    produtos?: Produto[];
    garantias?: VinculoGarantia[];
    documentos_utilizados?: Documento[];
    pessoas?: VinculoCliente[];
    processo_objeto?: Processo;
    respostas?: RespostaDossie[];
    dossie_produto?: number;
    co_erro?: string;
    documentosGed?: DocumentoGED[];
    etapa: string;
    ic_item_rejeitado?: boolean;
    ic_suspeita_fraude?: boolean;
    // ------------------------------------------------------- NOVOS ------------------------------------------------------------ //
    processo_fase: any;
    processo_patriarca?: any;
    processo_dossie?: any;
    id?: number;
    unidade_criacao?: number;
    canal_caixa?: string;
    data_hora_finalizacao?: string;
    unidades_tratamento?: any[];
    situacao_atual?: string;
    data_hora_situacao?: string;
    historico_situacoes?: any[];
    instancias_documento?: any[];
    cancelamento?: boolean;
    situacaoComplementacao?: boolean;
    codigo_integracao?: number;
    rascunho?: boolean;
    processo_origem?: number;
    produtos_contratados?: any[];
    vinculos_pessoas?: any[];
    garantias_informada?: any[];
    respostas_formulario?: RespostaCampoFormulario[];
    indexAba?: number;
    camposValidado?: boolean;
    descricaoDocumento?:string;
    lstDescricaoDoc?: any[];
    listaDocumentosFalta?: any[];
    elementos_conteudos?: ElementoConteudo[];
    nCamposObrigatoirio?: boolean;
    finalizacao?: boolean;
    retorno?: boolean;
    justificativa?: string;
}