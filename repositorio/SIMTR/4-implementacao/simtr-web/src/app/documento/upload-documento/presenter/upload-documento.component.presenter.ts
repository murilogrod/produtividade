import { Injectable, EventEmitter } from "@angular/core";
import { MSG_DOSSIE_PRODUTO, MYME_TYPE_ALLOWED, MYME_TYPE, UPLOAD_DOCUMENTO } from "src/app/constants/constants";
import { AlertMessageService, LoaderService } from "src/app/services";
import { ArquivoPdfOriginal } from "../../conversor-documentos/model/arquivo-pdf-original";
import { ConversorDocumentosUtil } from "../../conversor-documentos/conversor-documentos.util.service";
import { DocumentImage } from "../../documentImage";
import { Observable } from "rxjs";

@Injectable()
export class UploadDocumentoComponentPresenter {
    constructor(private conversorDocUtil: ConversorDocumentosUtil, private loadService: LoaderService) {
        this.conversorDocUtil.setArquivosPdfOringinais(new Array<ArquivoPdfOriginal>());
        this.conversorDocUtil.setPaginasRestantesPdf(new Array<DocumentImage>());
    }

    /**
     * 
     * @param file 
     */
    addArquivoPdfOriginal(file: string) {
        let pdf = new ArquivoPdfOriginal();
        pdf.arquivo = file;
        pdf.alteradoNaClassfificacao = false;
        pdf.arquivoAssinadoDigitalmente = false;
        this.conversorDocUtil.$arquivosPdfOringinais.subscribe(arrayArquivosPdfOriginal => {
            arrayArquivosPdfOriginal.push(pdf);
        },
        () => {
            this.loadService.hide();
        });
    }


    /**
     * 
     * @param qtdPaginas 
     */
    addQuantidadePaginasArquivoPdfOriginal(qtdPaginas: number) {
        this.conversorDocUtil.$arquivosPdfOringinais.subscribe(arrayArquivoPdfOriginal => {
            arrayArquivoPdfOriginal[arrayArquivoPdfOriginal.length - 1].quantidadePaginas = qtdPaginas;
        },
        () => {
            this.loadService.hide();
        });
    }

    /**
     * 
     * @param alterado 
     */
    setArquivosAteradosClassificacao(alterado: boolean) {
        this.conversorDocUtil.$arquivosPdfOringinais.subscribe(arrayArquivoPdfOriginal => {
            arrayArquivoPdfOriginal.forEach(pdf => {
                pdf.alteradoNaClassfificacao = alterado;
            })
        },
        () => {
            this.loadService.hide();
        });
    }

    /**
     * 
     * @param assinado 
     */
    setArquivoContemAssinaturaDigital(assinado: boolean) {
        this.conversorDocUtil.$arquivosPdfOringinais.subscribe(arrayArquivosPdfOriginal => {
            arrayArquivosPdfOriginal[arrayArquivosPdfOriginal.length - 1].arquivoAssinadoDigitalmente = assinado;
        },
        () => {
            this.loadService.hide();
        });
    }

    /**
     * 
     * @param images 
     */
    alteraTodosPdfAssinadosDevidoCarregamentoPrevioImagem(images: DocumentImage[]) {
        if (images.find(image => undefined == image.indiceDocListPdfOriginal)) {
            this.conversorDocUtil.$arquivosPdfOringinais.subscribe(arrayArquivosPdfOriginal => {
                arrayArquivosPdfOriginal.forEach(pdf => {
                    pdf.alteradoNaClassfificacao = true;
                });
            },
            () => {
                this.loadService.hide();
            });
        }
    }

    /**
     * 
     * @param images 
     */
    alteraTodosPdfsAssinadosPorCarregamentoImagens(images: DocumentImage[]) {
        let imagesPdf = images.filter(image => image.indiceDocListPdfOriginal);
        this.conversorDocUtil.$arquivosPdfOringinais.subscribe(arrayArquivosPdfOriginal => {
            imagesPdf.forEach(imagePdf => {
                arrayArquivosPdfOriginal[imagePdf.indiceDocListPdfOriginal].alteradoNaClassfificacao = true;
            });
        },
        () => {
            this.loadService.hide();
        });
    }

    /**
     * Armazena paginas restantes do pdf para posterior visualização, caso o usuario optar por escolher a
     * opção de converter o pdf em imagens para visualização total das miniaturas do documento na tela
     * @param image Imagem de pagina de pdf não visualizada na tela
     */
    guardaImagemRestantePdf(image: DocumentImage) {
        this.conversorDocUtil.$paginasRestantesPdf.subscribe(arrayPaginasRestantesPdf => {
            arrayPaginasRestantesPdf.push(image);
        },
        () => {
            this.loadService.hide();
        });
    }

    /**
     * Retorna todas as imagens restantes dos pdfs em miniatura, a partir da segunda página em diante
     */
    getArrayTodasImagensRestantesCadaPdf(): Observable<Array<DocumentImage>>{
        return this.conversorDocUtil.$paginasRestantesPdf;
    }

    /**
   * Retorna os tipos permitidos para upload
   * jpeg, png, bmp, pdf
   * @param files 
   */
    getAllowedFiles(files: File[]): File[] {
        let allowedFiles = new Array<File>();
        for (let file of files) {
            MYME_TYPE_ALLOWED.forEach(mta => {
                if (file.type.indexOf(mta) > -1) {
                    allowedFiles.push(file);
                }
            });
        }
        return allowedFiles;
    }

    /**
     * 
     * @param files Verifica se existe uma extensao não permitida para upload
     */
    isNotAllowedFile(files: File[]): boolean {
        let allowed: boolean = true;
        let flag: number = 0;
        file: for (let file of files) {
            for (let mta of MYME_TYPE_ALLOWED) {
                if (file.type.indexOf(mta) > -1) {
                    allowed = false;
                    flag = 0;
                    continue file;
                } else {
                    flag++;
                    allowed = true;
                    if (flag == MYME_TYPE_ALLOWED.length) {
                        break file;
                    } else {
                        continue;
                    }
                }
            }
        }
        return allowed;
    }

    existePDFsConsecutivos(files: File[]) {
        let existePDFConsecutivo: boolean = false;
        let contadorPdf: number = 0;
        for (let file of files) {
            if (file.type == MYME_TYPE.APPLICATION_PDF) {
                contadorPdf++
            }
            if (contadorPdf > 1) {
                existePDFConsecutivo = true;
                break;
            }
        }
        return existePDFConsecutivo;
    }

    mostraAlertaDocumentosPDFsConsecutivos(alertMessagesErrorChanged: EventEmitter<string[]>, alertMessageService: AlertMessageService): void {
        let messagesError = new Array<string>();
        messagesError.push(UPLOAD_DOCUMENTO.MSG_PDF_CONSECUTIVO);
        alertMessagesErrorChanged.emit(messagesError);
        alertMessageService.clearAllMessages();
    }

    /**
     * Mostra a msg de erro para tipo não permitido.
     * @param alertMessagesErrorChanged 
     * @param alertMessageService 
     * @param fileIsNotAllowed 
     */
    mostraAlertaErroTipoNaoPermitdo(alertMessagesErrorChanged: EventEmitter<string[]>, alertMessageService: AlertMessageService, fileIsNotAllowed: boolean): void {
        if (fileIsNotAllowed) {
            let messagesError = new Array<string>();
            messagesError.push(MSG_DOSSIE_PRODUTO.MSG_EXTENSAO_NAO_PERMITIDA);
            alertMessagesErrorChanged.emit(messagesError);
            alertMessageService.clearAllMessages();
        }
    }
}