import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable()
export class AbrirDocumentoPopupDefaultService{

    existeCheckList: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(null);
    showPopup: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(null);
    executouUmaUnicaVez: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(null);
    gatilhoAbrirPopup: BehaviorSubject<string> = new BehaviorSubject<string>(null);

    get $getExisteCheckList(){
        return this.existeCheckList.asObservable();
    }

    setExisteCheckList(existeCheckList: boolean){
        this.existeCheckList.next(existeCheckList);
    }

    get $getShowPopup(){
        return this.showPopup.asObservable();
    }
    
    setShowPopup(showPopup: boolean){
        this.showPopup.next(showPopup);
    }

    get $getExecutouUmaUnicaVez(){
        return this.executouUmaUnicaVez.asObservable();
    }
    
    setExecutouUmaUnicaVez(executouUmaUnicaVez: boolean){
        this.executouUmaUnicaVez.next(executouUmaUnicaVez);
    }

    get $getGatilhoAbrirPopup(){
        return this.gatilhoAbrirPopup.asObservable();
    }
    
    setGatilhoAbrirPopup(gatilhoAbrirPopup: string){
        this.gatilhoAbrirPopup.next(gatilhoAbrirPopup);
    }
}