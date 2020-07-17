import { Documento } from './documento';
import { Atributos } from './atributos';
import { ConteudoDocumento } from './conteudo-documento';

export class ElementoConteudo {
    
    id?: number;
    obrigatorio?: boolean;
    exigencia_validacao?: boolean;
    identificador_elemento?: number;
    identificador_elemento_vinculador?: number;
    documento?: Documento;
    atributos?: Atributos[];
}