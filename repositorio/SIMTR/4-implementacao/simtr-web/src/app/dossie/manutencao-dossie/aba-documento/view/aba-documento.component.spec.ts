import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AbaDocumentoComponent } from './aba-documento.component';

describe('AbaDocumentoComponent', () => {
  let component: AbaDocumentoComponent;
  let fixture: ComponentFixture<AbaDocumentoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AbaDocumentoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AbaDocumentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
