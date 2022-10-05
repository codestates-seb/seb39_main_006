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

      if (err.response.status === 400) {
        if (err.response.data.fieldErrors) {
          alert(err.response.data.fieldErrors[0].reason);
        } else if (
          err.response.data.fieldErrors === null &&
          err.response.data.violationErrors
        ) {
          alert(err.response.data.violationErrors[0].reason);
        } else {
          alert(
            "우리도 무슨 오류인지 모르겠어요. 새로고침하고 다시 시도하세요...."
          );
        }
      } else {
        alert(err.response.data.korMessage);
      }
      window.location.reload();
    });

  return result;
};

export default isDuplicateDisplayName;
