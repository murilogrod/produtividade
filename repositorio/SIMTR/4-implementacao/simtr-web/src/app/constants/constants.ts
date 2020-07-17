export const ARQUITETURA_SERVICOS = {
  login: '/servico/auth/login',
  logout: '/servico/auth/logout',
  notificacao: '/servico/notificacao',
  unidade: '/servico/unidade',
  servidor: '/servico/servidor',
  urlManual: '/servico/authSSO/manualUsuario',
  dossieCliente: '/negocio/v1/dossie-cliente',
  macroProccesso: '/v1/macroprocesso',
  processo: '/negocio/v1/processo',
  operacao: '/v1/produto',
  dossieProduto: '/negocio/v1/dossie-produto',
  campoFormulario: '/v1/campoformulario',
  funcaoDocumental: '/v1/funcaodocumental',
  garantia: '/v1/garantia',
  siusr: '/authSSO/getSiusr',
  image: '/image',
  tratamentoDossie: '/negocio/v1/tratamento',
  tipoDocumento: '/v1/tipodocumento',
  situacaodocumento: '/v1/situacaodocumento',
  processoAdministrativo: '/processoadministrativo/v1',
  documento: '/negocio/v1/documento',
  dashboard: '/negocio/v1/dashboard',
  consultaUsuarioSSO: '/keycloak/v1',
  inclusaoUsuarioSSO: '/keycloak/v1/incluirusuario',
  cartaoAssinatura: '/dossie-digital/v2/documento',
  extracaoManual: '/retaguarda/v1/documento',
  extracaoDados: '/retaguarda/v1/extracao-dados',
  extracaoDadosRetornaDocumentoPost: '/retaguarda/v1/extracao-dados/documento/',
  cadastro: '/cadastro/v1',
  cadastroProduto: '/cadastro/v1/produto',
  autorizacaoProduto: '/dossie-digital/v1/autorizacao',
  ENDERECO_WEB_SERVICE_CEP: '/informacoes-corporativas-publicas/v1/localidades/ceps/',
  API_MANAGER: '/api-manager',
  incluirNovaSituacaoDossie: '/administracao/v1/dossie-produto/{id}/situacao/{tipo-situacao}',
  exclusaoUltimasSituacoesDossieProduto: '/administracao/v1/dossie-produto/{id}/situacao/{quantidade}',
  atualizarUnidadesManipuladoras: '/administracao/v1/dossie-produto/{id}/unidades-tratamento',
  endereco_consulta_cnpj_receita_federal: '/cadastro-receita/v3/pessoas-juridicas/{cnpj}?campos=socios',
  exclusaoDossieProduto: '/administracao/v1/dossie-produto/{id}', 
  outsourcing: '/retaguarda/v1/outsourcing',
  administracao: '/administracao/v1'
}

export const EXTERNAL_RESOURCE = {
  searchAddress: '/informacoes-corporativas-publicas/v1/localidades/ceps/'
}

export const PROPERTY = {
  CO_UNIDADE: "co-unidade",
  CGC: "cgc",
  VINCULADAS: "vinculadas",
  USER: "user",
  APIKEY: "apikey",
  LOTACAO_FISICA: "nu-lotacaofisica"
}

export const PERFIL_ACESSO = {
  MTRADM: 'MTRADM',
  MTRTEC: 'MTRTEC',
  MTRAUD: 'MTRAUD',
  MTRSDNOPE: 'MTRSDNOPE',
  MTRSDNMTZ: 'MTRSDNMTZ',
  MTRSDNSEG: 'MTRSDNSEG',
  MTRSDNTTO: 'MTRSDNTTO',
  MTRSDNTTG: 'MTRSDNTTG',
  MTRSDNEXT: 'MTRSDNEXT',
  MTRBPMANP: 'MTRBPMANP',
  MTRBPMDEV: 'MTRBPMDEV',
  MTRSDNINT: 'MTRSDNINT',
  MTRBPMUSU: 'MTRBPMUSU',
  MTRDOSOPE: 'MTRDOSOPE',
  MTRBPMAPV: 'MTRBPMAPV',
  MTRDOSMTZ: 'MTRDOSMTZ',
  MTRBPMADM: 'MTRBPMADM',
  MTRDOSINT: 'MTRDOSINT',
  MTRPAEOPE: 'MTRPAEOPE',
  MTRPAESIG: 'MTRPAESIG',
  MTRPAEMTZ: 'MTRPAEMTZ',
  SET_SERVICO: 'SET_SERVICO',
  ECM_USER: 'ecm_user',
  UMA_AUTHORIZATION: 'uma_authorization'
}

