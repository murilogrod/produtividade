import { Injectable } from "@angular/core";
import { ConversorDocumentosUtil } from "src/app/documento/conversor-documentos/conversor-documentos.util.service";
import { Utils } from "src/app/utils/Utils";
import { LoaderService } from "src/app/services";
import { AbaDocumentosService } from "../service/aba-documentos.service";
import { AbaDocumentos } from "../model/aba-documentos.model";
import { AbaDocumentosComponent } from "../view/aba-documentos.component";
import { ABA_DOCUMENTOS } from "src/app/constants/constants";
import { RouterService } from "src/app/services/router-service";

@Injectable()
export class AbaDocumentosComponentPresenter {

    abaDocumentos: AbaDocumentos;

    constructor(private conversorDocUtil: ConversorDocumentosUtil,
        private loadService: LoaderService,
        private abaDocumentoService: AbaDocumentosService,
        private routerService: RouterService) { }

    /**
     * 
     * @param alterado 
     */
    setArquivosAteradosClassificacao(alterado: boolean) {
        this.conversorDocUtil.$arquivosPdfOringinais.subscribe(arrayArquivosPdfOriginal => {
            arrayArquivosPdfOriginal.forEach(pdf => {
                pdf.alteradoNaClassfificacao = alterado;
            })
        },
            () => {
                this.loadService.hide();
            });
    }

    /*
     * @param documentoGED 
     */
    private verificaNececissadeConversaoEmPDF(documentoGED: any): any {
        if (documentoGED.node.imagens.length > 1) {
            let pdfBase64 = this.conversorDocUtil.converteListaImgEmPdf(documentoGED.node.imagens);
            pdfBase64 = pdfBase64.substring(pdfBase64.indexOf(',') + 1);
            documentoGED.node.mime_type = "application/pdf";
            documentoGED.node.imagens = [];
            documentoGED.node.imagens.push(pdfBase64);
        }
        return documentoGED;
    }

    /**
     * 
     * @param documentoGED 
     */
    verificaHouveAlteracaoAlgumPDF(documentoGED: any): any {
        this.conversorDocUtil.$arquivosPdfOringinais.subscribe(arrayArquivosPdfOriginal => {
            if (arrayArquivosPdfOriginal.length > 0 && !arrayArquivosPdfOriginal.find(pdf => pdf.alteradoNaClassfificacao)) {
                documentoGED.node.imagens = [];
                documentoGED.node.imagens.push(arrayArquivosPdfOriginal[documentoGED.node.indiceDocListPdfOriginal].arquivo);
            } else {
                documentoGED = this.verificaNececissadeConversaoEmPDF(documentoGED);
            }
        },
            () => {
                this.loadService.hide();
            });
        documentoGED.node.indiceDocListPdfOriginal = undefined;
        return documentoGED;
    }


    /**
     * 
     * @param router 
     * @param cpfCnpj 
     * @param aba 
     * Recarrega o dossie cliente quando salvar Documentos 
     * adicionais vindas do banco.
     */
    recarregaDossieClienteDepoisDeSalvo(router: any, cpfCnpj: any, aba: any) {
        this.routerService.recarregaDossieClienteDepoisDeSalvo(cpfCnpj, aba);
    }

    /**
     * Obtem o base64 cartão assintura do cliente
     * @param abaDocumentosComponent 
     */
    obterCartaoAssinatura(abaDocumentosComponent: AbaDocumentosComponent) {
        this.loadService.show();
        this.abaDocumentoService.obterCartaoAssinatura(this.abaDocumentos.getCPFDossieClienteSemFormatacao())
            .subscribe(base64 => {
                this.onSucessCartaoAssinatura(base64);
                this.loadService.hide();
            }, error => {
                abaDocumentosComponent.addMessageError(JSON.parse(error.error).mensagem);
                abaDocumentosComponent.addMessageError(JSON.parse(error.error).detalhe);
                this.loadService.hide();
                throw new Error(error);
            });
    }

    /**
     * Ao obter sucesso no serviço de geração de cartão assinatura
     * @param base64 
     */
    onSucessCartaoAssinatura(base64: any) {
        this.carregarCartaoAssinaturaPDF(base64);
    }

    /**
     * Recebe o base64 e converte em array de bytes para posterior download do cartão assinatura
     * @param base64 
     */
    private carregarCartaoAssinaturaPDF(base64: any) {
        const urlDownload = Utils.convertBase64ArrayBytesDownload(base64);
        this.abaDocumentos.cartaoAssinatura.nativeElement.setAttribute("download", "CartaoAssinatura.pdf");
        this.abaDocumentos.cartaoAssinatura.nativeElement.setAttribute("href", urlDownload);
        this.abaDocumentos.cartaoAssinatura.nativeElement.click();
    }
}