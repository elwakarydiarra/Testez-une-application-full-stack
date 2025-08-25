import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { expect } from '@jest/globals';


describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const pathService = 'api/session';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should GET all sessions (all)', () => {
    const mockSessions: Session[] = [
      { id: 1, name: 'Yoga', description: 'desc', date: new Date(), teacher_id: 2, users: [] }
    ];

    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne(pathService);
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should GET one session (detail)', () => {
    const mockSession: Session = { id: 123, name: 'Pilates', description: 'desc', date: new Date(), teacher_id: 1, users: [] };

    service.detail('123').subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne(`${pathService}/123`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should POST to create a session', () => {
    const newSession: Session = { id: 0, name: 'Meditation', description: 'relax', date: new Date(), teacher_id: 1, users: [] };
    const createdSession = { ...newSession, id: 99 };

    service.create(newSession).subscribe((session) => {
      expect(session).toEqual(createdSession);
    });

    const req = httpMock.expectOne(pathService);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newSession);
    req.flush(createdSession);
  });

  it('should PUT to update a session', () => {
    const updatedSession: Session = { id: 1, name: 'Updated Yoga', description: 'new', date: new Date(), teacher_id: 1, users: [] };

    service.update('1', updatedSession).subscribe((session) => {
      expect(session).toEqual(updatedSession);
    });

    const req = httpMock.expectOne(`${pathService}/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession);
    req.flush(updatedSession);
  });

  it('should DELETE a session', () => {
    service.delete('1').subscribe((res) => {
      expect(res).toBeTruthy();
    });

    const req = httpMock.expectOne(`${pathService}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({ success: true });
  });

    it('should POST to participate in a session', () => {
    service.participate('1', '10').subscribe(() => {
      expect(true).toBe(true);
    });

    const req = httpMock.expectOne(`${pathService}/1/participate/10`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should DELETE to unParticipate from a session', () => {
    service.unParticipate('1', '10').subscribe(() => {
      expect(true).toBe(true);
    });

    const req = httpMock.expectOne(`${pathService}/1/participate/10`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

});
