import { configureStore } from "@reduxjs/toolkit";
import searchReducer from "./search-slice";
import filterReducer from "./filter-slice";
import pageReducer from "./page-slice";
// 새로고침 유지
import storage from "redux-persist/lib/storage";
import { combineReducers } from "@reduxjs/toolkit";
// 쿼리파라미터 알아보자
import { persistReducer } from "redux-persist";
import thunk from "redux-thunk";

const reducers = combineReducers({
  search: searchReducer,
  filter: filterReducer,
  page: pageReducer,
});

const persistConfig = {
  key: "root",
  storage,
};

const persistedReducer = persistReducer(persistConfig, reducers);

const store = configureStore({
  reducer: persistedReducer,
  devTools: process.env.NODE_ENV !== "production",
  middleware: [thunk],
});

export default store;