export const PRETTY_PROFILE = {
  MTRADM: { profile: PERFIL_ACESSO.MTRADM, description: 'Administrador', nickname: 'MTR', presentation: true },
  MTRTEC: { profile: PERFIL_ACESSO.MTRTEC, description: 'Tecnologia', nickname: 'MTR', presentation: true },
  MTRAUD: { profile: PERFIL_ACESSO.MTRAUD, description: 'Auditor', nickname: 'MTR', presentation: true },
  MTRSDNOPE: { profile: PERFIL_ACESSO.MTRSDNOPE, description: 'Operador', nickname: 'SDN', presentation: true },
  MTRSDNMTZ: { profile: PERFIL_ACESSO.MTRSDNMTZ, description: 'Tratamento Matriz', nickname: 'SDN', presentation: true },
  MTRSDNSEG: { profile: PERFIL_ACESSO.MTRSDNSEG, description: 'Segurança', nickname: 'SDN', presentation: true },
  MTRSDNTTO: { profile: PERFIL_ACESSO.MTRSDNTTO, description: 'Tratamento Operador', nickname: 'SDN', presentation: true },
  MTRSDNTTG: { profile: PERFIL_ACESSO.MTRSDNTTG, description: 'Tratamento Gerencial', nickname: 'SDN', presentation: true },
  MTRSDNEXT: { profile: PERFIL_ACESSO.MTRSDNEXT, description: 'Extração de Dados', nickname: 'SDN', presentation: true },
  MTRBPMANP: { profile: PERFIL_ACESSO.MTRBPMANP, description: 'Analista de Processo', nickname: 'BPM', presentation: true },
  MTRBPMDEV: { profile: PERFIL_ACESSO.MTRBPMDEV, description: 'Desenvolvedor', nickname: 'BPM', presentation: true },
  MTRSDNINT: { profile: PERFIL_ACESSO.MTRSDNINT, description: 'Integração', nickname: 'SDN', presentation: true },
  MTRBPMUSU: { profile: PERFIL_ACESSO.MTRBPMUSU, description: 'Usuário', nickname: 'BPM', presentation: true },
  MTRDOSOPE: { profile: PERFIL_ACESSO.MTRDOSOPE, description: 'Operador', nickname: 'DOS', presentation: true },
  MTRBPMAPV: { profile: PERFIL_ACESSO.MTRBPMAPV, description: 'Aprovador', nickname: 'BPM', presentation: true },
  MTRDOSMTZ: { profile: PERFIL_ACESSO.MTRDOSMTZ, description: 'Matriz', nickname: 'BPM', presentation: true },
  MTRBPMADM: { profile: PERFIL_ACESSO.MTRBPMADM, description: 'Administrador', nickname: 'BPM', presentation: true },
  MTRDOSINT: { profile: PERFIL_ACESSO.MTRDOSINT, description: 'Integração', nickname: 'DOS', presentation: true },
  MTRPAEOPE: { profile: PERFIL_ACESSO.MTRPAEOPE, description: 'Operador', nickname: 'PAE', presentation: true },
  MTRPAESIG: { profile: PERFIL_ACESSO.MTRPAESIG, description: 'Documento Sigiloso', nickname: 'PAE', presentation: true },
  MTRPAEMTZ: { profile: PERFIL_ACESSO.MTRPAEMTZ, description: 'Matriz', nickname: 'PAE', presentation: true },
  SET_SERVICO: { profile: PERFIL_ACESSO.SET_SERVICO, presentation: false },
  ECM_USER: { profile: PERFIL_ACESSO.ECM_USER, presentation: false },
  UMA_AUTHORIZATION: { profile: PERFIL_ACESSO.UMA_AUTHORIZATION, presentation: false },
}

