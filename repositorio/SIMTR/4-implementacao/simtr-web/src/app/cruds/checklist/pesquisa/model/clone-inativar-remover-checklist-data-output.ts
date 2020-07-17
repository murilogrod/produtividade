import { Checklist } from "src/app/cruds/model/checklist.model";

export class CloneInativarRemoverChecklistDataOutput {
    id: number;
    checklist: Checklist;
    inativarChecklist: boolean;
    removerChecklist: boolean;
    cloneChecklist: boolean;
}