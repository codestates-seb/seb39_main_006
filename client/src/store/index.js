import { configureStore } from "@reduxjs/toolkit";
import searchReducer from "./search-slice";
import filterReducer from "./filter-slice";
import pageReducer from "./page-slice";

const store = configureStore({
  reducer: { search: searchReducer, filter: filterReducer, page: pageReducer },
});

export default store;
