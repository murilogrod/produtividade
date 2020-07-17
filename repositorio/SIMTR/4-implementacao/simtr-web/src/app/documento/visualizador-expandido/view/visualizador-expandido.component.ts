import { ViewEncapsulation, Component, OnInit, Input, OnChanges } from "@angular/core";
import { DocumentImage } from "../../documentImage";
import { ConversorDocumentosUtil } from "../../conversor-documentos/conversor-documentos.util.service";
import { ModalShowImageComponent } from "../../modal-show-image/modal-show-image.component";
import { EventService, LoaderService } from "src/app/services";
import { DialogService } from "angularx-bootstrap-modal";


@Component({
	selector: 'visualizador-expandido',
	templateUrl: './visualizador-expandido.component.html',
	styleUrls: ['./visualizador-expandido.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class VisualizadorExpanditoComponent implements OnInit, OnChanges {
	constructor(
		private conversorDocumentosUtil: ConversorDocumentosUtil,
		private loadService: LoaderService,
        private dialogService: DialogService) { }

	@Input() images: DocumentImage[];

	@Input() chekarTodoasImg: boolean;

	@Input() manterPdfEmMiniatura: boolean;

	@Input() indexVisualizador: number;

	@Input() incluirImagem: boolean;
	
	listaVisualizar: DocumentImage[];
	translationOffsetX = 0;
	translationOffsetY = 0;
	imgAtual: number;

	canvas: any;

	ngOnInit() {
	}

	ngOnChanges() {
		this.canvas = document.querySelector('#canvasVisualizadorExpandido');
		this.listaVisualizar = [];
		let imageTemp = (this.images && this.indexVisualizador >= 0) ? this.images[this.indexVisualizador] : null;

		if (this.manterPdfEmMiniatura && this.listaVisualizar && this.incluirImagem && (imageTemp && imageTemp.indiceDocListPdfOriginal)) {
			this.imgAtual = 0;
			let id = imageTemp.indiceDocListPdfOriginal;
			let primeiraImagemPdf = this.images[this.indexVisualizador];
			this.conversorDocumentosUtil.$paginasRestantesPdf.subscribe(arrayPdf => {
				if(arrayPdf.length > 0) {
					this.listaVisualizar.push(primeiraImagemPdf);
					this.listaVisualizar = this.listaVisualizar.concat(arrayPdf.filter(paginaPdf => paginaPdf.indiceDocListPdfOriginal == id ));				
					this.drawImage(this.listaVisualizar[this.imgAtual].image, this.canvas);
				}
			},
			() => {
				this.loadService.hide();
			});
		} else {
			this.indexVisualizador = this.indexVisualizador ? this.indexVisualizador : 0;
			this.imgAtual = this.indexVisualizador;
			this.listaVisualizar = this.images;
			this.drawImage(this.listaVisualizar[this.indexVisualizador].image, this.canvas);
		}
	}

    onClickImageToShowInModal() {
        this.dialogService.addDialog(ModalShowImageComponent, {
            currentImage: this.listaVisualizar[this.imgAtual].image,
            imagesCarousel: this.images
        });
    }

	drawImage(uri, canvas: any) {
		// this.resetaConfigImg();
		if (uri != null) {
			let url = "";
			let rotacao = 0;
			let escala = 0;
			let uriPrinc = "";
			if (typeof uri === "string") {
				uriPrinc = uri;
				url = uri;
				rotacao = 0 || 0;
				escala = 0.7 || 1;
			} else {
				uriPrinc = uri.url;
				url = uri.url;
				rotacao = uri.rotacao || 0;
				escala = uri.escala || 1;
			}

			//   this.currentImage = uriPrinc;

			const img = new Image();
			img.setAttribute('class', 'brilhoImg');
			img.src = `data:image/png;base64,${uriPrinc}`;
			const cContext = canvas.getContext("2d");
			cContext.save();
			cContext.clearRect(0, 0, canvas.width, canvas.height);
			cContext.translate(this.translationOffsetX, this.translationOffsetY);
			img.onload = function () {
				const w = img.width * escala,
					h = img.height * escala;
				let cw = w,
					ch = h,
					cx = 0,
					cy = 0;

				switch (rotacao) {
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
				cContext.rotate(rotacao * Math.PI / 180);
				cContext.drawImage(img, cx, cy, w, h);
			};
		}
		this.touchScroll();
	}


	private touchScroll() {

		this.touchScrollVisual();
	}

	private touchScrollVisual() {
		var viewer = TouchScroll();
		viewer.init({
			id: 'corpoVisualidadorExpandido',
			draggable: true,
			wait: false
		});
	}


	primeiraImg() {
		if (this.imgAtual > 0) {
			this.imgAtual = 0;
			this.drawImage(this.listaVisualizar[this.imgAtual].image, this.canvas);
		}
	}

	ultimaImg() {
		if (this.imgAtual < (this.listaVisualizar.length - 1)) {
			this.imgAtual = this.listaVisualizar.length - 1;
			this.drawImage(this.listaVisualizar[this.imgAtual].image, this.canvas);
		}
	}

	buscarProximoOuAnterior(proximo: boolean) {
		this.imgAtual = proximo ? this.buscarProximo(this.imgAtual) : this.buscarAnterior(this.imgAtual);
		this.drawImage(this.listaVisualizar[this.imgAtual].image, this.canvas);
	}

	private buscarAnterior(posicao: number) {
		let pos = posicao;
		if (posicao > 0) {
			pos--;
		}
		return pos;
	}

	private buscarProximo(posicao: number) {
		let pos = posicao;
		if (posicao < (this.listaVisualizar.length - 1)) {
			pos++;
		}
		return pos;
	}
}