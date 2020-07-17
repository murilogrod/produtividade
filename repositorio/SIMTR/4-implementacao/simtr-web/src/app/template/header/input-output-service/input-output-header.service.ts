import { Output, EventEmitter } from "@angular/core";

export abstract class InputOutputHeaderService {
    @Output() onChangeRoleChanged = new EventEmitter<any[]>();
    @Output() toogleSideBarEvent: EventEmitter<boolean> = new EventEmitter();
}