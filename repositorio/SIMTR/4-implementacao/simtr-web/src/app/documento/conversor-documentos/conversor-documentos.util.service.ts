import { Injectable } from "@angular/core";
import { ArquivoPdfOriginal } from "./model/arquivo-pdf-original";
import { BehaviorSubject, Observable } from "rxjs";
import jsPDF from 'jspdf';
import { AlertMessageService, LoaderService } from "src/app/services";
import { DocumentImage } from "../documentImage";
import { MYME_TYPE } from "src/app/constants/constants";
import { FonteDocumento } from "../enum-fonte-documento/fonte-documento.enum";

@Injectable()
export class ConversorDocumentosUtil extends AlertMessageService {
  arquivosPdfOringinais: BehaviorSubject<Array<ArquivoPdfOriginal>> = new BehaviorSubject<Array<ArquivoPdfOriginal>>(null);
  paginasRestantesPdf: BehaviorSubject<Array<DocumentImage>> = new BehaviorSubject<Array<DocumentImage>>(null);

  constructor(private loadService: LoaderService) {
    super();
  }

  get $arquivosPdfOringinais() {
    return this.arquivosPdfOringinais.asObservable();
  }

  setArquivosPdfOringinais(arquivosPdfOringinais: Array<ArquivoPdfOriginal>) {
    this.arquivosPdfOringinais.next(arquivosPdfOringinais);
  }

  get $paginasRestantesPdf() {
    return this.paginasRestantesPdf.asObservable();
  }

  setPaginasRestantesPdf(paginasRestantesPdf: Array<any>) {
    this.paginasRestantesPdf.next(paginasRestantesPdf);
  }

  /**
   * Retira paginas restantes do pdf e mantem somente a primeira pagina
   * @param images imagens a serem apresentadas no visualizador de documentos
   */
  public manterPdfsMiniaturaVisualizacao(images: DocumentImage[]) {
    let imagesAux: DocumentImage[] = new Array<DocumentImage>();
    imagesAux = imagesAux.concat(images);
    imagesAux.forEach(image => {
      let arrayPaginasRestantesPdfsMiniatura: DocumentImage[] = this.paginasRestantesPdf.getValue();
      if (image.primeiraPagina) {
        let existePaginaRestantes: DocumentImage[] = this.vericaJaExistePaginasRestantes(image, arrayPaginasRestantesPdfsMiniatura);
        if (existePaginaRestantes.length == 0) {
          let imagesRestantesPdf: DocumentImage[] = this.filtraPaginasRestantesPdfAtual(image, images);
          arrayPaginasRestantesPdfsMiniatura = arrayPaginasRestantesPdfsMiniatura.concat(imagesRestantesPdf);
          this.paginasRestantesPdf = new BehaviorSubject<Array<DocumentImage>>(arrayPaginasRestantesPdfsMiniatura);
        }
      }
    });
    images = images.filter(pagina => {
      return pagina.primeiraPagina || pagina.type != "application/pdf"
    });
    return images;
  }

  /**
   * Verica se ja existe paginas restantes do pdf atual armazenadas no array
   * @param imageAtual 
   * @param arrayPaginasRestantesPdfsMiniatura 
   */
  private vericaJaExistePaginasRestantes(imageAtual: DocumentImage, arrayPaginasRestantesPdfsMiniatura: DocumentImage[]) {
    return arrayPaginasRestantesPdfsMiniatura.filter(pagina => {
      return !pagina.primeiraPagina &&
        pagina.indiceDocListPdfOriginal == imageAtual.indiceDocListPdfOriginal
    });
  }

  /**
   * Filtra as páginas restantes do pdf atual para serem adcionadas no array de visualização da tela
   * @param imageAtual 
   * @param images 
   */
  private filtraPaginasRestantesPdfAtual(imageAtual: DocumentImage, images: DocumentImage[]) {
    return images.filter(pagina => {
      return !pagina.primeiraPagina &&
        pagina.indiceDocListPdfOriginal == imageAtual.indiceDocListPdfOriginal
    });
  }

  /**
   * Verifca se paginas pdf já foram carregadas anteriormente não precisar carregar denovo
   * @param imageAtual 
   * @param images 
   */
  private vericaPaginasRestantesJaForamCarregadasVisualizacao(imageAtual: DocumentImage, images: DocumentImage[]) {
    return images.filter(pagina => pagina.indiceDocListPdfOriginal == imageAtual.indiceDocListPdfOriginal);
  }

