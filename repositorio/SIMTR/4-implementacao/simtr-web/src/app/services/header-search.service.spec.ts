import { TestBed, inject } from '@angular/core/testing';

import { HeaderSearchService } from './header-search.service';

describe('HeaderSearchService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HeaderSearchService]
    });
  });

  it('should be created', inject([HeaderSearchService], (service: HeaderSearchService) => {
    expect(service).toBeTruthy();
  }));
});
