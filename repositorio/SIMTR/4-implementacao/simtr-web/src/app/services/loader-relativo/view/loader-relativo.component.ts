import { Component, ViewEncapsulation, OnInit, Input, OnChanges, AfterViewChecked, ChangeDetectorRef } from "@angular/core";
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-loader-relativo',
  templateUrl: './loader-relativo.component.html',
  styleUrls: ['./loader-relativo.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LoaderRelativoComponent implements OnInit, OnChanges, AfterViewChecked {

  @Input() posicao: number;
  @Input() qtdComponent: number;
  @Input() eForOuSubscribe: boolean;
  @Input() ativo: boolean;
  @Input() mensagemInformativa: string;
  @Input() bloco: boolean;
  close: boolean = false;

  constructor(
    private cdRef: ChangeDetectorRef) { }

  ngOnInit() {
    this.calcularProgresso(this.posicao, this.qtdComponent);
    this.showCloseButton();
  }

  private showCloseButton() {
    if (this.ativo) {
      setTimeout(() => {
        this.close = true;
      }, environment.timeClosed);
    }
  }

  ngOnChanges() {
    this.calcularProgresso(this.posicao, this.qtdComponent);
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  calcularProgresso(posicao, total) {
    return this.mensagemInformativa = (!posicao || posicao == 0) ? '' : Math.trunc((100 * posicao) / total).toString() + "%";
  }

  fecharLoading() {
    this.ativo = false;
  }

}
