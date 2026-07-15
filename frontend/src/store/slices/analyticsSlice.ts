import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { AnalyticsData } from '../../types';
import { analyticsService } from '../../services/jobService';

export const fetchAnalytics = createAsyncThunk('analytics/fetch', async () => {
  const res = await analyticsService.get();
  return res.data;
});

export const fetchSchedulerLogs = createAsyncThunk('analytics/logs', async () => {
  const res = await analyticsService.schedulerLogs();
  return res.data;
});

const analyticsSlice = createSlice({
  name: 'analytics',
  initialState: {
    data: null as AnalyticsData | null,
    logs: [] as any[],
    loading: false,
  },
  reducers: {},
  extraReducers: (b) => {
    b.addCase(fetchAnalytics.pending, (s) => { s.loading = true; });
    b.addCase(fetchAnalytics.fulfilled, (s, a) => { s.loading = false; s.data = a.payload; });
    b.addCase(fetchSchedulerLogs.fulfilled, (s, a) => { s.logs = a.payload; });
  },
});

export default analyticsSlice.reducer;
