import { Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { TreeNode } from 'primeng/primeng';
import { DocumentoNode } from '../../../../model/documentoNode';

@Component({
  selector: 'produto-detalhe',
  templateUrl: './produto-detalhe.component.html',
  styleUrls: ['./produto-detalhe.component.css'],
  encapsulation : ViewEncapsulation.None
})
export class ProdutoDetalheComponent implements OnInit {

  @Input() garantias;
  @Input() documentosProdutos;
  @Input() pessoasProduto;
  
  @Output() backEvent : EventEmitter<string> = new EventEmitter<string>();
  
  @Input() docFoundProduto;

  @Input() indexMacro;
  @Input() indexProcesso;

  isExpandedAllProdutos = false;

  imagemDoc = "";
  rotacaoImg = 0;
  escalaImg = 0.70;
  showBtnProdutos = false;
  imagens: any[] = [];
  idNode: any = 0;
  uri = "";

  showDetalheDoc = false;

  matricula: string;
  dataCaptura: string;
  dataValidade: string;
  origemDoc: string;

  selectedFile: DocumentoNode;
      
  constructor() { }

  ngOnInit() {
  }

  onBack () {
    this.backEvent.emit(this.indexMacro + ',' + this.indexProcesso);
  }

  showButtonsExpandOrCollapse(): boolean {
    return this.documentosProdutos != null && this.documentosProdutos.length > 0;
  }

  expandAllProdutos() {
    this.isExpandedAllProdutos = true;
    this.documentosProdutos.forEach(node => {
      this.expandRecursive(node, true);
    });
  }

  collapseAllProdutos() {
    this.isExpandedAllProdutos = false;
    this.documentosProdutos.forEach(node => {
      this.expandRecursive(node, false);
    });
  }

  private expandRecursive(node: TreeNode, isExpand: boolean) {
    node.expanded = isExpand;
    if (node.children) {
      node.children.forEach(childNode => {
        this.expandRecursive(childNode, isExpand);
      });
    }
  }

  nodeSelect($event) {
    if($event.node.leaf) {
      this.selectedFile = $event.node;
      this.imagens = [];
      
      if(this.idNode != this.selectedFile.data.id) {
        this.clearCanvas();
      }
      if (this.selectedFile != undefined) {
        this.rotacaoImg = 0;
        this.escalaImg = 0.70;
        let img = {url: this.selectedFile, rotacao: this.rotacaoImg, escala: this.escalaImg }
        this.showBtnProdutos = true;
        this.idNode = this.selectedFile.data.id;
        this.preencheInfoDoc(this.selectedFile);
        this.drawImagePrev(img);
      }
      else {
        this.clearCanvas();
      }
      
    }
  }

  preencheInfoDoc(selecionado){
    this.matricula = "Matrícula responsável: " + selecionado.data.matricula;
    this.dataCaptura = "Validade documento: " + selecionado.data.data_validade;
    this.dataValidade = "Data captura: " + selecionado.data.data_captura;
    //this.origemDoc = "Origem documento: ";
    this.showDetalheDoc = true;
  }

  clearCanvas(){
    var canvas:any   = document.querySelector("#canvasProdutos");
    var cContext = canvas.getContext('2d');
    cContext.clearRect(0, 0, canvas.width, canvas.height);
    this.showBtnProdutos = false;
  }

  drawImage(uri) {
    var url = "";
    var rotacao = 0;
    var escala = 0;
    var uriPrinc = "";
    if(typeof uri === "string"){
      uriPrinc = uri;
      url = uri;
      rotacao = 0 || 0;
      escala = 0.70 || 1;
    }else{
      uriPrinc = uri.url;
      url = uri.url;
      rotacao = uri.rotacao || 0;
      escala = uri.escala || 1;
    }
    
    var canvas:any   = document.querySelector("#canvasProdutos");

    this.uri = uriPrinc;

    var img = new Image();
    img.src = uriPrinc;
    var cContext = canvas.getContext('2d');
    img.onload = function () {
        let w = img.width * escala,
            h = img.height * escala;
        let cw = w,
            ch = h,
            cx = 0,
            cy = 0;

        switch (rotacao) {
            case 90:
                cw = h;
                ch = w;
                cy = h * (-1);
                break;
            case 180:
                cx = w * (-1);
                cy = h * (-1);
                break;
            case 270:
                cw = h;
                ch = w;
                cx = w * (-1);
                break;
        }
        canvas.setAttribute('width', cw.toString());
        canvas.setAttribute('height', ch.toString());
        cContext.rotate(rotacao * Math.PI / 180);
        cContext.drawImage(img, cx, cy, w, h);

      }
  }

   drawImagePrev(imagem) {

    if (!imagem) return false;
    let uri = imagem.url.data.uri
    
    //if(imagem.url.data.uri.length >= 1){
      
      //for(let uris of uri){
        //this.imagens.push(uris);
        //this.indice++;
      //}
    //}

    //this.drawImage(uri[0]);
    this.drawImage(uri);

  }


  aumentarProdutos() {
    this.escalaImg += 0.10;
    if (this.escalaImg > 1) {
      this.escalaImg = 1.0;
    } 
    let img = {url: this.uri, rotacao: this.rotacaoImg, escala: this.escalaImg };
    this.drawImage(img);
  }

  diminuirProdutos() {
    this.escalaImg -= 0.10;
    if (this.escalaImg < 0.1) {
      this.escalaImg = 0.1;
    } 
    let img = {url: this.uri, rotacao: this.rotacaoImg, escala: this.escalaImg };
    this.drawImage(img);
  }

  girarDireitaProdutos() {
    this.rotacaoImg += 90;
    if (this.rotacaoImg >= 360 ) {
      this.rotacaoImg = 0;
    } 
    let img = {url: this.uri, rotacao: this.rotacaoImg, escala: this.escalaImg };
    this.drawImage(img);
  }
  girarEsquerdaProdutos() {
    this.rotacaoImg -= 90;
    if (this.rotacaoImg <= -90 ) {
      this.rotacaoImg = 270;
    } 
    let img = {url: this.uri, rotacao: this.rotacaoImg, escala: this.escalaImg };
    this.drawImage(img);
  }
  mostrarPadraoProdutos() {
    this.rotacaoImg = 0;
    this.escalaImg = 0.7
    let img = {url: this.uri, rotacao: this.rotacaoImg, escala: this.escalaImg };
    this.drawImage(img);
  }

}
