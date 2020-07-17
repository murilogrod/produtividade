import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalSicliDetailPjComponent } from './modal-sicli-detail-pj.component';

describe('ModalSicliDetailPjComponent', () => {
  let component: ModalSicliDetailPjComponent;
  let fixture: ComponentFixture<ModalSicliDetailPjComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalSicliDetailPjComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalSicliDetailPjComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