	/**
	 * Adiona Paginas de pdf restantes para apresentação completa no visualizador de documentos
	 */
  public addPaginasRestantesPdfVisualizacao(images: DocumentImage[]) {
    let imagesAux: DocumentImage[] = new Array<DocumentImage>();
    let paginasOrdenadas: DocumentImage[] = new Array<DocumentImage>();
    imagesAux = imagesAux.concat(images);
    imagesAux.forEach((image) => {
      let paginasPdfAtualInseridas: DocumentImage[] = this.vericaPaginasRestantesJaForamCarregadasVisualizacao(image, images);
      if (paginasPdfAtualInseridas.length < image.totalPaginas) {
        let arrayPaginasRestantesPdfsMiniatura: DocumentImage[] = this.paginasRestantesPdf.getValue();
        if (image.primeiraPagina) {
          let paginasRestantesPdf: DocumentImage[] = this.filtraPaginasRestantesPdfAtual(image, arrayPaginasRestantesPdfsMiniatura);
          paginasRestantesPdf.unshift(image);
          paginasOrdenadas = paginasOrdenadas.concat(paginasRestantesPdf);
          let paginasRestantesOutrosPdfs: DocumentImage[] = arrayPaginasRestantesPdfsMiniatura
            .filter(paginasRestantes => {
              return paginasRestantes.indiceDocListPdfOriginal != image.indiceDocListPdfOriginal
            });
          this.paginasRestantesPdf = new BehaviorSubject<Array<DocumentImage>>(paginasRestantesOutrosPdfs);
        }
      }
    });
    if (paginasOrdenadas.length > 0 && paginasOrdenadas.some(pagina => pagina.type == MYME_TYPE.APPLICATION_PDF && !pagina.primeiraPagina)) {
      images = this.organizaListaImagensParaVisualizacao(images, paginasOrdenadas);
    }
    return images;
  }

  /**
   * Atualiza lista de imagesn comm pdfs nas primeiras posições e imagens na últimas
   * @param images 
   */
  private organizaListaImagensParaVisualizacao(images: DocumentImage[], paginasOrdenadas: DocumentImage[]) {
    let listaImagensPermanentes = new Array<DocumentImage>();
    images.forEach(image => {
      paginasOrdenadas.forEach(paginaOrdenada => {
        if ((paginaOrdenada.indiceDocListPdfOriginal !== image.indiceDocListPdfOriginal && paginaOrdenada.name !== image.name) || (paginaOrdenada.type !== image.type)) {
          if (!listaImagensPermanentes.includes(image)) {
            listaImagensPermanentes.push(image);
          }
        }
      });
    });
    images = paginasOrdenadas;
    const ocorrenciaImagens: boolean = this.verificaOcorrenciaImagensEmArrayOrdenado(paginasOrdenadas, listaImagensPermanentes);
    if (!ocorrenciaImagens) {
      listaImagensPermanentes = this.removeOcorrenciaIgualArrayImagensPermanentes(images, listaImagensPermanentes);
      images = images.concat(listaImagensPermanentes);
    }
    return images;
  }

  /**
   * Retorna imagens divergentes entre duas listas de imagens
   * @param images 
   * @param listaImagensPermanentes 
   */
  private removeOcorrenciaIgualArrayImagensPermanentes(images: DocumentImage[], listaImagensPermanentes: DocumentImage[]): Array<DocumentImage> {
    const imagensDivergentes: DocumentImage[] = new Array<DocumentImage>();
    listaImagensPermanentes.forEach(imagemPermanente => {
      images.forEach(image => {
        if (!images.includes(imagemPermanente)) {
          if (!imagensDivergentes.includes(imagemPermanente)) {
            imagensDivergentes.push(imagemPermanente);
          }
        }
      })
    })
    return imagensDivergentes;
  }

  /**
   * Verifica ocorrencia de lista de imagens em outra lista de imagens
   * @param paginasOrdenadas 
   * @param listaImagensPermanentes 
   */
  private verificaOcorrenciaImagensEmArrayOrdenado(paginasOrdenadas: DocumentImage[], listaImagensPermanentes: DocumentImage[]): boolean {
    const ocorrenciaImagens: DocumentImage[] = new Array<DocumentImage>();
    listaImagensPermanentes.forEach(image => {
      paginasOrdenadas.forEach(paginaOrdenada => {
        if (image.indiceDocListPdfOriginal === paginaOrdenada.indiceDocListPdfOriginal && image.primeiraPagina === paginaOrdenada.primeiraPagina) {
          ocorrenciaImagens.push(image);
        }
      });
    });
    if (ocorrenciaImagens.length == listaImagensPermanentes.length) {
      return true;
    }
    return false;
  }

