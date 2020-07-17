import { VinculoCliente } from "./model-arvore-generica-dossie-produto/vinculos-model/vinculo-cliente";

export class Pessoa {
    cliente?: VinculoCliente;
    cpf?: string;
    nome?: string;
    tipo_relacionamento?: string;
}