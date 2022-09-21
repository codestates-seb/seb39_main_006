//redux 로직
import { configureStore } from "@reduxjs/toolkit";

import authReducer from "./auth";

const store = configureStore({
  reducer: { auth: authReducer.reducer },
});

export default store;
