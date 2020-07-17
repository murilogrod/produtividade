# Expressão Angular

# Perfis

>> Funcionalidade acessada pelos seguintes perfis: 
	  
>> + **MTRADM**
 
>> + **MTRSDNTTG**

>> + **MTRSDNTTO**

<span style="color:blue">**OBS: Solicitação de acesso via <span style="color:blue">https://novoacessologico.caixa</span>, selecionando o sistema SIMTR e, em seguida, os perfis desejados. **</span>



Tipo Expressao{
    FORMULARIO_FASE = "FF",
    GRID_CLIENTE 	= "GC",
    GRID_GARANTIA 	= "GG",
    GRID_PRODUTO 	= "GP"
}

Vinculo Pessoa - atributo de Cliente

identificador 		= id;
nome/RazãoSocial 	= nome;
tipoRelacionamento 	= tipo_relacionamento;
relacionado 		= indica_relacionado;
sequencia 			= indica_sequencia;
indicaSequencia 	= sequencia_titularidade;


Vinculo Produto - atributo de Produto

Código operação			= codigo_operacao;
Código Modalidade 		= codigo_modalidade;
Valor 					= valor;
Taxa 					= taxa_juros;
Período Taxa 			= periodo_juros;
Praza Carência 			= carencia;
Liquidação/Renovação? 	= liquidacao;
número Contrato 		=  numero_contrato;


Vinculo Garantias  - atributo de Garantia

Código Garantia 	= garantia;
Código Produto 		= produto;
Código Bacen 		= codigo_bacen;
nome 				= descricao;
Valor 				= valor;
Percenteual 		= percentual_garantia;
Forma de Utilização = forma_garantia;






