import { Socio } from "../../model/socio.model";

export interface SocioReceitaFederalOutput {
    cnpj: string;
    selectedSociosReceitaFederal: Array<Socio>;
    listaTipoRelacionamento: any[];
}