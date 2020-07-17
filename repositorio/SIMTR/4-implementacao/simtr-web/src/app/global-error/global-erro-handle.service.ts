import { Injectable, ErrorHandler } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { GLOBAL_ERROR } from './utils/error-contants';
import { GlobalError } from './model/global-error';
import { ApplicationService } from '../services';
import { UtilsErrorGlobal } from './utils/utils-erros';
import { AnalyticsService } from '../services/analytics/analytics.service';
import { GlobalErrorComponentPresenter } from './presenter/global-error.component.presenter';

@Injectable()
export class GlobalErrorHandlerService implements ErrorHandler {

    constructor(private globalErrorPresenter: GlobalErrorComponentPresenter, private appService: ApplicationService) {}
    
    handleError(error: any) {
        this.globalErrorPresenter.montarListaDeErros(error, this.appService);  
    }
}