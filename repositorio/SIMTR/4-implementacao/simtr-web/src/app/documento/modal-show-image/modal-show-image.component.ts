import { DialogComponent, DialogService } from 'angularx-bootstrap-modal';
import { Component, HostListener, SimpleChanges, AfterViewInit, OnChanges } from '@angular/core';
import { DocumentImage } from '../documentImage';

export interface MessageModalImage {
  currentImage?: string;
  matricula?: string;
  dataCaptura?: string;
  dataValidade?: string;
  origemDoc?: string;
  showDetalheDoc?: boolean;
  imagesCarousel?: DocumentImage[];
}

@Component({
  selector: 'modal-show-image',
  templateUrl: './modal-show-image.component.html',
  styleUrls: ['./modal-show-image.component.css']
})
export class ModalShowImageComponent extends DialogComponent<MessageModalImage, boolean> implements MessageModalImage, AfterViewInit, OnChanges {
  currentImage: string;
  matricula: string;
  dataCaptura: string;
  dataValidade: string;
  origemDoc: string;
  showDetalheDoc: boolean;
  imagesCarousel: DocumentImage[];
  title: string;
  /** A div que contém o canvas usado para exibir a imagem da página atual. */
  divCanvas: HTMLElement;
  /** Quando true, indica que os controles de páginação devem ser exibidos. */
  showPageControls: boolean = false;
  /** O total de páginas do documento que está sendo exibido. */
  totalPages: number;
  /** A página atual que está sendo exibida. A primeira página é a um (1). */
  currentPage: number;
  /** Array contendo a configuração de rotação de cada página. A posição do array se refere à página. */
  rotacaoImg: number[];
  /** Array contendo a configuração de escala de cada página. A posição do array se refere à página. */
  escalaImg: number[];
  /** Flag que indica se a rotação aplicada na imagem deve ser persistida. Será true se a imagem ainda não tiver sido salva no GED. */
  persistRotation: boolean;

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  ngAfterViewInit() {
    this.divCanvas = document.getElementById('canvasContainer');
    this.totalPages = this.imagesCarousel.length;
    this.showPageControls = (this.totalPages > 1);    
    this.currentPage = 1;
    this.rotacaoImg = []; 
    this.escalaImg = [];    
    this.persistRotation = !(typeof this.imagesCarousel[0].data.codigo_ged == 'string' && this.imagesCarousel[0].data.codigo_ged != ''); // Se há código GED, então a rotação da imagem não deverá ser salva.
    for (let i = 1; i <= this.totalPages; i++) {
      this.rotacaoImg[i] = 0;
      this.escalaImg[i] = 0.7;
    }    
    if (this.divCanvas != null) {
      this.drawMainCanvas();
    }
  }

  public ngOnChanges(changes: SimpleChanges): void {
    if (this.divCanvas == null) {
      this.divCanvas = document.getElementById('canvasContainer');
    }

    if (changes.matricula) {
      this.matricula = changes.matricula.currentValue;
    }
    if (changes.dataCaptura) {
      this.dataCaptura = changes.dataCaptura.currentValue;
    }
    if (changes.dataValidade) {
      this.dataValidade = changes.dataValidade.currentValue;
    }
    if (changes.origemDoc) {
      this.origemDoc = changes.origemDoc.currentValue;
    }
    if (changes.showDetalheDoc) {
      this.showDetalheDoc = changes.showDetalheDoc.currentValue;
    }
    
    this.title = null;
    if (this.showDetalheDoc) {
      this.title = this.matricula + '\n' + this.dataCaptura + '\n' + this.dataValidade;
    }

  }  

  /**
   * Desenha no canvas fornecido a imagem contida no base64, aplicando a rotação e escala indicadas.
   * @param canvas O canvas que abrigará a imagem.
   * @param rotation A rotação a ser utilizada sobre a imagem original.
   * @param scale A escala a ser utilizada sobre a imagem original.
   * @param base64 O base64 da imagem a ser desenhada.
   * @returns Após concluído o desenho da imagem no canvas, retorna uma promessa (Promise) que pode ser capturada com .then().
   */
  draw(canvas: HTMLCanvasElement, rotation: number, scale: number, base64: string) {
    return new Promise(resolve => {
      const img = new Image();
      img.src = `data:image/png;base64,${base64}`;
      const cContext = canvas.getContext("2d");
      img.onload = function () {
        const w = img.width * scale,
          h = img.height * scale;
        let cw = w,
          ch = h,
          cx = 0,
          cy = 0;

        switch (rotation) {
          case 90:
            cw = h;
            ch = w;
            cy = h * -1;
            break;
          case 180:
            cx = w * -1;
            cy = h * -1;
            break;
          case 270:
            cw = h;
            ch = w;
            cx = w * -1;
            break;
        }
        canvas.setAttribute("width", cw.toString());
        canvas.setAttribute("height", ch.toString());
        cContext.rotate(rotation * Math.PI / 180);
        cContext.drawImage(img, cx, cy, w, h);
        resolve();
      };
    });
  }