export const FECHA_MODAL = {
  "SHOW_PORTAL": "SHOW_PORTAL"
}

export const GRUPO_PERFIL = {
  ADMIN: 'Admin',
  DOSSIE_CLIENTE: 'Dossiê Cliente',
  DOSSIE_PRODUTO: 'Dossiê Produto',
  EXTRACAO_MANUAL: 'Extração Manual',
  TRATAMENTO: 'Tratamento',
  TES_MODAL_EXTRACAO: 'Test Modal Extração'
}

export const TIPO_PERFIL = {
  ROLE_ADMIN: [PERFIL_ACESSO.MTRADM, PERFIL_ACESSO.MTRTEC, PERFIL_ACESSO.MTRAUD, PERFIL_ACESSO.MTRSDNOPE,
  PERFIL_ACESSO.MTRSDNMTZ, PERFIL_ACESSO.MTRSDNTTO, PERFIL_ACESSO.MTRSDNTTG, PERFIL_ACESSO.MTRSDNEXT, PERFIL_ACESSO.MTRDOSOPE,
  PERFIL_ACESSO.MTRDOSMTZ, PERFIL_ACESSO.UMA_AUTHORIZATION],

  ROLE_DOSSIE_CLIENTE: [PERFIL_ACESSO.MTRADM, PERFIL_ACESSO.MTRTEC, PERFIL_ACESSO.MTRAUD, PERFIL_ACESSO.MTRSDNOPE,
  PERFIL_ACESSO.MTRSDNMTZ, PERFIL_ACESSO.MTRDOSOPE, PERFIL_ACESSO.MTRDOSMTZ, PERFIL_ACESSO.UMA_AUTHORIZATION],

  ROLE_DOSSIE_PRODUTO: [PERFIL_ACESSO.MTRADM, PERFIL_ACESSO.MTRTEC, PERFIL_ACESSO.MTRAUD, PERFIL_ACESSO.MTRSDNOPE,
  PERFIL_ACESSO.MTRSDNMTZ, PERFIL_ACESSO.MTRDOSOPE, PERFIL_ACESSO.MTRDOSMTZ, PERFIL_ACESSO.UMA_AUTHORIZATION],

  ROLE_EXTRACAO_MANUAL: [PERFIL_ACESSO.MTRADM, PERFIL_ACESSO.MTRTEC, PERFIL_ACESSO.MTRSDNEXT, PERFIL_ACESSO.UMA_AUTHORIZATION],

  ROLE_TRATAMENTO: [PERFIL_ACESSO.MTRSDNTTO, PERFIL_ACESSO.MTRSDNTTG, PERFIL_ACESSO.UMA_AUTHORIZATION],

  ROLE_TEST_MODAL_EXTRACAO: [PERFIL_ACESSO.MTRTEC, PERFIL_ACESSO.MTRAUD, PERFIL_ACESSO.MTRSDNOPE,
  PERFIL_ACESSO.MTRSDNMTZ, PERFIL_ACESSO.MTRDOSOPE, PERFIL_ACESSO.MTRDOSMTZ, PERFIL_ACESSO.UMA_AUTHORIZATION]
}

export const ROTAS = {
  NAO_AUTORIZADO: 'nao-autorizado'
}

export const CONFIG_SCANER = {
  "SCANNER": "config_scaner"
}

export const PARAMETRO_DEFINI_CONSULTA_MANUTENCAO_DOSSIE = {
  DOSSIE_PRODUTO: "dossieProduto"
}