  /**
   * Ordena todas as imagens resultantes da concatenação de miniaturas e pdfs restantes
   * mantendo sua ordenação original
   * @param imagensSelecionadasRestantes 
   * @param imagensSelecionadas 
   */
  public ordenaImagensConformeMiniaturaPDF(imagensSelecionadasRestantes: DocumentImage[], imagensSelecionadas: DocumentImage[]): DocumentImage[] {
    let x: number = 0;
    let y: number = 0;
    if (imagensSelecionadasRestantes && imagensSelecionadasRestantes.length > 0) {
      let imagensSelecionadasRestantesOriginal = imagensSelecionadasRestantes.length;
      labelSelecionada: for (let i = x; i < imagensSelecionadas.length; i++) {
        for (let j = y; j < imagensSelecionadasRestantes.length; j++) {
          if (i == 0) {
            imagensSelecionadasRestantes.splice(j, 0, imagensSelecionadas[j]);
            x = i + 1;
            y = j + 1;
            continue labelSelecionada;
          }
          let indice = j + 1;
          if (imagensSelecionadasRestantes[indice]) {
            let indiceDocPdf = imagensSelecionadasRestantes[indice].indiceDocListPdfOriginal;
            if (imagensSelecionadasRestantes[j].indiceDocListPdfOriginal !== indiceDocPdf) {
              imagensSelecionadasRestantes.splice(indice, 0, imagensSelecionadas[i]);
              x = i + 1;
              y = j + 1;
              continue labelSelecionada;
            }
          }
        }
      }
      if (imagensSelecionadasRestantes.length > imagensSelecionadasRestantesOriginal) {
        imagensSelecionadas = imagensSelecionadasRestantes;
      }
    }
    return imagensSelecionadas;
  }


  /**
  * Retorna todas as imagens restantes dos pdfs em miniatura, a partir da segunda página em diante
  */
  getArrayTodasImagensRestantesCadaPdf(): Observable<Array<DocumentImage>> {
    return this.$paginasRestantesPdf;
  }

