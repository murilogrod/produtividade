import { TipoAdministracaoDossie } from "./tipo-administracao-dossie.enum";

export interface AdministrarDossieOutput {
    razaoInclusaoSituacao: string;
    tipoAdministracaoDossie: TipoAdministracaoDossie;
}