import api from './api';
import { Application, JobListing, CandidateProfile, AnalyticsData, PageResponse } from '../types';

export const authService = {
  login: (username: string, password: string) =>
    api.post<{ token: string; username: string }>('/api/auth/login', { username, password }),
  register: (username: string, password: string) =>
    api.post<string>('/api/auth/register', { username, password }),
};

export const profileService = {
  get: () => api.get<{ candidate: CandidateProfile; resumeVersions: any[] }>('/api/profile'),
  updateReviewMode: (mode: string) => api.put<CandidateProfile>(`/api/profile/review-mode?mode=${mode}`),
};

export const jobService = {
  list: (page = 0, size = 20, status?: string, track?: string) => {
    const params: any = { page, size };
    if (status) params.status = status;
    if (track) params.track = track;
    return api.get<PageResponse<JobListing>>('/api/jobs', { params });
  },
  shortlisted: () => api.get<JobListing[]>('/api/jobs/shortlisted'),
  watchlist: () => api.get<JobListing[]>('/api/jobs/watchlist'),
};

export const applicationService = {
  queue: () => api.get<Application[]>('/api/applications/queue'),
  list: (page = 0, size = 20, status?: string) => {
    const params: any = { page, size };
    if (status) params.status = status;
    return api.get<PageResponse<Application>>('/api/applications', { params });
  },
  updateStatus: (id: number, status: string, notes?: string) =>
    api.put<Application>(`/api/applications/${id}/status`, { status, notes }),
  shortlist: () => api.post<Application[]>('/api/applications/shortlist'),
  followUp: () => api.get<Application[]>('/api/applications/follow-up'),
};

export const analyticsService = {
  get: () => api.get<AnalyticsData>('/api/analytics'),
  schedulerLogs: () => api.get<any[]>('/api/analytics/scheduler-logs'),
};

export const schedulerService = {
  run: () => api.post<{ status: string; message: string }>('/api/scheduler/run'),
  status: () => api.get<{ status: string; fetched?: number; shortlisted?: number; message?: string }>('/api/scheduler/status'),
  fetch: () => api.post<{ fetched: number }>('/api/scheduler/fetch'),
};
