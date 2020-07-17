import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalSicliComponent } from './modal-sicli.component';

describe('ModalSicliComponent', () => {
  let component: ModalSicliComponent;
  let fixture: ComponentFixture<ModalSicliComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalSicliComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalSicliComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
