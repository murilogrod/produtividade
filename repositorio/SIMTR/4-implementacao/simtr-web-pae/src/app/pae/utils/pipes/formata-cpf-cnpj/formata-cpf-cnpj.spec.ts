import { FormataCpfCnpj } from './formata-cpf-cnpj.pipe';

describe('FormataMimePipe', () => {
  it('create an instance', () => {
    const pipe = new FormataCpfCnpj();
    expect(pipe).toBeTruthy();
  });
});
