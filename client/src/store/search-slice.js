import { createSlice } from "@reduxjs/toolkit";

const initialSearchState = {
	title: "",
	body: "",
	location: "",
	startDate: "",
	endDate: "",
};

const checkInput = (input) => {
	const regExp = /.*[\\`[\]{}].*/gi;
	if (input.matches(regExp)) {
		return input.replaceAll(regExp, "");
	}
	return input;
};

const searchSlice = createSlice({
	name: "search",
	initialState: initialSearchState,
	reducers: {
		setTitle(state, action) {
			const value = action.payload;
			state.title = checkInput(value);
		},
		setBody(state, action) {
			const value = action.payload;
			state.body = checkInput(value);
		},
		setLocation(state, action) {
			const value = action.payload;
			state.location = checkInput(value);
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
