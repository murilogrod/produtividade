import { SelectItem } from "primeng/primeng";

export class DadosDeclaradosModel{
  resultado: boolean;
  formularioValido: boolean;
  listaItens: SelectItem[];
  opcoesSelecionadas: string[];
  dadosDeclarados: any;
  idDossie: number;
  idTipoDocumento: number;
}