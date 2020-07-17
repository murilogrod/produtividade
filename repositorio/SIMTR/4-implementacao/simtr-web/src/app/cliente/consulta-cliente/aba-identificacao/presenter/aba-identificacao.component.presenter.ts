import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { ConsultaClienteDisplay } from '../display/consulta-cliente.component.display';
import { DialogService } from 'angularx-bootstrap-modal';
import { ModalUserSSOComponent } from '../modal/modal-user-sso/view/modal-user-sso.component';
import { AbaIdentificacaoComponent } from '../view/aba-identificacao.component';
import { AbaIdentificacao } from '../model/aba-identificacao.model';
import { ConsultaClienteService } from '../../service/consulta-cliente-service';
import { LoaderService } from 'src/app/services';
import { VinculoCliente } from 'src/app/model';
import { UtilsAbaIdentificacao } from '../utils/UtilsAbaIdentificacao';
import { MudancaSalvaService } from 'src/app/services/mudanca-salva.service';
import { Utils } from 'src/app/utils/Utils';
import { ModalSicliComponent } from '../modal/modal-sicli/modal-sicli.component';
import { UtilsCliente } from '../../utils/utils-client';
declare var $: any;

@Injectable()
export class AbaIdentificadorComponentPresenter {

  abaIdentificacao: AbaIdentificacao;

  constructor(private router: Router,
    private clienteService: ConsultaClienteService,
    private mudancaSalvaService: MudancaSalvaService,
    private loadService: LoaderService,
    private dialogService: DialogService) { }

  /**
   * Verifica se o ambiente é produção
   */
  isEnviromentProduction(): boolean {
    if (environment.production) {
      return false;
    } else {
      return true;
    }
  }

  onSucessConsultaDossieCliente(consultaClienteDisplay: ConsultaClienteDisplay, chamarUrl: boolean, response: any): void {

    if (chamarUrl) {
      this.router.navigate(['principal', consultaClienteDisplay.cpfCnpj]);
    } else {
      consultaClienteDisplay.searchDone = true;
      consultaClienteDisplay.habilitaNovoProduto = true;
      consultaClienteDisplay.isResponseCliente = true;
      if (response.dossies_produto != null && response.dossies_produto.length > 0) {
        consultaClienteDisplay.isResponseCliente = false;
        consultaClienteDisplay.isListaProdutos = true;
      } else {
        consultaClienteDisplay.isListaProdutos = false;
      }
      this.loadCliente(consultaClienteDisplay);
    }
  }

  private loadCliente(consultaClienteDisplay: ConsultaClienteDisplay) {
    if (consultaClienteDisplay.vinculoCliente != null) {
      consultaClienteDisplay.userFound = true;
      consultaClienteDisplay.searching = true;
      // Variável que habilita os campos para edição.
      consultaClienteDisplay.updateClienteForm = true;
    }
  }

  isCPF(cpfCnpj: string) {
    return cpfCnpj.length === 14;
  }

  private atualizaCliente(abaIdentificacaoComponent: AbaIdentificacaoComponent, cliente: VinculoCliente) {
    abaIdentificacaoComponent.cliente = cliente;
    abaIdentificacaoComponent.cliente.porte = cliente.sigla_porte;
    abaIdentificacaoComponent.onClienteInicado.emit(cliente);
  }

