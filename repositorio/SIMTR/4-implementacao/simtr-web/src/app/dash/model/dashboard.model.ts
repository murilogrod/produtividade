export class Dashboard {
    estat: any = [];
    resumo_situacao = [];
    totais = [];
    totaisProcesso = [];
    situacoes = [];
    listFalhaBPMTO = [];
    atributos = [];
    processos = [];
    qtdLista = 0;
    contadorLista = 0;
    atributosOriginal = [];
    dossiesProcesso = [];
    chart: any;
    indexSituacao = 0;
    dossies = [];
    indexProcesso = 0;
    eventoSituacao: string;
    eventoProcesso: string;
    listaSituacao: any[] = [];
    listaProcesso: any[] = [];
    config = [
        { status: 'Aguardando Tratamento', icone: 'fa-clock-o', cor: 'bg-green' },
        { status: 'Finalizado Inconforme', icone: 'fa-thumbs-o-down', cor: 'bg-purple' },
        { status: 'Alimentação Finalizada', icone: 'fa-reply', cor: 'bg-yellow' },
        { status: 'Aguardando Complementação', icone: 'fa-minus-square', cor: 'bg-green' },
        { status: 'Finalizado Conforme', icone: 'fa-thumbs-o-up', cor: 'bg-green' },
        { status: 'Em Complementação', icone: 'fa-plus-square', cor: 'bg-red' },
        { status: 'Pendente Segurança', icone: 'fa-unlock-alt', cor: 'bg-teal' },
        { status: 'Cancelado', icone: 'fa-file-excel-o', cor: 'bg-purple' },
        { status: 'Aguardando Alimentação', icone: 'fa-clone', cor: 'bg-green' },
        { status: 'Em Tratamento', icone: 'fa-wrench', cor: 'bg-red' },
        { status: 'Análise Segurança', icone: 'fa-lock', cor: 'bg-red' },
        { status: 'Finalizado', icone: 'fa-calendar-times-o', cor: 'bg-green' },
        { status: 'Conforme', icone: 'fa-check-square-o', cor: 'bg-yellow' },
        { status: 'Segurança Finalizado', icone: 'fa-shield', cor: 'bg-yellow' },
        { status: 'Em Alimentação', icone: 'fa-database', cor: 'bg-red' },
        { status: 'Rascunho', icone: 'fa-pencil', cor: 'bg-red' },
        { status: 'Pendente Informação', icone: 'fa-star-half-empty', cor: 'bg-blue' },
        { status: 'Criado', icone: 'fa-calendar-check-o', cor: 'bg-yellow' }
    ];

    configProcessos = [
        { status: 'CONCESSÃO CRÉDITO COMERCIAL', icone: 'fa-clock-o', cor: '#00c0ef' },
        { status: 'CONCESSÃO CRÉDITO HABITACIONAL', icone: 'fa-thumbs-o-down', cor: '#00a65a' },
        { status: 'PESSOA JURÍDICA', icone: 'fa-reply', cor: '#F39C12' },
        { status: 'PESSOA FÍSICA', icone: 'fa-thumbs-o-up', cor: '#3D9970' },
        { status: 'CONTA CORRENTE', icone: 'fa-unlock-alt', cor: '#605CA8' },
        { status: 'FINANCIAMENTO DE VEÍCULOS', icone: 'fa-file-excel-o', cor: '#f56954' },
        { status: 'PAGAMENTO DE LOTERIAS', icone: 'fa-clone', cor: '#39CCCC' },
        { status: 'CRÉDITO RURAL', icone: 'fa-laptop', cor: '#D81B60' },
        { status: 'AVALIAÇÃO DO TOMADOR', icone: 'fa-lock', cor: '#00c0ef' },
        { status: 'AVALIAÇÃO DA OPERAÇÃO', icone: 'fa-calendar-times-o', cor: '#00a65a' },
        { status: 'VERIFICAÇÃO REGIME DE ALÇADA', icone: 'fa-check-square-o', cor: '#F39C12' },
        { status: 'CONFORMIDADE CONCESSÃO HABITACIONAL', icone: 'fa-shield', cor: '#3D9970' },
        { status: 'CONFORMIDADE CONCESSÃO COMERCIAL', icone: 'fa-database', cor: '#605CA8' },
        { status: 'CONFORMIDADE GARANTIA REATIVA', icone: 'fa-newspaper-o', cor: '#f56954' },
        { status: 'LIBERAÇÃO DE CRÉDITO', icone: 'fa-star-half-empty', cor: '#39CCCC' },
        { status: 'AVALIAÇÃO TOMADOR - RESULTADO', icone: 'fa-calendar-check-o', cor: '#D81B60' },
        { status: 'AVALIACAO TOMADOR - DADOS', icone: 'fa-clock-o', cor: '#00c0ef' },
        { status: 'DOSSIE DIGITAL', icone: 'fa-thumbs-o-down', cor: '#00a65a' },
        { status: 'ATUALIZAÇÃO CADASTRAL', icone: 'fa-reply', cor: '#F39C12' },
        { status: 'SUBMISSÃO DE DOCUMENTOS', icone: 'fa-thumbs-o-up', cor: '#3D9970' },
        { status: 'DOSSIE DIGITAL - CONTRATACAO', icone: 'fa-unlock-alt', cor: '#605CA8' },
        { status: 'CADASTRO SICLI', icone: 'fa-file-excel-o', cor: '#f56954' },

    ];
    collapse: boolean;
    collapseAll: boolean;
    all: boolean;
    emptyResumoProcesso: boolean = false;
}