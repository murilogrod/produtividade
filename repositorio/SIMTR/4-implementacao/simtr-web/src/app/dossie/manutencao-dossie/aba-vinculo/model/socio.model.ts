export class Socio {
    nome_socio: string;
    codigo_qualificacao: string;
    descricao_qualificacao: string;
    cpf_cnpj: string;
    pc_capital_social: string;
    data_inicio: string;
    cpf_representante: string;
    nome_representante: string;
    vinculo_pendente: string;
    type_socio: string;
    relacionamentosPermitidos: string[] = new Array<string>();
    // true para pessoa juridica
    tipo_pessoa: boolean;
    isError: boolean;
    isErrorSocioExistente: boolean;
    msgError: string;
}