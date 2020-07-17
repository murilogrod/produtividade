import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalProcessoPorPessoaComponent } from './modal-processo-por-pessoa.component';

describe('ModalProcessoPorPessoaComponent', () => {
  let component: ModalProcessoPorPessoaComponent;
  let fixture: ComponentFixture<ModalProcessoPorPessoaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalProcessoPorPessoaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalProcessoPorPessoaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
