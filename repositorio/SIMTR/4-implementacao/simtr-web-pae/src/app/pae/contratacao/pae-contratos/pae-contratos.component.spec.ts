import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaeContratosComponent } from './pae-contratos.component';

describe('AbaDocumentosComponent', () => {
  let component: PaeContratosComponent;
  let fixture: ComponentFixture<PaeContratosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaeContratosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaeContratosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
