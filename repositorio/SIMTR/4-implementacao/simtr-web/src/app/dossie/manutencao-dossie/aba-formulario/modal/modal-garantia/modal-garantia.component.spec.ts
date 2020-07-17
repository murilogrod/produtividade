import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalGarantiaComponent } from './modal-garantia.component';

describe('ModalGarantiaComponent', () => {
  let component: ModalGarantiaComponent;
  let fixture: ComponentFixture<ModalGarantiaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalGarantiaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalGarantiaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
