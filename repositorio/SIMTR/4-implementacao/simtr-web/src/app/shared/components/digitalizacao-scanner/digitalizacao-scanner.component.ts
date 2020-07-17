import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';

import { DigitalizacaoScannerService } from './digitalizacao-scanner.service';
import { environment } from '../../../../environments/environment';
import { LoaderService, AlertMessageService, ApplicationService, EventService } from '../../../services';
import { ConfigScaner } from 'src/app/model/configScaner';
import { CONFIG_SCANER } from 'src/app/constants/constants';
import { GlobalError } from 'src/app/global-error/model/global-error';
import { UtilsErrorGlobal } from 'src/app/global-error/utils/utils-erros';
import { FonteDocumento } from 'src/app/documento/enum-fonte-documento/fonte-documento.enum';


@Component({
  selector: 'app-digitalizacao-scanner',
  templateUrl: './digitalizacao-scanner.component.html',
  styleUrls: ['./digitalizacao-scanner.component.css']
})
export class DigitalizacaoScannerComponent extends AlertMessageService implements OnInit {

  listaDispositivos: any[];
  listaResolucao: any[];
  listaCor: any[];
  listaFormato: any[];
  listaDuplex: any[];

  protocolo: string;
  host: string;
  hostInputText: string;
  dispositivo: string;
  resolucao: string;
  cor: string;
  formato: string;
  duplex: string;
  continuo: string;
  habilitaMensagemDispositivos: boolean;
  inicializarComboDispositivo: boolean;
  configScanerSalvo = new ConfigScaner();

  @Input() userExiste: boolean;
  @Output() ImgCarregadaEvent: EventEmitter<any> = new EventEmitter<any>();



  constructor(private service: DigitalizacaoScannerService,
		private eventService: EventService,
    private appService: ApplicationService, private loadService: LoaderService) {
    super();
    this.listaResolucao = ["200", "300", "400"];
    this.listaCor = [{ desc: "Colorido", value: "RGB" },
    { desc: "Escala de Cinza", value: "GRAYSCALE" }];
    this.listaFormato = ["JPG", "PDF"];
    this.listaDuplex = ["Sim", "Não"];
    this.protocolo = environment.protocoloScanner;
    this.host = environment.hostScanner;
    this.cor = this.listaCor[0].value;
    this.formato = this.listaFormato[0];
    this.duplex = 'Não';
    this.continuo = 'Não';

  }

  ngOnInit() {
    this.obterParametrosSalvos();

  }

  atualizarDispositivos() {
    this.loadService.show();
    this.habilitaMensagemDispositivos = false;
    this.host = this.hostInputText;
    this.obterListaDispositivo();

  }

  private montaUrl():string {
    let hostSplit = this.host.split(':');
    let complementoDuplex = "&adf=on::";
    if(this.continuo == 'Sim'){
      complementoDuplex +=  "-1::";  
    }else{
      complementoDuplex += this.duplex == 'Sim' ? '2::' : '1::';
    }
    complementoDuplex += this.duplex == 'Sim' ? "on" : "off";
    return this.protocolo + "://"+ hostSplit[0] + ":" +( hostSplit[1] ? hostSplit[1] : environment.portaScanner) + "/scan?list=devices&" + complementoDuplex;
  }

  private verificaMudancaHost(): string{
    return this.montaUrl();
  }

  obterListaDispositivo() {
    let urlScanner = this.verificaMudancaHost();
    this.loadService.show();
    this.listaDispositivos = new Array<String>();
    let d;
    this.service.getDadosScanner(urlScanner).subscribe(res => {

      if (res.ok == true) {
        let lista = JSON.parse(res._body);
        if(!lista.error){
          for (var el in lista) {
            if (lista[el].indexOf("Cam") == -1) {
              this.listaDispositivos.push({ desc: lista[el], indice: el });
            }
          }
        }
      }
      this.dispositivo = d ? d : this.listaDispositivos[0].indice;
    }, error => {
      this.habilitaMensagemDispositivos = true;
      this.clearAllMessages();
      this.addMessageWarning('Não foi possível capturar a lista de scanners no endereço informado.');
      this.loadService.hide();
      throw error;
    },
    () => {
      this.loadService.hide();
    });
  }

