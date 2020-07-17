import { Injectable } from "@angular/core";
import { ApplicationService, AuthenticationService } from "../services";
import { LOCAL_STORAGE_CONSTANTS, PROPERTY, PERFIL_ACESSO } from "../constants/constants";
import { environment } from 'src/environments/environment';

@Injectable()
export class TratamentoValidarPermissaoService {

    constructor(private applicationService: ApplicationService,
        private authenticationService: AuthenticationService){}

    isPermissaoUnidadeDeTratamento(unidadesTratamentos: []){
        let permissaoOk: boolean = false;
    
        if(unidadesTratamentos && unidadesTratamentos.length > 0){
          let coUnidade = JSON.parse(this.applicationService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.userSSO))[PROPERTY.CO_UNIDADE];
          let lotacaoFisica = JSON.parse(this.applicationService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.userSSO))[PROPERTY.LOTACAO_FISICA];
          let coUnidadeNumber = coUnidade ? Number.parseInt(coUnidade) : null;
          let lotacaoFisicaNumber = lotacaoFisica ? Number.parseInt(lotacaoFisica) : null;
    
          for(let unidade of unidadesTratamentos){
    
            if(coUnidadeNumber != null && Number.parseInt(unidade) == coUnidadeNumber){
              permissaoOk = true;
              break;
            }
    
            if(lotacaoFisicaNumber != null && Number.parseInt(unidade) == lotacaoFisicaNumber){
              permissaoOk = true;
              break;
            }
          }
        }
    
        return permissaoOk;
    }

    /**
	 * Verifica se o usuário logado possui a permissao: MTRADM, MTRSDNTTO ou MTRSDNTTG.
	 */
	hasCredentialParaTratameto(): boolean {
		const credentials: string = `${PERFIL_ACESSO.MTRADM},${PERFIL_ACESSO.MTRSDNTTO},${PERFIL_ACESSO.MTRSDNTTG}`;
		if (!environment.production) {
			return this.applicationService.hasCredential(credentials, this.authenticationService.getRolesInLocalStorage());
		} else {
			return this.applicationService.hasCredential(credentials, this.authenticationService.getRolesSSO());
		}
	}

}