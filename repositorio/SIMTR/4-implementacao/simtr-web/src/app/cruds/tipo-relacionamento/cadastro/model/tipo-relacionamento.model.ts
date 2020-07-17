import { SelectTipoPessoa } from "../../../model/select-tipo-pessoa.interface";
import { GridTipoRelacionamento } from "../../pesquisa/model/grid-tipo-relacionamento";

export class TipoRelacionamentoModel {
    showHeader: boolean;
    configGridHeader: any[] = [
        { width: '30%', classIcon: 'fa fa-desktop fa-4x', labelTitle: 'ID', index: 0 },
        { width: '100%', classIcon: 'fa fa-clock-o fa-4x', labelTitle: 'DATA DA ÚLTIMA ATUALIZAÇÃO', index: 1 }
    ];
    controlError: boolean;
    editar: boolean;
    tipoPessoas: Array<SelectTipoPessoa>;
    selectedTipoPessoa: SelectTipoPessoa;
    gridTipoRelacionamentos: Array<GridTipoRelacionamento> = new Array<GridTipoRelacionamento>();
}