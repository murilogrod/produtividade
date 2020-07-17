import { SelectItem } from "primeng/primeng";

export class AbaVerificacao {
    verificacaoCarregada: boolean = true;
    btnOcultadoConformeCampoMostrar: boolean = false;
    filtroFase: string;
    filtroSituacao: string;
    listaAbaVerificacaoOriginal: any[];
    filtroGlobal: string;
    listaFase: any[];
    listaSituacao: SelectItem[];
    expander: boolean = false;
    contadorPosicacao?: number = 1;
    configSelectnconforme: any[] = [
        { label: 'Exibir Todas as situações', value: '' },
        { label: 'APROVADOS', value: 'AP', tooltip: 'DOCUMENTOS/CHECKLIST da fase APROVADOS' },
        { label: 'APROVADOS com OBSERVAÇÃO', value: 'APO', tooltip: 'DOCUMENTOS/CHECKLIST da fase APROVADOS com OBSERVAÇÃO' },
        { label: 'PENDENTES DE REGULARIZAÇÃO', value: 'PER', tooltip: 'DOCUMENTOS/CHECKLIST da fase PENDENTES DE REGULARIZAÇÃO' }
    ];
}