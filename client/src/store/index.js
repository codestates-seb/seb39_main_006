//redux 로직
import { configureStore } from "@reduxjs/toolkit";

import authSlice from "./auth";

const store = configureStore({
  reducer: { auth: authSlice.reducer },
});

export default store;
