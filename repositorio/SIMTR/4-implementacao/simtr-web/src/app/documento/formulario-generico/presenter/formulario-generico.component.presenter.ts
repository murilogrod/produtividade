import { Injectable } from "@angular/core";
import { CampoFormulario } from "src/app/model";
import { SIGLA_LARGURA_TELA } from "src/app/constants/constants";
import { formularioGenericoModel } from "../model/formulario-generico.model";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { KzCpfValidator } from "src/app/shared";

@Injectable()
export class FormularioGenericoComponentPresenter {

    formularioGenericoModel: formularioGenericoModel;

    inicializarValidacao(formulario: FormGroup, formBuilder: FormBuilder) {
        formulario = formBuilder.group({
            cpfCnpj: [null, [Validators.required, KzCpfValidator]]
        });
	}

	aplicaLarguraPCCampo(formulario: CampoFormulario, larguraCampo: string): string {
		if (window.innerWidth > 992) {
			for (let apresentacao of formulario.lista_apresentacoes) {
				if (apresentacao.tipo_dispositivo === SIGLA_LARGURA_TELA.WEB) {
					larguraCampo = 'col-sm-' + apresentacao.largura;
				}
			}
		}
		return larguraCampo;
	}

	aplicaLarguraTabletCampo(formulario: CampoFormulario, larguraCampo: string): string {
		if (window.innerWidth > 600 && window.innerWidth < 992) {
			for (let apresentacao of formulario.lista_apresentacoes) {
				if (apresentacao.tipo_dispositivo === SIGLA_LARGURA_TELA.TABLET) {
					larguraCampo = 'col-sm-' + apresentacao.largura;
				}
			}
		}
		return larguraCampo;
	}

	aplicaLarguraMobileCampo(formulario: CampoFormulario, larguraCampo: string): string {
		if (window.innerWidth < 600) {
			for (let apresentacao of formulario.lista_apresentacoes) {
				if (apresentacao.tipo_dispositivo === SIGLA_LARGURA_TELA.MOBILE) {
					larguraCampo = 'col-sm-' + apresentacao.largura;
				}
			}
		}
		return larguraCampo;
	}
    
}