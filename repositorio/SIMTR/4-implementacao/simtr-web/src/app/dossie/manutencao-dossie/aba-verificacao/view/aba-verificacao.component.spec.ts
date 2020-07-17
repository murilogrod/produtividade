import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AbaVerificacaoComponent } from './aba-verificacao.component';

describe('AbaVerificacaoComponent', () => {
  let component: AbaVerificacaoComponent;
  let fixture: ComponentFixture<AbaVerificacaoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AbaVerificacaoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AbaVerificacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
