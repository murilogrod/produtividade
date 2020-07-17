import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProdutoDocumentoComponent } from './produto-documento.component';

describe('ProdutoDocumentoComponent', () => {
  let component: ProdutoDocumentoComponent;
  let fixture: ComponentFixture<ProdutoDocumentoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProdutoDocumentoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProdutoDocumentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
