		01  ES600-ENTRADA-SAIDA.                                   
          03 ES600-ENTRADA.
            05 ES600-NO-TS             PIC  X(012)  VALUE SPACES.
            05 FILLER REDEFINES ES600-NO-TS.
              07 ES600-EIBTRNID       PIC  X(004).
              07 ES600-EIBTASKN       PIC  9(008) VALUE ZEROS.
            05 ES600-NU-PAGINA         PIC  9(005).
            05 ES600-QTDE              PIC  9(005).
            05 ES600-CNPJ-SOLICITANTE  PIC  9(014).
            05 ES600-SISTEMA           PIC  X(005).
            05 ES600-CPF-CNPJ.
              07 ES600-CPF-CNPJ-BASE  PIC  9(008).
              07 ES600-CPF-CNPJ-REST  PIC  9(006).
            05 ES600-TIPO-PESSOA       PIC  9(001).
            05 ES600-PESQ-SERASA       PIC  X(001).
            05 ES600-PESQ-CADIN        PIC  X(001).
            05 ES600-PESQ-SINAD        PIC  X(001).
            05 ES600-PESQ-CCF          PIC  X(001).
            05 ES600-PESQ-SPC          PIC  X(001).
            05 ES600-PESQ-SICOW        PIC  X(001).
            05 ES600-FORCAR-CONSULTA   PIC  X(001).
            05 FILLER                  PIC  X(009).

        03 ES600-SAIDA.
            05 ES600-CONEXAO-SERASA    PIC  9(001).
            05 ES600-CONEXAO-CADIN     PIC  9(001).
            05 ES600-CONEXAO-SINAD     PIC  9(001).
            05 ES600-CONEXAO-CCF       PIC  9(001).
            05 ES600-CONEXAO-SPC       PIC  9(001).
            05 ES600-CONEXAO-SICOW     PIC  9(001).
            05 ES600-PESQ-PARCIAL      PIC  X(001).
            05 ES600-QTDE-TOTAL        PIC  9(005).
            05 ES600-NOME-PESSOA       PIC  X(070).
            05 ES600-TP-PESQ-SERASA    PIC  X(001).
            05 ES600-DT-PESQ-SERASA    PIC  X(008).
            05 ES600-HR-PESQ-SERASA    PIC  X(006).
            05 ES600-TP-PESQ-CADIN     PIC  X(001).
            05 ES600-DT-PESQ-CADIN     PIC  X(008).
            05 ES600-HR-PESQ-CADIN     PIC  X(006).
            05 ES600-TP-PESQ-SINAD     PIC  X(001).
            05 ES600-DT-PESQ-SINAD     PIC  X(008).
            05 ES600-HR-PESQ-SINAD     PIC  X(006).
            05 ES600-TP-PESQ-CCF       PIC  X(001).
            05 ES600-DT-PESQ-CCF       PIC  X(008).
            05 ES600-HR-PESQ-CCF       PIC  X(006).

            05 ES600-TP-PESQ-SPC       PIC  X(001).
            05 ES600-DT-PESQ-SPC       PIC  X(008).
            05 ES600-HR-PESQ-SPC       PIC  X(006).

            05 ES600-TP-PESQ-SICOW     PIC  X(001).
            05 ES600-DT-PESQ-SICOW     PIC  X(008).
            05 ES600-HR-PESQ-SICOW     PIC  X(006).

            05 ES600-TAB-OCORRENCIAS.
              07 ES600-OCORRENCIA     PIC  X(150) OCCURS 130 TIMES.
