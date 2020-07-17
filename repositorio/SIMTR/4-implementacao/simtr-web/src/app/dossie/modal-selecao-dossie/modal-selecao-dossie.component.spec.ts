import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalSelecaoDossieComponent } from './modal-selecao-dossie.component';

describe('ModalSelecaoDossieComponent', () => {
  let component: ModalSelecaoDossieComponent;
  let fixture: ComponentFixture<ModalSelecaoDossieComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalSelecaoDossieComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalSelecaoDossieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