  private criaNovoCliente(abaIdentificacaoComponent: AbaIdentificacaoComponent) {
    this.clienteService.insertCliente(abaIdentificacaoComponent.cliente).subscribe(
      response => {
        this.atualizaCliente(abaIdentificacaoComponent, <VinculoCliente>response);
        let messagesSuccess = new Array<string>();
        messagesSuccess.push('Dossiê de cliente armazenado com sucesso.');
        abaIdentificacaoComponent.alertMessagesSucessChanged.emit(messagesSuccess);
        abaIdentificacaoComponent.inserting = false;
        abaIdentificacaoComponent.updateClienteForm = true;
        abaIdentificacaoComponent.afterInsertUser.emit(true);
        this.router.navigateByUrl('/principal', { skipLocationChange: true }).then(() =>
          this.router.navigate(['principal', Utils.removerTodosCaracteresEspeciais(abaIdentificacaoComponent.cpfCnpj)])
        );
      }, error => {
        let messagesError = new Array<string>();
        if (error.status == 500) {
          messagesError.push('Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.');
          abaIdentificacaoComponent.alertMessagesErrorChanged.emit(messagesError);
        } else if (error.status == 409) {
          messagesError.push('Falha ao persistir o dossiê de produto por inconsistência de parâmetros.');
          abaIdentificacaoComponent.alertMessagesErrorChanged.emit(messagesError);
        } else if (error.status == 400) {
          messagesError.push('Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.');
          abaIdentificacaoComponent.alertMessagesErrorChanged.emit(messagesError);
        }
        this.loadService.hide();
        throw error;
      });
  }

  /**
   * Método responsável por inserir um novo usuário no sistema
   */
  onInsertUser(abaIdentificacaoComponent: AbaIdentificacaoComponent) {
    this.mudancaSalvaService.setIsMudancaSalva(true);
    if (this.isCPF(abaIdentificacaoComponent.cpfCnpj)) {
      abaIdentificacaoComponent.cliente.data_nascimento = Utils.checkDate(abaIdentificacaoComponent.cliente.data_nascimento) && abaIdentificacaoComponent.inserting ? Utils.transformPrimeNGDateToInsert(abaIdentificacaoComponent.cliente.data_nascimento) : abaIdentificacaoComponent.cliente.data_nascimento;
    } else {
      abaIdentificacaoComponent.cliente.conglomerado = this.abaIdentificacao.radioGlomerado;
      abaIdentificacaoComponent.cliente.data_fundacao = Utils.checkDate(abaIdentificacaoComponent.cliente.data_fundacao) && abaIdentificacaoComponent.inserting ? Utils.transformPrimeNGDateToInsert(abaIdentificacaoComponent.cliente.data_fundacao) : abaIdentificacaoComponent.cliente.data_fundacao;
      abaIdentificacaoComponent.cliente.nome = abaIdentificacaoComponent.cliente.razao_social;
    }
    if (abaIdentificacaoComponent.inserting) {
      this.criaNovoCliente(abaIdentificacaoComponent);
    } else if (abaIdentificacaoComponent.updateClienteForm && abaIdentificacaoComponent.cliente.id) {
      this.atualizaClienteExistente(false, abaIdentificacaoComponent);
    } else {
      this.criaNovoCliente(abaIdentificacaoComponent);
    }
  }

  private convertObjectCliente(cliente: VinculoCliente, cpfCnpj: string): VinculoCliente {
    let clientePatch: VinculoCliente = new VinculoCliente();
    if (this.isCPF(cpfCnpj)) {
      clientePatch.nome = cliente.nome;
      clientePatch.email = cliente.email;
      clientePatch.nome_mae = cliente.nome_mae;
      clientePatch.data_nascimento = UtilsAbaIdentificacao.transformDateToPrimeNG(cliente.data_nascimento);
    } else {
      clientePatch.nome = cliente.nome;
      clientePatch.email = cliente.email;
      clientePatch.razao_social = cliente.razao_social;
      clientePatch.data_fundacao = UtilsAbaIdentificacao.transformDateToPrimeNG(cliente.data_fundacao);
      clientePatch.conglomerado = this.abaIdentificacao.radioGlomerado;
      clientePatch.sigla_porte = cliente.porte;
    }
    clientePatch.tipo_pessoa = cliente.tipo_pessoa;
    return clientePatch;
  }


