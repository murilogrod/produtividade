import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaeContratacaoComponent } from './pae-contratacao.component';

describe('PaeContratacaoComponent', () => {
  let component: PaeContratacaoComponent;
  let fixture: ComponentFixture<PaeContratacaoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaeContratacaoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaeContratacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
