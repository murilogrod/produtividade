import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalProcessosComponent } from './modal-processos.component';

describe('ModalProcessosComponent', () => {
  let component: ModalProcessosComponent;
  let fixture: ComponentFixture<ModalProcessosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalProcessosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalProcessosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
