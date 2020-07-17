import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { LoaderService } from './loader.service';
import { LoaderState } from './loader';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'cx-loader',
    templateUrl: 'loader.component.html',
    styleUrls: ['loader.component.css']
})
export class LoaderComponent implements OnInit, OnDestroy {

    show = false;
    closeLoading: boolean;
    progressShow: boolean;
    progress: number;
    timeClosed;

    private subscription: Subscription;

    constructor(
        private loaderService: LoaderService
    ) { }

    ngOnInit() {
        this.closeLoading = false;
        this.subscription = this.loaderService.loaderState.subscribe((state: LoaderState) => {
            this.closeLoading = false;
            if(!this.show) {
                this.timeClosed =  setTimeout(() => {
                    this.closeLoading = this.show;
                }, environment.timeClosed);
            }
            this.show = state.show;
            if(state.total > 0) {
                this.progressLoading(state);
            }else {
                this.progressShow = false;
            }
        },
        () => {
            this.loaderService.hide();
        });
    }

    progressLoading(state: LoaderState) {
        this.progressShow = true;
        this.progress = Math.trunc((100*state.posicao)/state.total);
    }

    ngOnDestroy() {
        this.inicializarParametrosLoading();
        this.subscription.unsubscribe();
    }

    fecharLoading() {
        clearTimeout(this.timeClosed);
        this.inicializarParametrosLoading();
        this.loaderService.hide()
    }

    private inicializarParametrosLoading() {
        this.closeLoading = false;
        this.progressShow = false;
    }
}