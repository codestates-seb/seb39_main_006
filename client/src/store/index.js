import { configureStore } from "@reduxjs/toolkit";
import searchReducer from "./search-slice";
import filterReducer from "./filter-slice";

const store = configureStore({
  reducer: { search: searchReducer, filter: filterReducer },
});

export default store;
