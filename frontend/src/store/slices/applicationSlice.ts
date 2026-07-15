import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { Application } from '../../types';
import { applicationService } from '../../services/jobService';

export const fetchQueue = createAsyncThunk('applications/fetchQueue', async () => {
  const res = await applicationService.queue();
  return res.data;
});

export const fetchAll = createAsyncThunk('applications/fetchAll', async (status?: string) => {
  const res = await applicationService.list(0, 50, status);
  return res.data.content;
});

export const updateStatus = createAsyncThunk('applications/updateStatus',
  async ({ id, status, notes }: { id: number; status: string; notes?: string }, { rejectWithValue }) => {
    try {
      const res = await applicationService.updateStatus(id, status, notes);
      return res.data;
    } catch (e: any) {
      return rejectWithValue(e?.response?.data?.message || 'Failed to update status');
    }
  });

export const triggerShortlist = createAsyncThunk('applications/shortlist', async () => {
  const res = await applicationService.shortlist();
  return res.data;
});

const appSlice = createSlice({
  name: 'applications',
  initialState: {
    queue: [] as Application[],
    all: [] as Application[],
    loading: false,
    error: null as string | null,
  },
  reducers: {},
  extraReducers: (b) => {
    b.addCase(fetchQueue.pending, (s) => { s.loading = true; });
    b.addCase(fetchQueue.fulfilled, (s, a) => { s.loading = false; s.queue = a.payload; });
    b.addCase(fetchQueue.rejected, (s) => { s.loading = false; s.error = 'Failed to load queue'; });
    b.addCase(fetchAll.fulfilled, (s, a) => { s.all = a.payload; });
    b.addCase(updateStatus.fulfilled, (s, a) => {
      const idx = s.all.findIndex(x => x.id === a.payload.id);
      if (idx >= 0) s.all[idx] = a.payload;
      // Only remove from queue if status is no longer SHORTLISTED
      if (a.payload.status !== 'SHORTLISTED') {
        s.queue = s.queue.filter(x => x.id !== a.payload.id);
      }
    });
    b.addCase(updateStatus.rejected, (s) => { s.loading = false; });
    b.addCase(triggerShortlist.fulfilled, (s, a) => { s.queue = [...s.queue, ...a.payload]; });
  },
});

export default appSlice.reducer;
