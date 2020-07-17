import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaeProcessoComponent } from './pae-processo.component';

describe('PaeProcessoComponent', () => {
  let component: PaeProcessoComponent;
  let fixture: ComponentFixture<PaeProcessoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaeProcessoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaeProcessoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
