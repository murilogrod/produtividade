/*==============================================================*/
/* Table: MTRTB045_ATRIBUTO_EXTRACAO                            */
/* ADD COLUMN: PC_ALTERACAO_PERMITIDO                           */
/*==============================================================*/
ALTER TABLE mtrsm001.mtrtb045_atributo_extracao ADD COLUMN pc_alteracao_permitido INT4 null;
COMMENT ON COLUMN mtrsm001.mtrtb045_atributo_extracao.pc_alteracao_permitido IS 'Atributo que representa o percentual adicional de alteração permitido sobre o valor recebido do OCR, no formato inteiro.
Exemplo:
Considere que foi recebido do OCR um valor com 90% de assertividade.
1) Se o atributo permite alteração de até 6% do conteúdo recebido do OCR, o valor armazenado será 6, mas a aplicação deve permitir 16% de alteração (10% da margem de assertividade do OCR, mais 6% permitidos por este atributo);

2) Se o atributo permite alteração de até 10% do conteúdo recebido do OCR, o valor armazenado será 10, mas a aplicação deve permitir 20% de alteração (10% da margem de assertividade do OCR, mais 10% permitidos por este atributo);

O valor máximo para o atributo é 100. Caso a soma da margem de assertividade do OCR com o valor do atributo resulte num valor superior a 100, a aplicação deverá limitar, considerando que está permitido 100% de alteração do valor.';