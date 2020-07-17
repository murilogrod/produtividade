import { AlertMessageService, ApplicationService } from "src/app/services";
import { ViewEncapsulation, Component, OnInit, Input } from "@angular/core";
import { RespostaCampoFormulario } from "src/app/model/resposta-campo-formulario";
import { LOCAL_STORAGE_CONSTANTS, UNDESCOR, TIPO_DOCUMENTO } from 'src/app/constants/constants';
import { Utils } from "src/app/utils/Utils";


@Component({
    selector: 'formulario-resposta-generico',
    templateUrl: './formulario-resposta-generico.html',
    styleUrls: ['./formulario-resposta-generico.css'],
    encapsulation: ViewEncapsulation.None
})
export class FormularioRespostaGenericoComponent extends AlertMessageService implements OnInit {

    @Input() respostasCampoFormulario: RespostaCampoFormulario[];
    @Input() titulo: string;
    @Input() dossieProduto: any;
    refDossiePatriarca: any;
    camposFormatados: RespostaCampoFormulario[] = [];
    refFaseDossie: any;

    constructor(private appService: ApplicationService){
        super();
    }

    ngOnInit() {
        this.refDossiePatriarca = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + this.dossieProduto.processo_dossie.id));

        if(this.respostasCampoFormulario && this.respostasCampoFormulario.length > 0){

            for(let campo of this.respostasCampoFormulario){
    
              if(campo.tipo_campo === TIPO_DOCUMENTO.TEXT || campo.tipo_campo === TIPO_DOCUMENTO.CEP){
                 campo.resposta_aberta = this.mascaraDinamicaformulario(campo);
    
              }else if(campo.tipo_campo === TIPO_DOCUMENTO.EMAIL){
                campo.resposta_aberta = this.verificarValorVazio(campo);
    
              }else if(campo.tipo_campo === TIPO_DOCUMENTO.DATE){
                campo.resposta_aberta = this.verificarValorVazio(campo);

              }else if(campo.tipo_campo === TIPO_DOCUMENTO.CONTA_CAIXA){
                campo.resposta_aberta = Utils.completarContaCaixaComZeroEsquerda(campo.resposta_aberta);
                
              }
              
              this.camposFormatados.push(campo);
            }
          }
    }

    mascaraDinamicaformulario(campoFormulario: any) {
        let descricao = campoFormulario.resposta_aberta;
        let mascaraCep = "99.999-999"
        
        for(let fase of this.refDossiePatriarca.processos_filho) {
          if(this.dossieProduto.processo_fase.id == fase.id) {
            this.refFaseDossie = fase;
            break;
          }
        }
  
        for(let formulario of this.refFaseDossie.campos_formulario) {
          if(campoFormulario && formulario.id == campoFormulario.id_campo_formulario) {
            
            if(formulario.tipo_campo == 'CEP' && !formulario.mascara_campo){
                formulario.mascara_campo = mascaraCep;
            }

            if(formulario.mascara_campo) { 
              descricao = this.montarMascaraDinamica(formulario.mascara_campo, campoFormulario);
              break;          
            } 
            if(undefined == campoFormulario.resposta_aberta) {
              descricao = "";
              break;
            }
            descricao = campoFormulario.resposta_aberta;
          }
        }
  
        return descricao;  
      }
  
    private montarMascaraDinamica(mascara: any, campoFormulario: any) {
      let descricaoMontada = "";
      let posicaoInicial = 0;
      let posicaoFinal = 1;
      for (let i = 0; i <= mascara.length; i++) {
        let char = mascara.charAt(i);
        let regex = /[-!$%^&*()_+|~=`{}\[\]:\/;<>?,.@#]/g;
        let campoAMontarMascara = Utils.removerTodosCaracteresEspeciais(campoFormulario.resposta_aberta);
        let charCampo = campoAMontarMascara.substring(posicaoInicial, posicaoFinal);
        posicaoInicial = posicaoInicial + 1;
        posicaoFinal = posicaoFinal + 1;
        if (!regex.test(char)) {
          if(charCampo) {
            descricaoMontada = descricaoMontada + charCampo;
          }
        }
        else {
          descricaoMontada = descricaoMontada + char + charCampo;
        }
      }
      return descricaoMontada;
    }

    verificarValorVazio(formulario: any) {
        if(undefined == formulario.resposta_aberta) {
          return "Não preenchido";
        }
        return formulario.resposta_aberta;
    }
}