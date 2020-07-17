import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExtracaoManualComponent } from './extracao-manual.component';

describe('ExtracaoManualComponent', () => {
  let component: ExtracaoManualComponent;
  let fixture: ComponentFixture<ExtracaoManualComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExtracaoManualComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExtracaoManualComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
