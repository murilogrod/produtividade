import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalProdutoVisualizacaoComponent } from './modal-produto-visualizacao.component';

describe('ModalProdutoVisualizacaoComponent', () => {
  let component: ModalProdutoVisualizacaoComponent;
  let fixture: ComponentFixture<ModalProdutoVisualizacaoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalProdutoVisualizacaoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalProdutoVisualizacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
