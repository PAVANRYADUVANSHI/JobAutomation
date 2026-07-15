export interface JobListing {
  id: number;
  title: string;
  company: string;
  location: string;
  description: string;
  applyUrl: string;
  source: string;
  track: 'JAVA_FULLSTACK' | 'GENAI';
  javaMatchScore: number;
  genaiMatchScore: number;
  selectedResumeVersion: string;
  status: string;
  isRemote: boolean;
  isManualWatchlist: boolean;
  postedDate: string;
  fetchedAt: string;
}

export interface Application {
  id: number;
  jobListing: JobListing;
  resumeVersion: string;
  coverLetter: string;
  status: 'SHORTLISTED' | 'APPLIED' | 'RESPONSE' | 'INTERVIEW' | 'REJECTED' | 'OFFER';
  track: string;
  matchScore: number;
  submittedAt: string | null;
  responseAt: string | null;
  interviewAt: string | null;
  notes: string | null;
  autoSubmitted: boolean;
  followUpSent: boolean;
  createdAt: string;
}

export interface CandidateProfile {
  id: number;
  name: string;
  email: string;
  phone: string;
  linkedin: string;
  githubHandle: string;
  portfolio: string;
  location: string;
  experienceLevel: string;
  reviewMode: 'MANUAL' | 'AUTO';
  dailyShortlistTarget: number;
}

export interface AnalyticsData {
  totalApplications: number;
  shortlisted: number;
  applied: number;
  responses: number;
  interviews: number;
  rejected: number;
  offers: number;
  javaTrackTotal: number;
  genaiTrackTotal: number;
  javaTrackApplied: number;
  genaiTrackApplied: number;
  dailyCounts: Record<string, number>;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
