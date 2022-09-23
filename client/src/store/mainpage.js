import { createSlice } from "@reduxjs/toolkit";
const initialMainPageState = {
  isMainPageState: "",
};

const mainpageSlice = createSlice({
  name: "mainpageslice",
  initialState: initialMainPageState,
  reducers: {
    nonClincked(state) {
      state.isMainPageState = "";
    },
    Clicked(state) {
      state.isMainPageState = button = { onClick };
    },
  },
});

export const authActions = authSlice.actions;
export default authSlice.reducer;