export const LOCAL_STORAGE_CONSTANTS = {
  token: 'token',
  userSSO: 'userSSO',
  userSiusr: 'userSiusr',
  userImage: 'imagem-usuario-',
  processoPatriarca: 'patriarca',
  processoTipologia: 'tipologia',
  macroProccesso: 'macroProcesso',
  processoDossie: 'processoDossie',
  cpfCnpjDossieProduto: 'cpfCnpjDossieProduto',
  dossieSalvo: 'dossieSalvo',
  msgOrientacaoExcluida: 'msgOrientacaoExcluida',
  userUrl: 'userUrl',
  rolesInMemory: 'rls',
  logout: 'logout',
  marcaTodos: 'marcaTodos',
  miniaturaPDF: 'miniaturaPDF',
  prefixoSIMTR: 'SIMTR-',
  textPlain: 'text-plain',
  ABRIR_DOCUMENTO_EM_NOVA_JANELA: 'ABRIR_DOCUMENTO_EM_NOVA_JANELA',
  MOSTRA_PROCESSOS_OCULTOS: 'MOSTRAR_PROCESSOS_OCULTOS',
  TIPO_DOCUMENTO_SELECIONADO_ARVORE: "TIPO_DOCUMENTO_SELECIONADO_ARVORE"
}

export const DASHBOARD = {
  MSG_DADOS_VAZIO: 'Nenhum registro foi encontrado com os parâmetros passados.',
}

export const VISUALIZADOR_EXTERNO = {
  IMAGEM_EXTERNO: 'IMAGEM_EXTERNO',
  IMAGEM_EXTERNO_2: 'IMAGEM_EXTERNO_2'
}

export const SITUACAO_GATILHO_POPUP = {
  ABRIR:  'ABRIR',
  EM_ABERTO:  'EM_ABERTO',
  FECHADO: 'FECHADO'
}

export const MODAL_SOCIOS_RECEITA_FEDERAL = {
  SOCIO_FISICO: 'SOCIOPESSOAFISICA',
  SOCIO_JURIDICO: 'SOCIOPESSOAJURIDICA',
  SOCIO_JURIDICO_TIPO_RELACIONAMENTO: 'Esta pessoa não pode ser vinculado ao dossiê, pois este processo não prevê vínculos de pessoas jurídicas.',
  SOCIO_FISICO_TIPO_RELACIONAMENTO: 'Esta pessoa não pode ser vinculado ao dossiê, pois este processo não prevê vínculos de pessoas físicas.',
  SOCIO_CLIENTE_EXISTENTE: 'Este sócio já se encontra vinculado ao dossiê.',
  SOCIO_DOSSIE_CRIADO: 'No momento o dossie não se encontra em estado de manipulação. Impossibilitando a adição de novos sócios.',
}

export const USER_SSO = {
  USER_SSO_SUCESS: 'Senha do usuário cadastrada com sucesso',
}

export const VISUALIZADOR_DOCUMENTOS = {
  MSG_CLASSIFICACAO_PARTE_PDF: "Ao prosseguir com essa classificação você irá perder a estrutura original do(s) arquivo(s): [0] Não poderá mais usar a opção miniatura para o(s) mesmo(s). Tem certeza que deseja confirmar essa classificação?"
}

export const UPLOAD_DOCUMENTO = {
  MSG_PDF_CONSECUTIVO: "Não é possível carregar mais de um arquivo do tipo PDF ao mesmo tempo."
}

export const RADIO_TRATAMENTO_MENU = {
  CHECKLIST: 'Checklist',
  FORMULARIO: 'Formulario',
  GARANTIAS: 'Garantias',
  PESSOAS: 'Pessoas',
  PRODUTOS: 'Produtos'
}

export const EVENTS = {
  authorization: 'authorization',
  cleanHeaderSearch: 'cleanHeaderSearch',
  headerSearch: 'headerSearch',
  alertMessage: 'alertMessage',
  finishSaveDossieCliente: 'finishSaveDossieCliente',
  QTD_IMAGEM_VISUALIZACAO: "QTD_IMAGEM_VISUALIZACAO",
  INIT_CEP_ONLINE_DADOS_DECLARADOS: "INIT_CEP_ONLINE_DADOS_DECLARADOS",
  INIT_CEP_ONLINE_FORMULARIO_DINAMICO: "INIT_CEP_ONLINE_FORMULARIO_DINAMICO",
  eventFontUp : "eventFontUp",
  eventFontDown : 'eventFontDown',
  eventContrast : 'eventContrast'
}