  carregarScaner() {
    this.obterListaDispositivo();
  }

  digitalizarDocumento() {
    this.loadService.show();

    let complementoDuplex = "&adf=on::";
    if(this.continuo == 'Sim'){
      complementoDuplex +=  "-1::";  
    }else{
      complementoDuplex += this.duplex == 'Sim' ? '2::' : '1::';
    }
    complementoDuplex += this.duplex == 'Sim' ? "on" : "off";

    let urlScanner = this.protocolo + '://' +
      this.host + ':' + environment.portaScanner + '/' +
      'scan?device=' + this.dispositivo +
      '&dpi=' + this.resolucao +
      '&colorMode=' + this.cor +
      '&format=' + this.formato + 
      complementoDuplex;

    this.service.getDadosScanner(urlScanner.replace(':5128:5128', ':5128')).subscribe(res => {

      let obj = JSON.parse(res._body);
      if (obj.ok == false) {
        this.ImgCarregadaEvent.emit(new Error('Erro ao obter imagem do scanner.'));
      } else {
        obj.scannedImageList.forEach(imgFrente=>{
          let tpDoc = imgFrente.name.match(/\.[^,]\w+/).toString().replace(".", "");
          tpDoc = "image/" + tpDoc.toLowerCase();
  
          let img = {
            image: imgFrente.base64,
            name: imgFrente.name,
            checked: false,
            data: new Date(),
            source: FonteDocumento.SCANNER,
            type: tpDoc
  
          };
          this.ImgCarregadaEvent.emit(img);
        });
      }        
    }, error => {
        let erroGlobal = new GlobalError();
            erroGlobal.status = 400;
            erroGlobal.name = 'Falha de comunicação com o scanner.';
            erroGlobal.message = 'Scanner não localizado.';
        UtilsErrorGlobal.validarDigitalizacao(erroGlobal, this.eventService);
        this.loadService.hide();
        throw error;
      });
  }

  obterParametrosSalvos() {

    this.configScanerSalvo = JSON.parse(this.appService.getItemFromLocalStorage(CONFIG_SCANER.SCANNER));
    let p, h, r, c , f, d, dp, cnt;
    if(this.configScanerSalvo) {
      p = this.configScanerSalvo.protocolo;
      h = this.configScanerSalvo.host;
      r = this.configScanerSalvo.resolucao;
      c = this.configScanerSalvo.cor;
      f = this.configScanerSalvo.formato;
      d = this.configScanerSalvo.dispositivo;  
      dp = this.configScanerSalvo.duplex;
      cnt = this.configScanerSalvo.continuo;
    }  
    this.definirParametros(p, h, r, c, f, d, dp, cnt);
  }
  private definirParametros(p: string, h: string, r: string, c: string, f: string, d: string, dp: string, cnt: string) {
    this.protocolo = p ? p : environment.protocoloScanner;
    this.host = h ? h : environment.hostScanner + ':' + environment.portaScanner;
    this.hostInputText = this.host ;
    this.resolucao = r ? r : this.resolucao = this.listaResolucao[0];
    this.cor = c ? c : this.listaCor[0].value;
    this.formato = f ? f : this.listaFormato[0];
    this.dispositivo = d ? d : "0";
    this.duplex = dp ? dp : 'Não';
    this.continuo = cnt ? cnt : 'Sim';
  }

  salvarDadosScanner() {

    let configScaner = new ConfigScaner();
      configScaner.protocolo    = this.protocolo;
      configScaner.host         = this.host;
      configScaner.dispositivo  = this.dispositivo;
      configScaner.resolucao    = this.resolucao;
      configScaner.cor          = this.cor;
      configScaner.formato      = this.formato;
      configScaner.continuo     = this.continuo;
      configScaner.duplex       = this.duplex;
    this.appService.saveInLocalStorage(CONFIG_SCANER.SCANNER, JSON.stringify(configScaner));
  }

  restaurapadroes() {
    this.definirParametros(undefined, undefined, undefined, undefined, undefined, undefined, undefined, undefined);
    this.salvarDadosScanner();
    this.obterListaDispositivo();
    ;
  }

}
