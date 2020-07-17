import { PaeContratacaoPatch } from './pae-contratacao-patch';

export class PaeContratacao extends PaeContratacaoPatch {
    id?: string = null;
    numero_processo?: string  = null;
    ano_processo?: string  = null;
    data_hora_inclusao?: string  = null;
    matricula_inclusao? = 'c999999';
    documentos?: any[];
    contratos?: any[];
    apensos?: any[];

}