  getBase64(file: File): Promise<any> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsBinaryString(file);
      reader.onload = () => resolve(btoa(reader.result.toString()));
      reader.onerror = error => reject(error);
    });
  }

  getBinary(file: File) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsArrayBuffer(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }

  getAsText(file: File) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsText(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }

  getAsDataURL(file: File) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }

  convertDataURIToBinary(dataURI): Uint8Array {
    let base64_maker = ';base64,';
    let base64Index = dataURI.indexOf(base64_maker) + base64_maker.length;
    let base64 = dataURI.substring(base64Index);
    let raw = atob(base64);
    let rawLength = raw.length;
    let array = new Uint8Array(new ArrayBuffer(rawLength));
    for (var i = 0; i < rawLength; i++) {
      array[i] = raw.charCodeAt(i);
    }
    return array;
  }

  converteListaImgEmPdf(listaImagem: any[]) {
    let doc = new jsPDF('p', 'pt', 'a4', true);
    for (let i = 0; i < listaImagem.length; i++) {
      if (listaImagem[i].source === FonteDocumento.SCANNER) {
        let img = listaImagem[i].image ? listaImagem[i].image : listaImagem[i].img;
        let width = doc.internal.pageSize.getWidth();
        let height = doc.internal.pageSize.getHeight();
        doc.addImage(img, 'PNG', 0, 0, width, height);
        if ((i + 1) < listaImagem.length) {
          doc.addPage();
        }
      }
      if (listaImagem[i].source === FonteDocumento.UPLOAD) {
        let larguraPDF = 585;
        let alturaPDF = 825;
        let newHeight, newidth, proporcao;
        let img = listaImagem[i].image ? listaImagem[i].image : listaImagem[i].img;
        proporcao = this.calcularProporcaoImg(listaImagem, i, larguraPDF, proporcao, alturaPDF);
        if (proporcao && proporcao > 1) {
          newHeight = listaImagem[i].altura / proporcao;
          newidth = listaImagem[i].largura / proporcao;
        } else {
          newHeight = listaImagem[i].altura;
          newidth = listaImagem[i].largura;
        }
        doc.addImage(img, 'PNG', 5, 5, (newidth == larguraPDF ? (newidth - 5) : newidth), (newHeight == alturaPDF ? (newHeight - 5) : newHeight), undefined, 'FAST');
        if ((i + 1) < listaImagem.length) {
          doc.addPage();
        }
      }
    }
    let pdfBase63 = doc.output('datauristring');
    return pdfBase63.substring(pdfBase63.indexOf(',') + 1);
  }
  
  private calcularProporcaoImg(listaImagem: any[], i: number, largPdf: number, proporcao: any, altuPdf: number) {
    proporcao = 0;
    if (listaImagem[i].largura > largPdf && (listaImagem[i].altura < altuPdf || (listaImagem[i].largura > largPdf && listaImagem[i].altura < listaImagem[i].largura))) {
      proporcao = (listaImagem[i].largura / largPdf);
    } else if (listaImagem[i].altura > altuPdf) {
      proporcao = (listaImagem[i].altura / altuPdf);
    }
    return proporcao;
  }

  /**
   * Verica necessidade de converão para pdf multipaginado, permanecer o pdf original ou permanecer em um documento png.
   * @param documentos 
   */
  converteDocumentosDoosieProduto(documento: any) {
    documento.dossieCliente = false;
    this.$arquivosPdfOringinais.subscribe(arrayArquivosPdfOriginal => {
      if (arrayArquivosPdfOriginal.length > 0 &&
        undefined != documento.indiceDocListPdfOriginal &&
        !arrayArquivosPdfOriginal[documento.indiceDocListPdfOriginal].alteradoNaClassfificacao &&
        documento.mime_type == "application/pdf") {
        documento.binario = arrayArquivosPdfOriginal[documento.indiceDocListPdfOriginal].arquivo;
        arrayArquivosPdfOriginal.splice(documento.indiceDocListPdfOriginal, 1);
        this.arquivosPdfOringinais = new BehaviorSubject<Array<ArquivoPdfOriginal>>(arrayArquivosPdfOriginal);
      } else if (documento && documento.conteudos && documento.conteudos.length > 1) {
        documento.mime_type = "application/pdf";
        documento.binario = this.converteListaImgEmPdf(documento.conteudos);
      } else {
        let imgUnica = documento.conteudos[0];
        documento.mime_type = "image/png";
        documento.binario = imgUnica.img;
      }
      documento.conteudos = undefined;
      documento.indiceDocListPdfOriginal = undefined;
    },
      () => {
        this.loadService.hide();
      });
  }

	/**
	 * Verica necessidade de converão para pdf multipaginado, permanecer o pdf original ou permanecer em um documento png.
	 * @param documento
	 */
  verificaHouveAlteracaoAlgumPDFDossieCliente(documento: any): any {
    documento.dossieCliente = true;
    this.$arquivosPdfOringinais.subscribe(arrayArquivosPdfOriginal => {
      if (arrayArquivosPdfOriginal.length > 0 &&
        undefined != documento.node.indiceDocListPdfOriginal &&
        !arrayArquivosPdfOriginal[documento.node.indiceDocListPdfOriginal].alteradoNaClassfificacao &&
        !documento.node.imagens.find(image => image.type == "image/png")) {
        documento.node.imagens = undefined;
        documento.node.binario = arrayArquivosPdfOriginal[documento.node.indiceDocListPdfOriginal].arquivo;
        arrayArquivosPdfOriginal.splice(documento.node.indiceDocListPdfOriginal, 1);
        this.arquivosPdfOringinais = new BehaviorSubject<Array<ArquivoPdfOriginal>>(arrayArquivosPdfOriginal);
      } else {
        documento = this.verificaNececissadeConversaoEmPDFDossieCliente(documento);
      }
    },
      () => {
        this.loadService.hide();
      });
    documento.node.indiceDocListPdfOriginal = undefined;
    return documento;
  }

	/*
	 * @param documento
	 */
  private verificaNececissadeConversaoEmPDFDossieCliente(documento: any): any {
    if (documento.node.imagens.length > 1) {
      let pdfBase64 = this.converteListaImgEmPdf(documento.node.imagens);
      documento.node.mime_type = "application/pdf";
      documento.node.imagens = undefined;
      documento.node.binario = pdfBase64;
    } else {
      let image = documento.node.imagens[0].image;
      documento.node.mime_type = "image/png";
      documento.node.imagens = undefined;
      documento.node.binario = image;
    }
    return documento;
  }
}