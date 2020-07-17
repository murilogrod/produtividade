import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalSicliDetailPfComponent } from './modal-sicli-detail-pf.component';

describe('ModalSicliDetailPfComponent', () => {
  let component: ModalSicliDetailPfComponent;
  let fixture: ComponentFixture<ModalSicliDetailPfComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalSicliDetailPfComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalSicliDetailPfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