export const ALERT_MESSAGE_SUCCESS = 1;
export const ALERT_MESSAGE_INFO = 2;
export const ALERT_MESSAGE_ERROR = 3;
export const ALERT_MESSAGE_WARNING = 4;
export const UNDESCOR = '_';
export const ARVORE_DOCUMENTOS = "Árvore de Documentos"
export const ROLES = "roles";
export const PERFIL = "perfil";
export const CEP = "cep";
export const CEP_BASE = "_base";
export const CEP_COMPLEMENTO = "_complemento";
export const MYME_TYPE_ALLOWED = ['image/jpeg', 'image/png', 'image/bmp', 'image/pdf', 'application/pdf'];
export const INTERCEPTOR_SKIP_HEADER = 'X-Skip-Interceptor';

export const MYME_TYPE = {
  IMAGE_JPEG: "image/jpeg",
  IMAGE_PNG: "image/png",
  IMAGE_BMP: "image/bmp",
  IMAGE_PDF: "image/pdf",
  APPLICATION_PDF: "application/pdf",
}

export const ID_TIPO_PESSOA_PATRIARCA = {
  ID_PESSOA_JURIDICA: 1,
  ID_PESSOA_FISICA: 2
}

export const TIPO_PESSOA = {
  FISICA: "F",
  JURIDICA: "J",
  AMBOS: "A"
}

export const TIPO_RELACIONAMENTO = {
  TOMADOR_PRIMEIRO_TITULAR: 'TOMADOR/PRIMEIRO TITULAR',
  PATRIARCA_TOMADOR_PRIMEIRO_TITULAR: 'TOMADOR_PRIMEIRO_TITULAR'
}

export const MESSAGE_ALERT_MENU = {
  MSG_FININALIZADO_TRATAMENTO: "Tratamento finalizado com sucesso!",
  MSG_ALTERACAO_DESCARTADA: "As alterações realizadas serão descartadas. Deseja continuar?",
  MSG_FINALIZAR_TRATAMENTO: "Não há mais dossiês disponíveis para tratamento neste processo.",
  MSG_DOSSIE_FILA_TRATAMENTO: "O dossiê está em fila de tratamento. Caso seja capturado, será retirado da fila e ao enviar qualquer modificação, ele será reinserido em posição de final de fila. Confirma tentativa de manipulação?",
  MSG_DOSSIE_CAPTURADO: "Dossiê capturado para manutenção com sucesso.",
  MSG_DOSSIE_IMPOSSIBILITADO: "Dossiê encontra-se em situação que impossibilita sua manipulação. Verificar o histórico para mais detalhes.",
  MSG_REVOGAR_DOSSIE: "Após revogar o dossiê, este será finalizado e não poderá mais ser utilizado. Confirma a operação?",
  MSG_EXCLUIR_RELACIONAMENTO: 'Deseja excluir o relacionamento ?',
  MSG_SALVO_DOSSIE_SUCESSO: "Dossie produto salvo com sucesso!",
  MSG_SALVO_DOSSIE_REVOGADO_SUCESSO: "Dossie produto revogado com sucesso!",
  MSG_ERRO_SALVAR_DOSSIE: "Falha ao salvar dossiê produto!",
  MSG_ERRO_SEM_PERFIL: "Acesso não permitido. Solicitação de perfil via acessologico.caixa",
  MSG_ALERTA_TROCA_PERFIL: "Você tem certeza que deseja trocar sua sessão para o perfil [0] ?",
  MSG_ALTERACAR_CADASTRO_CAIXA: "O cadastro será atualizado com base nos documentos que possuem dados já extraídos. Confirma atualização?"
}

