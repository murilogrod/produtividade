import { TestBed } from '@angular/core/testing';

import { ExtracaoManualService } from './extracao-manual.service';

describe('ExtracaoManualService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ExtracaoManualService = TestBed.get(ExtracaoManualService);
    expect(service).toBeTruthy();
  });
});
