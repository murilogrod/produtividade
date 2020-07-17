import { Processo, VinculoCliente } from "src/app/model";
import { VinculoArvore } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { FormGroup, FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";

export class ConsultaCliente {
	searchDone?: boolean = false;
	userFound?: boolean = false;
	inserting?: boolean = false;
	searching?: boolean = false;
	docFound?: boolean = false;
	userExiste?: boolean;
	docFoundIna = false;
	docEmpty?: boolean = true;
	updateClienteForm?: boolean = false;
	processo?: Processo;
	cliente?: VinculoCliente;
	cpfCnpj?: string;
	cpfCnpjDossieProduto?: any;
	indexAba?: number;
	isNewSearch = false;
	sicliInformation?: any;
	sicliError = false;
	habilitaNovoProduto?: boolean = false;
	verBotaoCriaDossie?: boolean;
	msgConsultaDossie?: string;
	tipoConsulta?: string;
	listaVinculoArvore: Array<VinculoArvore>;
	listaProdutos: any[] = [];
	idUltimoDossieProdutoCadastrado: number;
	formulario: FormGroup;
	route: ActivatedRoute;
	userSSO?: boolean;
	dossiePessoaFisica?: boolean;
	productionEnvironment: boolean;
}