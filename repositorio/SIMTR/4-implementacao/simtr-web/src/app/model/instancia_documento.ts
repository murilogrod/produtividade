import { Documento } from "./documento";

export class InstanciaDocumento{
    data_hora_situacao_atual?: string;
    data_hora_vinculacao?: string
    documetno?: Documento;
    historico_situacoes?: any[];
    id?: Number;
    id_dossie_cliente_produto?: Number;
    id_elemento_conteudo?: Number;
    id_garantia_informada?: Number;
    situacao_atual?: string
}