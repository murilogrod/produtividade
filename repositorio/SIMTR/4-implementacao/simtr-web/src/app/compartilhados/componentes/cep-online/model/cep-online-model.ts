import { TipoLogradouroModel } from "./tipo-logradouro-model";
import { LocalidadeModel } from "./localidade-model";

export class CepOnlineModel {
    cep?: string;
    localidade?: LocalidadeModel;
    codigoLogradouro?: number;
    logradouro?: string;
    tipoLogradouro?: TipoLogradouroModel;
    complemento?: string;
    limiteInferior?: string;
    limiteSuperior?: string;
    codigo?: number;
    bairro?: string;
    siglaUf?: string;
    codigoTreco?: number;
}

