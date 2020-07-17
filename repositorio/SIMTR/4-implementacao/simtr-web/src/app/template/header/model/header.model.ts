import { UserSIUSR } from "src/app/model";
import { UserSSO } from '../../../model/user-sso';
import { MSG_HEADER, GRUPO_PERFIL, PERFIL_ACESSO } from "src/app/constants/constants";
import { Profile } from "src/app/services/model/profile.model";
export class Header {
    app: any = {};
    userSIUSR: UserSIUSR;
    userSSO: UserSSO;
    role : string;
    filtrosvalor: any = {};
    notificacoes: any[] = [];
    listaErroGlobal: any[] = [];
    userImage: any;
    userImageDefault: any;
    hasImage: boolean = false;
    currentView: any;
    toogleSidebar: boolean = false;
    labelPerfis: string = MSG_HEADER.LABEL_PERFIS;
    labelPerfil: string = MSG_HEADER.LABEL_PERFIL;
    labelAdmin: string = GRUPO_PERFIL.ADMIN;
    labelDossieCliente: string = GRUPO_PERFIL.DOSSIE_CLIENTE;
    labelDossieProduto: string = GRUPO_PERFIL.DOSSIE_PRODUTO;
    labelExtracaoManual: string = GRUPO_PERFIL.EXTRACAO_MANUAL;
    labelTratamento: string = GRUPO_PERFIL.TRATAMENTO;
    labelTestModalExtracao: string = GRUPO_PERFIL.TES_MODAL_EXTRACAO;
    selectedRole: string;
    isMockRole: boolean;
    changeSelectedRole: string;
    presentationProfiles: Array<Profile>;
    withoutPresentationProfiles: Array<Profile>;
    cepModel: string;
    manuais: any[] = [
        { nome: 'Manual de Operação', url: '../../../assets/manual/operacao/site/index.html', roles: PERFIL_ACESSO.UMA_AUTHORIZATION },
        { nome: 'Manual de Integracao', url: '../../../assets/manual/integracao/site/index.html', roles: PERFIL_ACESSO.UMA_AUTHORIZATION },
        { nome: 'Manual de Backoffice', url: '../../../assets/manual/backoffice/site/index.html', roles: `${PERFIL_ACESSO.MTRADM},${PERFIL_ACESSO.MTRSDNTTO},${PERFIL_ACESSO.MTRSDNTTG},${PERFIL_ACESSO.MTRSDNMTZ}` },
        { nome: 'Manual de Administração', url: '../../../assets/manual/administrativo/site/index.html', roles: PERFIL_ACESSO.MTRADM },
    ];
}