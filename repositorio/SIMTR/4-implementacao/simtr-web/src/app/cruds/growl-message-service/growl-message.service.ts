import { MessageService } from 'primeng/components/common/messageservice';
import { Injectable } from '@angular/core';
import { GrowlMessage } from '../model/growl-message';

@Injectable({
    providedIn: 'root'
})
export class GrowlMessageService {

    constructor(private messageService: MessageService) { }

    showSuccess(summary: string, detail: string) {
        this.messageService.add(this.getSucessGrowlMessage(summary, detail));
    }

    showInfo(summary: string, detail: string) {
        this.messageService.add(this.getInfoGrowlMessage(summary, detail));
    }

    showWarn(summary: string, detail: string) {
        this.messageService.add(this.getWarnGrowlMessage(summary, detail));
    }


    showError(summary: string, detail: string) {
        this.messageService.add(this.getErrorGrowlMessage(summary, detail));
    }

    private getSucessGrowlMessage(summary: string, detail: string): GrowlMessage {
        const growlMessage: GrowlMessage = new GrowlMessage();
        growlMessage.severity = "success";
        growlMessage.summary = summary;
        growlMessage.detail = detail;
        return growlMessage;
    }

    private getInfoGrowlMessage(summary: string, detail: string): GrowlMessage {
        const growlMessage: GrowlMessage = new GrowlMessage();
        growlMessage.severity = "info";
        growlMessage.summary = summary;
        growlMessage.detail = detail;
        return growlMessage;
    }

    private getWarnGrowlMessage(summary: string, detail: string): GrowlMessage {
        const growlMessage: GrowlMessage = new GrowlMessage();
        growlMessage.severity = "warn";
        growlMessage.summary = summary;
        growlMessage.detail = detail;
        return growlMessage;
    }

    private getErrorGrowlMessage(summary: string, detail: string): GrowlMessage {
        const growlMessage: GrowlMessage = new GrowlMessage();
        growlMessage.severity = "error";
        growlMessage.summary = summary;
        growlMessage.detail = detail;
        return growlMessage;
    }

}
