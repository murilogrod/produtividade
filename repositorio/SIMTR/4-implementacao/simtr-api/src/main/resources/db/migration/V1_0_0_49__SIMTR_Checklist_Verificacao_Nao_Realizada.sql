/* Tabela 055 */
---------------
ALTER TABLE mtr.mtrtb055_verificacao ADD COLUMN ic_verificacao_realizada BOOL NOT NULL;

COMMENT ON COLUMN mtr.mtrtb055_verificacao.ic_verificacao_realizada IS
'Atributo utilizado para armazenar o indicativo do operador de ter realizado a verficiação ou não para aquele elemento. 
Essa situações pode ocorrer devido a existência de documentos opcionais (não obrigatórios) e que neste caso permite ao operador não analisar o mesmo.
Apesar da ausência de analise, ficará registrado que o operador optou por não realizar aquela verificação no momento da execução do tratamento do dossiê.';
