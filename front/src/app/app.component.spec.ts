import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { spyOn } from 'jest-mock';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';


describe('AppComponent', () => {
  let app: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let sessionService: SessionService;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();

  });

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it('should logout', async () => {
    const sessionServiceSpy = spyOn(sessionService, 'logOut');

    app.logout();

    await fixture.whenStable();

    expect(sessionServiceSpy).toHaveBeenCalled();

  })
});