export const TIPO_ARVORE = {
  ARVORE_DOSSIE_CLIENTE: 1,
  ARVORE_DOSSIE_PRODUTO: 2
}

export const TIPO_SELETOR = {
  SELETOR_LISTA_ARVORE: "seletor_1",
  SELETOR_LISTA_TIPO_DOCUMENTO: "seletor_2"
}

export const SIGLA_LARGURA_TELA = {
  WEB: "W",
  TABLET: "M",
  MOBILE: "L"
}

export const TIPO_DOCUMENTO = {
  INPUT_RADIO: "RADIO",
  INPUT_CHECKBOX: "CHECKBOX",
  SELECT: "SELECT",
  EMAIL: "EMAIL",
  DATE: 'DATE',
  TEXT: 'TEXT',
  MONETARIO: 'MONETARIO',
  DECIMAL: 'DECIMAL',
  CONTA_CAIXA: 'CONTA_CAIXA',
  CEP: 'CEP',
  CEP_ONLINE: 'CEP_ONLINE'
}

export const STATUS_FORM = {
  VALID: "VALID",
  INVALID: "INVALID"
}


export const SITUACAO_DOSSIE_ATUAL = {
  RASCUNHO: "Rascunho",
  CRIADO: "Criado",
  AGUARDANDO_TRATAMENTO: "Aguardando Tratamento",
  EM_TRATAMENTO: "Em Tratamento",
  CONFORME: "Conforme",
  ANALISE_SEGURANCA: "Analise Segurança",
  PEDENTE_SEGURANCA: "Pedente Segurança",
  SEGURANCA_FINALIZADO: "Fianlizado Conforme",
  FINALIZADO_CONFORME: "Finalizado Conforme",
  FINALIZADO_INCONFORME: "Finalizado Inconforme",
  CANCELADO: "Cancelado",
  AGUARDANDO_ALIMENTACAO: "Aguardando Alimentação",
  EM_ALIMENTACAO: "Em Alimentação",
  PENDENTE_INFORMACAO: "Pendente Informação",
  ALIMENTACAO_FINALIZADA: "Alimentação Finalizada",
  FIANLIZADO: "Finalizado",
  AGUARDANDO_COMPLEMENTACAO: "Aguardando Complementação"
}

export const STATUS_ALERT = {
  SUCESSO: "SUCESSO",
  INFOR: "INFOR",
  WARNING: "WARNING",
  ERROR: "ERROR"
}

export const INDEX_ABA_DOCUMENTO_DOSSIE = {
  ABA_VINCULO: 0,
  ABA_FORMULARIO: 1,
  ABA_DOCUMENTO: 2
}

export const DOSSIE_PRODUTO = {
  SALVAR_PARCIAL: "SALVAR_PARCIAL",
  SALVAR_ENVIAR: "SALVAR_ENVIAR",
  REVOGAR: "REVOGAR",
  VALIDAR_DOSSIE: 'VALIDAR_DOSSIE'
}

export const ABA_DOCUMENTOS = {
  ERRO_CARTAO_ASSINATURA: "Falha ao gerar cartão de assinaturas.",
}

export const TITLO_CHECKLIST = {
  CHECKLIST: "INFORMADO",
  VISUALIZAR_DOCUMENTO: "VISUALIZAR DOCUMENTOS"
}

export const TIPO_MACRO_PROCESSO = {
  PESSOA_JURIDICA: "PESSOA JURÍDICA",
  PESSOA_FISICA: "PESSOA FÍSICA",
  AMBOS: "AMBOS",
  F: "F",
  J: "J",
  A: "A"
}

export const TIPO_VINCULO = {
  CLIENTE: "cliente",
  PRODUTO: "produto",
  GARANTIA: "garantia"
}

