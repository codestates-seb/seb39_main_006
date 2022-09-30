import { createSlice } from "@reduxjs/toolkit";

const filterSlice = createSlice({
  name: "filter",
  initialState: { filterValue: "" },
  reducers: {
    setFilter(state, action) {
      const value = action.payload;
      state.filterValue = value;
    },
  },
});

export const filterActions = filterSlice.actions;
export default filterSlice.reducer;