  /** Desenha a imagem da página atual no canvas principal. */
  drawMainCanvas() {
    const canvas: HTMLCanvasElement = document.querySelector('#mainCanvas');
    let rotation = this.rotacaoImg[this.currentPage];
    let scale = this.escalaImg[this.currentPage];
    let base64 = this.imagesCarousel[this.currentPage - 1].image;
    this.draw(canvas, rotation, scale, base64);
  }

  /** Substitui a imagem da página atual aplicando a rotação selecionada pelo usuário. */
  replaceCurrentPageImage() {
    if(!this.persistRotation) {
      return; // Se a rotação não deve ser persistida, finaliza o método aqui.
    }
    const canvas: HTMLCanvasElement = document.querySelector('#rotationCanvas');
    let rotation = this.rotacaoImg[this.currentPage];
    let scale = 1;
    let base64 = this.imagesCarousel[this.currentPage - 1].image;
    this.draw(canvas, rotation, scale, base64).then(() => { // Executa após o desenho da imagem no rotationCanvas.
      let src = canvas.toDataURL('image/png', 1); // Obtêm o base64 a partir do canvas rotacionado e com escala 1.
      let rotatedBase64 = src.replace(/data:.{3,100};base64,/i, ''); // Remove o prefixo 'data:tipo/ext;base64,' do src. Regex .{3,100} = qualquer caractere de 3 a 100 vezes.
      this.imagesCarousel[this.currentPage - 1].image = rotatedBase64; // Substitui a imagem da página atual no carousel origem.
      this.rotacaoImg[this.currentPage] = 0; // Zera a configuração de rotação da página atual.
    });
  }

  zoomUp() {
    this.escalaImg[this.currentPage] += 0.1;
    this.drawMainCanvas();
  }

  zoomDown() {
    // Somente realiza redução de zoom se a escala atual for maior ou igual a 0.2
    if(this.escalaImg[this.currentPage] >= 0.2) {
      this.escalaImg[this.currentPage] -= 0.1;
      this.drawMainCanvas();
    }
  }

  turnRight() {
    this.rotacaoImg[this.currentPage] += 90;
    if (this.rotacaoImg[this.currentPage] >= 360) {
      this.rotacaoImg[this.currentPage] = 0;
    }
    this.drawMainCanvas();
    this.replaceCurrentPageImage();
  }

  turnLeft() {
    this.rotacaoImg[this.currentPage] -= 90;
    if (this.rotacaoImg[this.currentPage] <= -90) {
      this.rotacaoImg[this.currentPage] = 270;
    }
    this.drawMainCanvas();
    this.replaceCurrentPageImage();
  }

  showDefault() {
    this.rotacaoImg[this.currentPage] = 0;
    this.escalaImg[this.currentPage] = 0.7;
    this.drawMainCanvas();
  }

  nextImage(){
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.drawMainCanvas();
    }
  }

  previousImage(){
    if (this.currentPage > 1) {
      this.currentPage--;
      this.drawMainCanvas();
    }
  }

  @HostListener('window:keydown', ['$event'])
  onKeydown(event: KeyboardEvent) {
    if (event.ctrlKey && !event.shiftKey && ( // Ctrl pressionado, Shift não.
			event.key == '2' || event.key == '3' || event.key == 'z' || event.key == 'Z' ||
			event.key == ',' || event.key == '.' || event.key == '<' || event.key == '>' ||
			event.key == '-' || event.key == '+' || event.key == '_' || event.key == '=')) {
			event.preventDefault(); // Previne o comportamento padrão do evento de atalho no navegador.		 
			
      if (event.key == '2') {
        this.previousImage();
      }
      if (event.key == '3') {
        this.nextImage();
      }
      if (event.key == ',' || event.key == '<') {
        this.turnLeft();
      }
      if (event.key == '.' || event.key == '>') {
        this.turnRight();
      }
      if (event.key == '-' || event.key == '_') {
        this.zoomDown();
      }
      if (event.key == '+' || event.key == '=') {
        this.zoomUp();
      }
      if (event.key == 'z' || event.key == 'Z') {
        this.showDefault();
      }
		}
  }

}
