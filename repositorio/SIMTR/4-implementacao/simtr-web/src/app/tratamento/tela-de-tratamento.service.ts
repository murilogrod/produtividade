import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable()
export class TelaDeTratamentoService {

    private _exibindoChecklistFase = new BehaviorSubject<boolean>(false);
    private _exibindoChecklistsPrevios = new BehaviorSubject<boolean>(true);

    constructor() { }

    /** Retorna true quando um checklist de fase está sendo exibido na tela de tratamento. */
    get exibindoChecklistFase() {
        return this._exibindoChecklistFase.asObservable();
    }

    /**
     * Define se é um checklist de fase que está sendo exibido na tela de tratamento.
     * @param exibindoChecklistFase Passe true para definir que um checklist de fase está sendo exibido.
     */
    setExibindoChecklistFase(exibindoChecklistFase: boolean) {
        this._exibindoChecklistFase.next(exibindoChecklistFase);
    }

    /** Retorna true quando os checklists prévios estão sendo tratados.
     * Somente após o tratamento e aprovação dos checklists prévios os demais itens poderão ser tratados. */
    get exibindoChecklistsPrevios() {
        return this._exibindoChecklistsPrevios.asObservable();
    }

    /**
     * Define se a tela de tratamento está no momento de verificação de checklists prévios.
     * @param exibindoChecklistsPrevios Forneça false quando o tratamento dos checklists prévios for
     * finalizado com sucesso.
     */
    setExibindoChecklistsPrevios(exibindoChecklistsPrevios: boolean) {
        this._exibindoChecklistsPrevios.next(exibindoChecklistsPrevios);
    }

}