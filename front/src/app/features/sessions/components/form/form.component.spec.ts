import { HttpClientModule } from '@angular/common/http';
import { Component, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations'; // 👈 remplace BrowserAnimationsModule
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect, jest } from '@jest/globals';
import { of } from 'rxjs';

import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { FormComponent } from './form.component';
import { ActivatedRoute } from '@angular/router';

@Component({template: ''})
class DummySessionsComponent {}

describe('FormComponent - branches', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let router: Router;
  let snackBar: MatSnackBar;

  const mockSessionService = {
    sessionInformation: { admin: true }
  };

  const mockSession: Session = {
    id: 1,
    name: 'Yoga Class',
    date: new Date(),
    teacher_id: 1,
    description: 'Relaxing session',
    users: []
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]),
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatSnackBarModule,         // ✅ apporte Overlay + providers
        NoopAnimationsModule       // ✅ évite element.animate en test
      ],
      declarations: [FormComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        {
        provide: ActivatedRoute,
        useValue: {
          snapshot: {
            paramMap: {
              get: (key: string) => '123'  // 👈 ici on force toujours "123"
            }
          }
        }
      },
        SessionApiService
        // ❌ ne pas fournir MatSnackBar ici
      ],
      schemas: [NO_ERRORS_SCHEMA]  // ✅ ignore les balises Angular Material du template
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    router = TestBed.inject(Router);
    snackBar = TestBed.inject(MatSnackBar);

    jest.spyOn(router, 'navigate').mockResolvedValue(true as any);
    jest.spyOn(snackBar, 'open');
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions');

    fixture.detectChanges();
  });

  it('should redirect if user is not admin', () => {
    (TestBed.inject(SessionService) as any).sessionInformation.admin = false;

    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should go into update mode and fetch session details', () => {
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/123');
    const detailSpy = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));

    component.ngOnInit();

    expect(component.onUpdate).toBe(true);
    expect(detailSpy).toHaveBeenCalledWith('123');
  });

  it('should call exitPage after create()', () => {
    const spy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockSession));

    component.sessionForm!.setValue({
      name: 'Yoga Class',
      date: '2025-08-20',
      teacher_id: 1,
      description: 'Relaxing session'
    });

    component.submit();

    expect(spy).toHaveBeenCalled();
    expect(snackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call exitPage after update()', () => {
    component['onUpdate'] = true;
    component['id'] = '123';
    const spy = jest.spyOn(sessionApiService, 'update').mockReturnValue(of(mockSession));

    component.sessionForm!.setValue({
      name: 'Yoga Class',
      date: '2025-08-20',
      teacher_id: 1,
      description: 'Relaxing session'
    });

    component.submit();

    expect(spy).toHaveBeenCalledWith('123', component.sessionForm!.value);
    expect(snackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });
});
