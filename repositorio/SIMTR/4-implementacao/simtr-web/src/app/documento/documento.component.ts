import { HttpClient } from '@angular/common/http';
import { ALERT_MESSAGE_WARNING } from '../constants/constants';
import { ModalShowImageComponent } from './modal-show-image/modal-show-image.component';
import { DocumentImage } from './documentImage';
import { DialogService } from 'angularx-bootstrap-modal';
import { Component, OnInit, Input, Output, ViewEncapsulation, EventEmitter, OnChanges, SimpleChanges, AfterViewChecked } from '@angular/core';
import { AlertMessageService } from '../services';
import { FuncaoDocumental, VinculoCliente,  } from '../model';
import { ConsultaClienteService } from '../cliente/consulta-cliente/service/consulta-cliente-service';
import { ArvoreGenericaComponent } from './arvore-generica/arvore-generica.component';

export interface ModelNovosDocumentos {
  tipoDocumento: string;
  funcaoDocumental: string;
  validade: string;
  ids:{};
}

@Component({
  selector: 'documento',
  templateUrl: './documento.component.html',
  styleUrls: ['./documento.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DocumentoComponent extends AlertMessageService implements OnInit, OnChanges, AfterViewChecked {
  
  @Input() images: DocumentImage[];

  @Output() imagesChange: EventEmitter<DocumentImage[]> = new EventEmitter<DocumentImage[]>();

  @Input() funcoesDocumentais;

  @Input() tipoDocumentos;

  @Input() readOnly: Boolean;

  //@Input() showCategorizar: boolean;

  @Input() cliente: VinculoCliente;

  @Output() onFinish: EventEmitter<ModelNovosDocumentos> = new EventEmitter<ModelNovosDocumentos>();

  @Output() funcoesChange: EventEmitter<FuncaoDocumental[]> = new EventEmitter<FuncaoDocumental[]>();

  currentImage = null;
  data = null;

  isChosenAll = false;

  matricula: string;
  dataCaptura: string;
  dataValidade: string;
  origemDoc = '';
  showDetalheDoc = false;

  changeWasMade = false;

  constructor(
    private dialogService: DialogService,
    private http: HttpClient,
    private clienteService: ConsultaClienteService) {
    super();
  }

  ngOnInit() {
    if (this.images != null && this.images.length > 0) {
      this.currentImage = this.images[0].image;
      this.imageClicked(this.images[0]);
      // this.imageChecked(this.images[0], 0);
      this.createIndexForImages();
    }
  }

  private createIndexForImages() {
    if (this.images != null) {
      for (let i = 0 ; i < this.images.length; i++) {
        this.images[i].index = i;
      }
    }
  }

  private verifyIfNeedSearchGed() {
    this.data = this.images[0].data;
    if (this.data != null && this.data.id != null) {
      const allImagesIds = [];
      for (const item of this.images) {
        allImagesIds.push(item.index);
      }
    }
  }

  dragStarted(event) {
    if (this.readOnly) {
      event.stopPropagation();
      event.preventDefault();
      return;
    }
    if (!this.hasAnyChose()) {
      this.sendMessage({ msg: 'Selecione alguma página para arrastar', type: ALERT_MESSAGE_WARNING });
      event.stopPropagation();
      event.preventDefault();
      return;
    }

    //ArvoreComponent.NODE_SELECTED = null;
    ArvoreGenericaComponent.NODE_SELECTED = null;

    event.dataTransfer.setData('transferImage', this.getAllChecked());
  }

  dragEnded(event) {
    const imagesAux = [];
    for (const item of this.images) {
      if (!item.checked) {
        imagesAux.push(this.images.filter(e => e.image === item.image)[0]);
      }
    }
    this.images = imagesAux;
    this.imagesChange.emit(this.images);
  }

  ngAfterViewChecked(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.images && changes.images.currentValue != null && changes.images.currentValue.length > 0) {
      this.images = changes.images.currentValue;
      this.currentImage = changes.images.currentValue[0].image;
      this.changeWasMade = true;
      this.verifyIfNeedSearchGed();
      this.createIndexForImages();
    }
  }

  onChangeImageToShow() { 
    const divImageName = this.readOnly ? 'divImageToShowVisualizar' : 'divImageToShowCategorizar';
    const divImage = document.getElementById(divImageName);
    const imageName = this.readOnly ? 'imageToShowVisualizar' : 'imageToShowCategorizar';
    const image = document.getElementById(imageName);
    if (image != null && divImage != null) {
     image.setAttribute('style', 'width: inherit !important;');
    }
  }

  private getAllChecked(){
    const list = [];
    for (const item of this.images) {
      if (item.checked) {
        const obtImg = {
          image : item.image,
          tipo  : item.type,
          insert: 'I'
        }
        list.push(obtImg);
      }
    }
    return JSON.stringify(list);
  }

  chooseAll(): void {
  }

  private hasAnyChose() {
    if (this.images != null) {
      for (const item of this.images) {
        if (item.checked) {
          return true;
        }
      }
    }
  }

  moveUp(index) {
    const new_index = index + 1;
    this.move(index, new_index);
    this.createIndexForImages();
  }

  private move(old_index, new_index) {
    if (new_index >= this.images.length) {
      let k = new_index - this.images.length;
      while (k-- + 1) {
        this.images.push(undefined);
      }
    }
    this.images.splice(new_index, 0, this.images.splice(old_index, 1)[0]);
  }

  moveDown(index) {
    const new_index = index - 1;
    this.move(index, new_index);
    this.createIndexForImages();
  }

  /**
   * Para mostrar imagem no componente ainda na tela de exibição.
   * Para mostrar na modal ficou dentro do componente ShowImage
   * @param input
   * @param item
   */
  imageClicked(item) {
    this.currentImage =  item.image;
    this.showDetalheDoc = false;
    if (item.data) {
      this.matricula = 'Matrícula responsável: ' + item.data.matricula;
      this.dataCaptura = 'Validade documento: ' + item.data.data_validade;
      this.dataValidade = 'Data captura: ' + item.data.data_captura;
      this.origemDoc = 'Origem documento: ';
      this.showDetalheDoc = true;
    }
    this.onChangeImageToShow();
  }

  imageChecked(item, index) {
    if (this.readOnly === false) {
      const prefixImage = 'imageCategorizar-';
      if (document.getElementById(prefixImage + index) != null) {
        this.images[index].checked = !this.images[index].checked;
        document.getElementById(prefixImage + index).style.border = this.images[index].checked ? '1px solid red' : '1px solid white';
        this.imagesChange.emit(this.images);
      }
    }
  }

  onClickImageToShowInModal() {
    this.dialogService.addDialog(ModalShowImageComponent, {
      currentImage: this.currentImage.imagem == undefined ? this.currentImage: this.currentImage.imagem  ,
      matricula: this.matricula,
      dataCaptura: this.dataCaptura,
      dataValidade: this.dataValidade,
      origemDoc: this.origemDoc,
      showDetalheDoc: this.showDetalheDoc,
      imagesCarousel: this.images
    });
  }

  getVerificaImg(item) {
      if(item != undefined && item.imagem != "" && item.imagem !=null){
        return item.imagem;
      }else{
         return item;
      }

  }

  removeImage(index) {
    if (this.images != null) {
      this.images.splice(index, 1);
      this.createIndexForImages();
      this.imagesChange.emit(this.images);
    }
  }

}