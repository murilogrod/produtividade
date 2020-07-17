import { TipoRelacionamento } from "./tipo-relacionamento";

export class Processo {
    id?: number;
    nome?: string;
    tipo: string;
    cpfCnpj?:string;
    ativo?: boolean;
    ic_dossie?: boolean;
    selecionado?: string;
    possui_filhos?: string;
    styleClass?: string;
    ic_tipo_pessoa: string;
    processos_filho?: Processo[];
    tipos_relacionamentos?: TipoRelacionamento[];
}
