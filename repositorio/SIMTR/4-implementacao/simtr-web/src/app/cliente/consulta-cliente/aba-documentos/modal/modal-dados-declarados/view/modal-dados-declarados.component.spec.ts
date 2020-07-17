import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalDadosDeclaradosComponent } from './modal-dados-declarados.component';

describe('ModalDadosDeclaradosComponent', () => {
  let component: ModalDadosDeclaradosComponent;
  let fixture: ComponentFixture<ModalDadosDeclaradosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalDadosDeclaradosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalDadosDeclaradosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
