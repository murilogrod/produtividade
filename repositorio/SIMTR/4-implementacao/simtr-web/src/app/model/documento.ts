import { ConteudoDocumento } from './conteudo-documento';
import { FuncaoDocumental, TipoDocumento } from '.';
import { Atributos } from './atributos';
import { Conteudo } from './conteudos';

export class Documento {
    id?: number;
    data_hora_captura?: string;
    matricula_captura?: string;
    data_hora_validade?: string;
    dossie_digital?: Boolean;
    canal_captura?: string;
    ConteudoDocumentos?: ConteudoDocumento[];
    funcoes_documentais?: FuncaoDocumental[];
    hora_captura?: string;
    possui_instancia?: boolean;
    situacao?: string;
    nu_processo?: number;
    dossie_produto?: number;
    //Incluído devido a erro na classe app/utils/Utils.ts
    data_captura?: string;
    indiceDocListPdfOriginal?: number;
    tipo_documento?: TipoDocumento;
    origem_documento?: string;
    mime_type?: any;
    atributos?: Atributos[];
    conteudos?: Conteudo[];
}