import { TestBed, inject } from '@angular/core/testing';

import { TratamentoService } from './tratamento.service';

describe('TratamentoService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TratamentoService]
    });
  });

  it('should be created', inject([TratamentoService], (service: TratamentoService) => {
    expect(service).toBeTruthy();
  }));
});
