import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalShowImageComponent } from './modal-show-image.component';

describe('ModalShowImageComponent', () => {
  let component: ModalShowImageComponent;
  let fixture: ComponentFixture<ModalShowImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalShowImageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalShowImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
