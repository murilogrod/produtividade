import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashComponent } from './dashboard.component';

describe('DashboardComponent', () => {
  let component: DashComponent;
  let fixture: ComponentFixture<DashComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