  private atualizaClienteExistente(changeEmailUserSSO: boolean, abaIdentificacaoComponent: AbaIdentificacaoComponent) {
    let cliente = this.convertObjectCliente(abaIdentificacaoComponent.cliente, abaIdentificacaoComponent.cpfCnpj);
    this.clienteService.updateCliente(abaIdentificacaoComponent.cliente.id, cliente).subscribe(
      () => {
        if (!changeEmailUserSSO) {
          let messagesSuccess = new Array<string>();
          messagesSuccess.push('Dossiê de cliente atualizado com sucesso.');
          abaIdentificacaoComponent.alertMessagesSucessChanged.emit(messagesSuccess);
        }
      }, error => {
        let messagesError = new Array<string>();
        if (error.status == 400) {
          messagesError.push('Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.');
          abaIdentificacaoComponent.alertMessagesErrorChanged.emit(messagesError);
        } else if (error.status == 500) {
          messagesError.push('Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.');
          abaIdentificacaoComponent.alertMessagesErrorChanged.emit(messagesError);
        }
        this.loadService.hide();
        throw error;
      });
  }

  /**
   * Método responsável por mostrar os dados do cliente basedo no SICLI
   */
  showModalSICLI(abaIdentificacaoComponent: AbaIdentificacaoComponent) {
    this.loadService.show();
    this.abaIdentificacao.btnAtivoConsultaClienteSicli = true;
    this.clienteService.getClienteSicliUnico(Utils.removerTodosCaracteresEspeciais(abaIdentificacaoComponent.cpfCnpj), this.isCPF(abaIdentificacaoComponent.cpfCnpj)).subscribe(response => {
      this.dialogService.addDialog(ModalSicliComponent, {
        sicliUser: response,
        cpf: this.isCPF(abaIdentificacaoComponent.cpfCnpj),
        cpfCnpj: Utils.removerTodosCaracteresEspeciais(abaIdentificacaoComponent.cpfCnpj)

      });
      this.loadService.hide();
      this.abaIdentificacao.btnAtivoConsultaClienteSicli = false;
    }, error => {
      let messagesError = new Array<string>();
      if (error.status == 404 && this.isCPF(abaIdentificacaoComponent.cpfCnpj)) {
        messagesError.push('Cliente solicitado não foi localizado no SICLI com o CPF informado.');
        abaIdentificacaoComponent.alertMessagesErrorChanged.emit(messagesError);
      } else if (error.status == 404 && !this.isCPF(abaIdentificacaoComponent.cpfCnpj)) {
        messagesError.push('Cliente solicitado não foi localizado no SICLI com o CNPJ informado.');
        abaIdentificacaoComponent.alertMessagesErrorChanged.emit(messagesError);
      } else if (error.status == 500) {
        messagesError.push('Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.');
        abaIdentificacaoComponent.alertMessagesErrorChanged.emit(messagesError);
      } else if (error.status == 503 && this.isCPF(abaIdentificacaoComponent.cpfCnpj)) {
        messagesError.push('Falha ao consumir serviço do SICLI com o CPF informado.');
        abaIdentificacaoComponent.alertMessagesErrorChanged.emit(messagesError);
      } else if (error.status == 503 && !this.isCPF(abaIdentificacaoComponent.cpfCnpj)) {
        messagesError.push('Falha ao consumir serviço do SICLI com o CNPJ informado.');
        abaIdentificacaoComponent.alertMessagesErrorChanged.emit(messagesError);
      }
      this.abaIdentificacao.btnAtivoConsultaClienteSicli = false;
      this.loadService.hide();
      throw error;
    });
  }

  applyCSSRadios() {
    $('input[type="radio"]').iCheck({
      checkboxClass: 'icheckbox_square-blue',
      radioClass: 'iradio_square-blue'
    });
    $('input[type="radio"]').on('ifChecked', event => {
      let valor: boolean = (event.target.value == "true");
      this.abaIdentificacao.radioGlomerado = this.abaIdentificacao.radioGlomerados.find(radio => radio.id == valor).id;
    });
  }

  setMaxLengthInput() {
    if (!this.abaIdentificacao.inputExiste) {
      this.abaIdentificacao.inputExiste = $("input").hasClass('ui-inputtext');
      $("input.ui-inputtext").attr("maxlength", 10);
    }
  }


