       01 AREA-DE-ENTRADA.
          03 AREA-DE-CONTROLE.
             05 FILLER                  PIC X(004).
             05 WEB-SERVER              PIC X(025).
             05 TIMESTAMP-WEB-SERVER    PIC 9(016).
             05 TRANSACTION-SERVER      PIC X(025).
             05 TIMESTAMP-TRAN-SERVER   PIC 9(016).
             05 AMBIENTE                PIC X(001).
             05 TP-USUARIO              PIC X(002).
             05 FILLER                  PIC X(008). 
             05 NIS                     PIC 9(011). 
             05 NIVEL-AUTENT            PIC X(002).
             05 SISTEMA                 PIC X(006). 
             05 FUNCAO                  PIC X(010). 
             05 OPERACAO                PIC X(016).
             05 NO-PROGRAMA             PIC X(008).
             05 AREA-A-PASSAR-PARA-PGM  PIC X(001).
             05 FORMA-DE-CHAMAR-O-PGM   PIC X(001).
             05 FILLER                  PIC X(001).
             05 TAMANHO-AREA-RETORNO    PIC 9(005).
             05 FILLER                  PIC X(242). 

*
          03 AREA-DE-ENTRADA-SALDO.
             05 LK_CO_REGRA_NEG         PIC X(005).
             05 LK_NU_CPF               PIC 9(011).
