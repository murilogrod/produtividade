import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AbaDocumentosComponent } from './aba-documentos.component';

describe('AbaDocumentosComponent', () => {
  let component: AbaDocumentosComponent;
  let fixture: ComponentFixture<AbaDocumentosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AbaDocumentosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AbaDocumentosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