  /**
   * Transforma a data do cliente para mostrar na tela
   */
  private transformDateValueToShow(abaIdentificacaoComponent: AbaIdentificacaoComponent) {
    if (this.isCPF(abaIdentificacaoComponent.cpfCnpj)) {
      abaIdentificacaoComponent.cliente.data_nascimento = UtilsAbaIdentificacao.transformDateToPrimeNG(abaIdentificacaoComponent.cliente.data_nascimento);
    } else {
      abaIdentificacaoComponent.cliente.data_fundacao = UtilsAbaIdentificacao.transformDateToPrimeNG(abaIdentificacaoComponent.cliente.data_fundacao);
    }
  }
  setMudancaSalva() {
    this.mudancaSalvaService.setIsMudancaSalva(false);
  }

  inicializaDadosAbaIdentificacao(abaIdentificacaoComponent: AbaIdentificacaoComponent) {
    this.abaIdentificacao.radioGlomerados = [
      { id: true, description: 'SIM', ativo: false },
      { id: false, description: 'NÃO', ativo: true }
    ];
    this.abaIdentificacao.sicliWasSearched = false;
    this.abaIdentificacao.disableNIS = false;
    if (abaIdentificacaoComponent.inserting) {
      abaIdentificacaoComponent.cliente = new VinculoCliente();
      if (this.isCPF(abaIdentificacaoComponent.cpfCnpj)) {
        abaIdentificacaoComponent.cliente.cpf = Utils.removerTodosCaracteresEspeciais(abaIdentificacaoComponent.cpfCnpj);
        abaIdentificacaoComponent.cliente.tipo_pessoa = 'F';
      }
      else {
        abaIdentificacaoComponent.cliente.cnpj = Utils.removerTodosCaracteresEspeciais(abaIdentificacaoComponent.cpfCnpj);
        abaIdentificacaoComponent.cliente.tipo_pessoa = 'J';
      }
    }
    else if (abaIdentificacaoComponent.updateClienteForm) {
      if (!this.isCPF(abaIdentificacaoComponent.cpfCnpj)) {
        this.abaIdentificacao.radioGlomerado = abaIdentificacaoComponent.cliente.conglomerado;
        abaIdentificacaoComponent.cliente.porte = abaIdentificacaoComponent.cliente.sigla_porte;
        if (abaIdentificacaoComponent.cliente.conglomerado) {
          this.abaIdentificacao.radioGlomerados.find(radio => radio.id == abaIdentificacaoComponent.cliente.conglomerado).ativo = true;
          this.abaIdentificacao.radioGlomerados.find(radio => radio.id != abaIdentificacaoComponent.cliente.conglomerado).ativo = false;
        }
      }
    }
    else {
      this.transformDateValueToShow(abaIdentificacaoComponent);
    }
    this.abaIdentificacao.maxDate = new Date();
    this.abaIdentificacao.ptBR = this.abaIdentificacao.calendarProperties;
    this.abaIdentificacao.tipoPortes = [];
    UtilsCliente.tipoPortes().forEach(elemento => {
      this.abaIdentificacao.tipoPortes.push({ label: elemento.descricao, value: elemento.sigla });
    });
  }

  /**
   * Abre a modal user-sso; e mostra o retorno da ação de inclusão de usuário sso
   * @param abaIdentificacaoComponent 
   */
  abrirModalUserSSO(abaIdentificacaoComponent: AbaIdentificacaoComponent) {
    this.dialogService.addDialog(ModalUserSSOComponent, {
      cliente: abaIdentificacaoComponent.cliente
    }).subscribe(userSSOOutput => {
      if (!userSSOOutput.close) {
        const msg: string = userSSOOutput.msg;
        if (userSSOOutput.error) {
          abaIdentificacaoComponent.addMessageError(msg);
        } else {
          if (userSSOOutput.changeEmail) {
            abaIdentificacaoComponent.cliente.email = userSSOOutput.email;
            this.atualizaClienteExistente(true, abaIdentificacaoComponent);
          }
          abaIdentificacaoComponent.userCreateSucessSSO.emit(true);
          abaIdentificacaoComponent.addMessageSuccess(msg);
        }
      }
    });
  }

}
