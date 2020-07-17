import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaeApensosComponent } from './pae-apensos.component';

describe('AbaDocumentosComponent', () => {
  let component: PaeApensosComponent;
  let fixture: ComponentFixture<PaeApensosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaeApensosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaeApensosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
