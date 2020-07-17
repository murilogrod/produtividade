import { Vinculo } from "./vinculo.model";

export class VinculoProcesso implements Vinculo {
    id?: number;
    nome?: string;
    tipo: string;
    ativo?: boolean;
    ic_dossie?: boolean;
    ic_tipo_pessoa: string;
    processos_filho?: VinculoProcesso[];
    instancias_documento: any;
}
