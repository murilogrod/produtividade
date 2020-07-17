import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalPessoaSimtrComponent } from './modal-pessoa-simtr.component';

describe('ModalPessoaSimtrComponent', () => {
  let component: ModalPessoaSimtrComponent;
  let fixture: ComponentFixture<ModalPessoaSimtrComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalPessoaSimtrComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalPessoaSimtrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
