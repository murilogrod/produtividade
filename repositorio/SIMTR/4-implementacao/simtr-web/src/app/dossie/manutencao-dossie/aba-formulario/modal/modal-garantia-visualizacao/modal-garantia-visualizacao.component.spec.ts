import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalGarantiaVisualizacaoComponent } from './modal-garantia-visualizacao.component';

describe('ModalProdutoVisualizacaoComponent', () => {
  let component: ModalGarantiaVisualizacaoComponent;
  let fixture: ComponentFixture<ModalGarantiaVisualizacaoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalGarantiaVisualizacaoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalGarantiaVisualizacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
