import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

jest.mock('../../services/session-api.service');

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let api: jest.Mocked<SessionApiService>;

  const mockSession: Session = {
    id: 10,
    name: 'Yoga session',
    description: 'Relax and stretch',
    date: new Date(),
    teacher_id: 1,
    users: []
  };

  const mockSessionService = {
    sessionInformation: {
      id: 1,
      admin: true
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule
      ],
      providers: [
        { provide: SessionApiService, useValue: { detail: jest.fn() } },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '10' } } } },
        { provide: MatSnackBar, useValue: { open: jest.fn() } },
        { provide: SessionService, useValue: mockSessionService } // <-- mock SessionService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    api = TestBed.inject(SessionApiService) as jest.Mocked<SessionApiService>;
  });

  it('should call api.detail and load session successfully', () => {
    api.detail.mockReturnValue(of(mockSession));

    component.ngOnInit();

    expect(api.detail).toHaveBeenCalledWith('10');
    expect(component.session).toEqual(mockSession);
  });

  it('should handle 404 error when session not found', () => {
    api.detail.mockReturnValue(throwError(() => ({ status: 404 })));

    component.ngOnInit();

    expect(api.detail).toHaveBeenCalledWith('10');
    expect(component.session).toBeUndefined();
  });

});
