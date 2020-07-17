export class PaeUtils {
    public validaCpfCnpj (numero) {
        const cpfCnpj = numero
        const exp = /\D/g
        const valor = cpfCnpj.toString().replace( exp, '' );
        if (valor.length <= 11) {
            return this.validarCPF(valor)
        } else {
            return this.validarCNPJ(valor);
        }
        
    }

    private validarCPF(cpf) {
        cpf = cpf.replace(/[^\d]+/g,'');
        if (cpf.length !== 11 ||
            cpf === '00000000000' ||
            cpf === '11111111111' ||
            cpf === '22222222222' ||
            cpf === '33333333333' ||
            cpf === '44444444444' ||
            cpf === '55555555555' ||
            cpf === '66666666666' ||
            cpf === '77777777777' ||
            cpf === '88888888888' ||
            cpf === '99999999999')
            return false;

        let add = 0;

        for (let i = 0; i< 9; i++) {
                add += cpf.charAt(i) * (10 - i);
        }
        let rev = 11 - (add % 11);
        
        if (rev === 10 || rev === 11)
            rev = 0;
        if (rev.toString() !== cpf.charAt(9))
            return false;
        
        add = 0;
        for (let i = 0; i < 10; i++) {
            add += cpf.charAt(i) * (11 - i);
                }
        rev = 11 - (add % 11);
        if (rev === 10 || rev === 11)
            rev = 0;
        if (rev.toString() !== cpf.charAt(10))
            return false;
        
        return true;        
    }

    private validarCNPJ(cnpj) {
        cnpj = cnpj.replace(/[^\d]+/g,'');
 
        if(cnpj === '') return false;
         
        if (cnpj.length !== 14)
            return false;
     
        // Elimina CNPJs invalidos conhecidos
        if (cnpj === '00000000000000' || 
            cnpj === '11111111111111' || 
            cnpj === '22222222222222' || 
            cnpj === '33333333333333' || 
            cnpj === '44444444444444' || 
            cnpj === '55555555555555' || 
            cnpj === '66666666666666' || 
            cnpj === '77777777777777' || 
            cnpj === '88888888888888' || 
            cnpj === '99999999999999')
            return false;
             
        // Valida DVs
        let tamanho = cnpj.length - 2
        let numeros = cnpj.substring(0,tamanho);
        const digitos = cnpj.substring(tamanho);
        let soma = 0;
        let pos = tamanho - 7;
        for (let i = tamanho; i >= 1; i--) {
          soma += numeros.charAt(tamanho - i) * pos--;
          if (pos < 2)
                pos = 9;
        }
        let resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado.toString() !== digitos.charAt(0))
            return false;
             
        tamanho = tamanho + 1;
        numeros = cnpj.substring(0,tamanho);
        soma = 0;
        pos = tamanho - 7;
        for (let i = tamanho; i >= 1; i--) {
          soma += numeros.charAt(tamanho - i) * pos--;
          if (pos < 2)
                pos = 9;
        }
        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado.toString() !== digitos.charAt(1))
              return false;
               
        return true;
    }

    paginar(event, totalRows)  {
        
        let startRow = 0;
        let endRow = totalRows
        
        if (totalRows < 1) {
          endRow = 0
        } else {
          startRow = event.first + 1
        } 
    
        if (totalRows > 0 && (event.rows+ event.first) <= totalRows ) {
          endRow = event.rows + event.first
        } 
    
        return 'Exibindo ['+ startRow + '-'+ endRow + '] de '+ totalRows  + ' registros'
    
      }
}


