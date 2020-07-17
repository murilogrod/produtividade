package br.gov.caixa.dossie.enumerate;

public enum EnumMensagemInterface {
	
	AUTENTICACAO_SEGURANCA_ERRO		(-1,"Usuário ou Senha inválidos."),
	
	SUCESSO							(0,"Operação realizada com sucesso"),			
	ERRO							(500,"Ocorreu um erro ao realizar a operação"),
		
	GERAL_PROCESSAMENTO				(300,"Falha geral de processamento"),
	GERAL_FALHA_OBJETO				(305,"Falha ao processar objeto (JSON)"),
	GERAL_FALHA_IO					(306,"Falha ao ler/gravar dados em disco (IO)"),	
	GERAL_FALHA_PROC_SERVIDOR		(309,"Falha ao processar servidor"),
	GERAL_FALHA_CONECTAR_BANCO		(310,"Falha ao conectar banco"),
	
	JSON_PARAMETRO_ERRO				(501,"JSON com parametros inválidos"),
	TOKEN_INVALIDO					(502,"Token inválido ou não informado"),
	TOKEN_EXPIRADO					(503,"Token expirado ou inválido por favor efetue a autenticação novamente"),
	
	
	USUARIO_NAO_LOCALIZADO			(510,"Usuário %s não foi localizado"),
	USUARIO_SENHA_INVALIDA			(511,"A senha informada não é válida"),
	USUARIO_NAO_HABILITADO			(512,"Este Usuário ainda não foi habilitado, contate o administrador"),
	USUARIO_FALHA_CADASTRAR			(513,"Falha ao cadastrar o usuário"),
	USUARIO_JA_CADASTRADO			(514,"Usuário %s já foi registrado"),
	USUARIO_PERFIL_INADEQUADO		(515,"O Usuário %s não possui o perfil adequado a este procedimento"),
	USUARIO_FALHA_ATUALIZAR			(516,"Falha ao atualizar Usuário"),
	USUARIO_FALHA_NOVA_SENHA		(518,"Falha ao processar nova senha"),
	USUARIO_SEM_SENHA				(519,"Este Usuário não possui senha de acesso definida"),
	USUARIO_FALHA_IDENTIFICAR		(520,"Falha ao identificar Usuário"),
	USUARIO_FALHA_PROCESSAR			(521,"Falha ao processar Usuário"),
	USUARIO_FALHA_NOTIFICAR			(522,"Falha ao notificar Usuário"),
	USUARIO_ACESSO_DIA_NAO_UTIL		(526,"Tentativa de acesso em dia não útil"),
	USUARIO_ACESSO_FORA_TURNO		(526,"Tentativa de acesso fora do turno definido"),
	
	
	PROJETO_FALHA_PROCESSAR			(600,"Falha ao processar projeto"),
	PROJETO_NAO_LOCALIZADO			(601,"Projeto %s não foi localizado"),
	PROJETO_FALHA_ATUALIZAR			(602,"Falha ao atualizar projeto"),
	PROJETO_FALHA_REMOVER			(603,"Falha ao remover projeto %s"),
	PROJETO_FALHA_REGISTRAR			(604,"Falha ao registrar projeto"),
	PROJETO_NENHUM_LOCALIZADO		(605,"Nenhum projeto localizado"),
	PROJETO_OPERACAO_NEGADA			(606,"Operação não permitida"),
	PROJETO_FALHA_PURGAR			(607,"Falha ao purgar projeto %s"),
	
	
	GERAL_INOPERANTE				(600,"Servidor momentaneamente inoperante"),
	GERAL_DADO_NAO_RECEBIDO			(601,"Falha ao processar comando. Dados não recebidos"),
	GERAL_COMANDO_NAO_CONHEC		(602,"Falha ao processar comando. Comando não reconhecido: %s"),
	GERAL_SESSAO_EXPIRADA			(603, "Sessão expirada."),
	GERAL_PARAM_RECEBIDO			(604, "Parametro não recebido: %s"),
	GERAL_USUARIO_INVALIDO			(603, "Usuário inválido."),
	GERAL_PROC_NAO_AUTORIZADO		(605, "Procedimento não autorizado."),
	
	AUDITORIA_PROC_SUSPEITO			(653, "Procedimento suspeito."),
	
	COMPART_FALHA_PROCESSAR			(700,"Falha ao processar compartilhamento"),	
	COMPART_NAO_LOCALIZADO			(701,"Compartilhamento não foi localizado"),	
	COMPART_FALHA_REMOVER			(703,"Falha ao remover compartilhamento"),
	COMPART_FALHA_REGISTRAR			(704,"Falha ao registrar compartilhamento"),
	COMPART_NENHUM_LOCALIZADO		(705,"Nenhum compartilhamento localizado"),
	
	
	BANCO_INOPERANTE 				(700,"Servidor momentaneamente inoperante"),
	BANCO_FALHA_PROCESSAR			(701,"Falha ao processar requisição: %s"),
	BANCO_LIMITE_TOTAL				(702,"Limite no total de resultados alcançado"),
	
	CRIPTO_PROCESSAR 				(800,"Falha ao processar criptografia"),
	CRIPTO_ALGORITMO_NAO_ENC		(801,"Algoritimo de criptografia não encontrado"),
	CRIPTO_DIMEN_NAO_ENC 			(802,"Dimensionador de criptografia não encontrado"),
	CRIPTO_CHAVE_INVALIDA 			(803,"Chave de criptografia inválida"),
	CRIPTO_TAM_BLC_INVALIDO 		(804,"Tamanho de bloco de criptografia inválido"),
	CRIPTO_DIMEN_INVALIDO 			(805,"Dimensionador de criptografia inválido"),
	CRIPTO_COD_INVALIDO 			(806,"Código de criptografia inválido"),
	
	EVENTO_FALHA_REGISTRAR 			(900,"Erro ao registrar o evento"),
	
	PUSH_ENVIO_MSG 					(1000,"Erro ao enviar a mensagem push"),
	PUSH_ENVIO_EMAIL 				(1001,"Erro ao enviar a mensagem de email push"),
	PUSH_LISTAR_INSCRITOS 			(1002,"Erro ao listar os inscritos"),
	
	NAVEGADOR_ERRO					(1100,"Erro ao registrar o navegador"),
	NAVEGADOR_LOCALIZACAO_ERRO		(1101,"Falha ao localizar o navegador "),
		
	DISPOSITIVO_ERRO				(1200,"Erro ao registrar o dispositivo"),
	DISPOSITIVO_LOCALIZACAO_ERRO	(1201,"Falha ao localizar o dispositivo "),
		
	SO_ERRO							(1300,"Erro ao registrar o sistema operacional"),
	SO_LOCALIZACAO_ERRO				(1301,"Falha ao localizar o sistema operacional ")
	
	;
	
	
	private int codigo;
	private String mensagem;
	
	private EnumMensagemInterface(int codigo, String mensagem) {
		this.codigo = codigo;
		this.mensagem = mensagem;
	}
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}