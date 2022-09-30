import { createSlice } from "@reduxjs/toolkit";

const initialSearchState = {
  title: "",
  body: "",
  location: "",
  startDate: "",
  endDate: "",
};

const searchSlice = createSlice({
  name: "search",
  initialState: initialSearchState,
  reducers: {
    setTitle(state, action) {
      const value = action.payload;
      state.title = value;
    },
    setBody(state, action) {
      const value = action.payload;
      state.body = value;
    },
    setLocation(state, action) {
      const value = action.payload;
      state.location = value;
    },
    setStartDate(state, action) {
      const value = action.payload;
      state.startDate = value;
    },
    setEndDate(state, action) {
      const value = action.payload;
      state.endDate = value;
    },
  },
});

export const searchActions = searchSlice.actions;
export default searchSlice.reducer;
