import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaeApensosContratosComponent } from './pae-apensos-contratos.component';

describe('AbaDocumentosComponent', () => {
  let component: PaeApensosContratosComponent;
  let fixture: ComponentFixture<PaeApensosContratosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaeApensosContratosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaeApensosContratosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
