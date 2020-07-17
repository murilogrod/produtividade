import { TestBed } from '@angular/core/testing';

import { MudancaSalvaService } from './mudanca-salva.service';

describe('MudancaSalvaService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MudancaSalvaService = TestBed.get(MudancaSalvaService);
    expect(service).toBeTruthy();
  });
});
