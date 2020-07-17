import { EventEmitter, Input, Output } from '@angular/core';
import { DocumentImage } from "../documentImage";
import { AlertMessageService, ApplicationService } from 'src/app/services';
import { LOCAL_STORAGE_CONSTANTS } from 'src/app/constants/constants';
import { ConversorDocumentosUtil } from '../conversor-documentos/conversor-documentos.util.service';
import { ClassificacaoDocumento } from '../conversor-documentos/model/classificacao-documento';

export abstract class ImageChangeService extends AlertMessageService {
	@Input() imagesCopy: DocumentImage[] = [];
	@Output() imagesCopyChanged: EventEmitter<ClassificacaoDocumento> = new EventEmitter<ClassificacaoDocumento>();
	@Output() imagesChanged: EventEmitter<DocumentImage[]> = new EventEmitter<DocumentImage[]>();
	conversorDocumentosUtil: ConversorDocumentosUtil;
	applicationService: ApplicationService;

	constructor(conversorDocumentosUtil: ConversorDocumentosUtil,
		applicationService: ApplicationService) {
		super();
		this.conversorDocumentosUtil = conversorDocumentosUtil;
		this.applicationService = applicationService;
	}

	/**
	 * 
	 * @param images 
	 */
	protected emiteImagemAClassificarVisualizador(images: DocumentImage[]) {
		let manterPdfEmMiniatura: boolean = (this.applicationService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.miniaturaPDF) == 'false') ? false : true;
		let imagesMiniatura: Array<DocumentImage> = new Array<DocumentImage>();
		if (manterPdfEmMiniatura) {
			imagesMiniatura = this.conversorDocumentosUtil.manterPdfsMiniaturaVisualizacao(images);
		}
		this.imagesCopy = images;
		this.imagesChanged.emit(images);
		const classificacaoDocumento: ClassificacaoDocumento = new ClassificacaoDocumento();
		classificacaoDocumento.imagensClassificar = this.imagesCopy;
		classificacaoDocumento.qtdClassificar = manterPdfEmMiniatura ? imagesMiniatura.length : this.imagesCopy.length;
		this.imagesCopyChanged.emit(classificacaoDocumento);
	}

}
