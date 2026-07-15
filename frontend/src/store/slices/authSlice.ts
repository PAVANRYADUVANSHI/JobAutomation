import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { authService } from '../../services/jobService';

export const login = createAsyncThunk('auth/login', async ({ username, password }: { username: string; password: string }) => {
  const res = await authService.login(username, password);
  localStorage.setItem('token', res.data.token);
  return res.data;
});

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    token: localStorage.getItem('token'),
    username: null as string | null,
    loading: false,
    error: null as string | null,
  },
  reducers: {
    logout: (state) => {
      state.token = null;
      state.username = null;
      localStorage.removeItem('token');
    },
  },
  extraReducers: (b) => {
    b.addCase(login.pending, (s) => { s.loading = true; s.error = null; });
    b.addCase(login.fulfilled, (s, a) => { s.loading = false; s.token = a.payload.token; s.username = a.payload.username; });
    b.addCase(login.rejected, (s) => { s.loading = false; s.error = 'Invalid credentials'; });
  },
});

export const { logout } = authSlice.actions;
export default authSlice.reducer;
