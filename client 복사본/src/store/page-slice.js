import { createSlice } from "@reduxjs/toolkit";

const pageSlice = createSlice({
  name: "page",
  initialState: { page: 1 },
  reducers: {
    setPage(state, action) {
      const page = action.payload;
      state.page = page;
    },
  },
});

export const pageActions = pageSlice.actions;
export default pageSlice.reducer;
