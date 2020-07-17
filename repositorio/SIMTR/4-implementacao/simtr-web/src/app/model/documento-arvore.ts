import { ConteudoDocumento } from './conteudo-documento';
import { FuncaoDocumental, TipoDocumento } from '.';
import { Atributos } from './atributos';
import { TipoDocumentoArvoreGenerica } from './model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model';
import { ConteudoDocumentoArvore } from './conteudo-documento-arvore';
import { FuncaoDocumentalArvoreGenerica } from './model-arvore-generica-dossie-produto/funcao-documental-arvore-generica';

export class DocumentoArvore {
    id?: number;
    data_hora_captura?: string;
    matricula_captura?: string;
    data_hora_validade?: string;
    dossie_digital?: boolean;
    canal_captura?: string;
    conteudoDocumentos?: ConteudoDocumentoArvore[];
    funcoes_documentais?: FuncaoDocumentalArvoreGenerica[];
    hora_captura?: string;
    possui_instancia?: boolean;
    situacaoAtual: string;
    docReutilizado: boolean;
    nu_processo?: number;
    dossie_produto?: number;
    //Incluído devido a erro na classe app/utils/Utils.ts
    data_captura?: string;
    documento?: DocumentoArvore;  
    tipo_documento?: TipoDocumentoArvoreGenerica;
    origem_documento?: string;
    mime_type?: any[];
    atributos?: Atributos[];
    binario?: string;
    quantidade_conteudos?: number;
}