export const ALERT_SALVAR_DOSSIE_PRODUTO = {
  SEQUENCIA_TITULARIDADE_FALHA: "Alteração não permitida. Existe sequência de titularidade em aberto. Favor ajustar os registros de forma a manter a lista de titulares com os valores entre 1 e $$.",
  CAMPOS_FORMULARIO_OBRIGATORIO: "Existem campos de formulário obrigatórios não preenchidos!",
  CAMPOS_FORMULARIO_EMAIL_OBRIGATORIO: "Campos email do formulário Inválido!",
  VINCULO_CLIENTE_PRINCIPAL: "Necessario vincular um cliente com o tipo: "
}

export const SITUACAO_DOSSIE_PRODUTO = {
  TRATAMENTO: "TRATAMENTO",
  NOVO: "NOVO",
  MANTER: "MANTER",
  CONSULTAR: "CONSULTAR",
  CANCELAR: "CANCELAR",
  LISTAR: "LISTAR",
  COMPLEMENTACAO: "COMPLEMENTACAO"
}

export const SITUACAO_DOSSIE_BANCO = {
  CRIADO: "Criado",
  RASCUNHO: "Rascunho",
  EM_ALIMENTACAO: "Em Alimentação",
  PENDENTE_INFORMACAO: "Pendente Informação",
  AGUARDANDO_ALIMENTACAO: "Aguardando Alimentação",
  AGUARDANDO_TRATAMENTO: "Aguardando Tratamento",
  AGUARDANDO_COMPLEMENTACAO: "Aguardando Complementação"
}

export const ABA_DOSSIE_CLIENTE = {
  IDENTIFICACAO: 0,
  DOCUMENTOS: 1,
  PRODUTOS: 2
}

export const TIPO_ARVORE_DOCUMENTO = {
  CLIENTE: "CLIENTE",
  PRODUTO: "PRODUTO",
  GARANTIAS: "GARANTIAS",
  FASE_DOSSIE: "FASE",
  DOSSIE: "DOSSIE",
  CHECKLIST: "CHECKLIST"
}

export const TIPO_LAYOUT = {
  HORIZONTAL_A: "HORIZONTAL_A",
  VERTICAL_A: "VERTICAL_A",
  HORIZONTAL_B: "HORIZONTAL_B",
  VERTICAL_B: "VERTICAL_B"
}

export const MSG_MODAL_PESSOA = {
  PROCESSO_NAO_PERMITIDO_CPF_CNPJ: 'Processo não permitido para o tipo de pessoa selecionado.',
  VALIDA_EXISTENCIA_NA_LISTA: 'Vínculo Pessoa já realizado para esse tipo de relacionamento.',
  SEQUENCIA_TITULARIDADE_INFORMADA: 'Sequência de titularidade já informada.',
  VINCULO_JA_REALIZADO: 'Vínculo Pessoa já realizado para esse tipo de relacionamento.',
  CAMPO_RELACIONADO: 'Campo Relacionado Obrigatório.',
  CONFIRMA_EXCLUIR: 'Deseja excluir o relacionamento?',
  VINCULADA_A_GARANTIA: 'Exclusão não permitida. Pessoa relacionada a garantia "$$", necessário alteração do vínculo ou exclusão do registro de garantia primeiro.',
  VINCULADA_AS_GARANTIAS: 'Exclusão não permitida. Pessoa relacionada aos vínculos [$$], necessário alteração dos vínculos ou exclusão dos registros de garantia primeiro.',
  VINCULADA_RELACIONADO: 'Exclusão não permitida. Pessoa relacionada ao vínculo $$, necessário exclusão do vínculo dependente primeiro.',
  VINCULADA_RELACIONADOS: 'Exclusão não permitida. Pessoa relacionada aos vínculos [$$], necessário exclusão dos vínculos dependentes primeiro."'
}
export const MSG_ABA_VINCULO = {
  EXCLUIR_GARANTIA: 'Exclusão não permitida. Produto relacionada ao vínculo da garantia $$, necessário exclusão do vínculo dependente primeiro.',
  EXCLUIR_GARANTIAS: 'Exclusão não permitida. Produto relacionado aos vínculos de garantia [$$], necessário exclusão dos vínculos dependentes primeiro.',
  PESSOA_EXISTE_LISTA: 'Esta pessoa já existe na lista de vínculos com o tipo de relacionamento informado.'
}

