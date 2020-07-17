import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalSicliListCocliComponent } from './modal-sicli-list-cocli.component';

describe('ModalSicliListCocliComponent', () => {
  let component: ModalSicliListCocliComponent;
  let fixture: ComponentFixture<ModalSicliListCocliComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalSicliListCocliComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalSicliListCocliComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
