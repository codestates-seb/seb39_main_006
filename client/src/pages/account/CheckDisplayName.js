import axios from "axios";

const isDuplicateDisplayName = async (enteredDisplayName) => {
  // 지우지마요 bjh
  let result = true;

  await axios(
    `${process.env.REACT_APP_URL}/api/members/display-name?display_name=${enteredDisplayName}`
  )
    .then((res) => {
      if (res.status === 200) {
        console.log("200");
        result = false;
      }
    })
    .catch((err) => {
      // console.log("err");
      // console.log(err.response);

      if (err.response === undefined) {
      } else {
        // console.log(err.response.data);
        // console.log(err.response.status);
        // console.log(err.response.headers);
        result = true;
      }
    });

  return result;
};

export default isDuplicateDisplayName;
