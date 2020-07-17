import { DadoDivergente } from "./dadoDivergente";
import { SelectItem } from "primeng/primeng";

export class AbaIdentificacao {

    ptBR: any;

    calendarProperties = {
        firstDayOfWeek: 1,
        dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
        dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'],
        dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
        monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
        monthNamesShort: ['Jan', 'Feb', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dec'],
        today: 'Hoje',
        clear: 'Limpar'
    };

    maxDate: Date;

    btnAtivoConsultaClienteSicli: boolean = false;

    dadosDivergentes: DadoDivergente[];

    sicliWasSearched = false;

    disableNIS = false;

    // Variáveis que habilita o CNPJ
    radioGlomerados: any[];

    radioGlomerado: boolean = false;

    // Variáveis que habilita o CPF
    inputExiste: boolean = false;

    tipoPortes: SelectItem[];

}