export const MSG_CONSULTA_DOSSIE_CLIENTE = {
  CPF_NAO_ENCONTRADO: 'Não existe dossiê para o cliente informado e o CPF não foi localizado junto a Receita Federal do Brasil.',
  CNPJ_NAO_ENCONTRADO: 'Não existe dossiê para o cliente informado e o CNPJ não foi localizado junto a Receita Federal do Brasil.',
}

export const MSG_TRATAMENTO = {
  SEM_MACROPROCESSO: 'Não existe processos definidos para tratamento pela sua unidade.',
  CHECK_LIST_DUPLICADO: 'Foram identificados múltiplos checklists neste processo passíveis de associação ao tipo de documento $$. Favor entrar em contato com a área de suporte do sistema.',
  CHECK_LIST_NAO_EXISTE: 'Este documento não possui checklist definido para esta fase do processo e por isso não requer análise do mesmo.',
  CHECK_LIST_CONFORME: 'Documento já avaliado e a situação atual é: $$'
}

export const TIPO_MSG_TRATAMENTO = {
  TIPO_DUPLICIDADE: 'DUPLICIDADE',
  TIPO_INEXISTENTE: 'NAOEXISTE',
  TIPO_CONFORME: 'CONFORME'
}

export const MILISSEGUNDOS = {
  DIA: 86400000
}
export const MSG_HEADER = {
  LABEL_PERFIL: 'Selecione o Perfil:',
  LABEL_PERFIS: 'Perfis do Usuário:',
  LABEL_CEP: 'CEP',
}

export const MSG_DOSSIE_PRODUTO = {
  MSG_DOC_ASSINADO_DIGITAL: "Este documento está assinado digitalmente. Em caso de carga complementar ou qualquer manipulação no documento (remoção de página, reordenação, etc) fará com que a assinatura seja descartada.",
  MSG_ERRO_UPLOAD_PDF: "Erro ao fazer upload do PDF. Por favor, contate o administrador",
  MSG_EXTENSAO_NAO_PERMITIDA: "Formatos Permitidos: JPEG, PNG, BMP, JFIF, JPG e PDF",
  MSG_ERRO_CONVERSAO_PDF: "Erro ao converter page do pdf. Por favor, contate o administrador"
}

export const MSG_DOSSIE_CLIENTE = {
  MSG_EXTENSAO_NAO_PERMITIDA: "Formatos Permitidos: JPEG, PNG, BMP, JFIF, JPG e PDF",
}

export const SWITCH_TRATMENTO = {
  DOSSIE_FASE: "DOSSIE_FASE",
  CLIENTE: "CLIENTE",
  PRODUTO: "PRODUTO",
  GARANTIAS: "GARANTIAS"
}

export const MSG_VALIDAR_SUCESSO = {
  TRATAMENTO: 'Tratamento Validado com Sucesso!',
  DOSSIE_PRODUTO: 'Dossiê Produto Validado com Sucesso!',
}

export const URL_CACH = {
  DASHBOARD: '/dashboard'
}

export const LINKS = {
  REUTILIZAR: 'Reutilizar'
}

export const STATUS_DOCUMENTO = {
  REJEITADO: "Rejeitado"
}

export const MESSAGEM_MODAL_EXTRACAO_DE_DADOS = {
  SUCESSO: 'Registro salvo com sucesso.',
  CANCELADO: 'Registro cancelado com sucesso.'
}

export const EVENTO_DASHBOARD = {
  TODOS: "TODOS"
}

export const DADOS_DECLARADO = 'DADOS-DECLARADOS';
export const FORMULARIO_DINAMICO: string = "FORMULARIO-DINAMICO";
export const BROWSERS_NAME = {
  CHROME : "chrome",
  FIREFOX : "firefox"
}
export const PATH_NOT_SUPPORTED_PAGE = '/assets/browsers/not_supported.html';
export const MIN_FIREFOX_VERSION = 60;