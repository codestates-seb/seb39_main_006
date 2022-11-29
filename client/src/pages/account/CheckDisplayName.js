import axios from "axios";

const isDuplicateDisplayName = async (enteredDisplayName) => {
	// 지우지마요 bjh
	let result = true;

	await axios(
		`${process.env.REACT_APP_URL}/api/members/display-name?display_name=${enteredDisplayName}`
	)
		.then((res) => {
			if (res.status === 200) {
				result = false;
			}
		})
		.catch((err) => {
			if (err.response === undefined) {
			} else {
				result = true;
			}
		});

	return result;
};

export default isDuplicateDisplayName;
