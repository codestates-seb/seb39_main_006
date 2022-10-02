import axios from "axios";

const isDuplicateDisplayName = (enteredDisplayName) => {
  //닉네임을 입력한다.
  //중복확인 버튼을 누른다.
  // 중복확인 Req 날아간다. 응답코드 === 409 일 경우, 비활성화 되어야함
  // if (res === 200 ){
  // 다음 입력칸이 활성화되어야함.
  //}

  axios(
    `${process.env.REACT_APP_URL}/api/members/display-name?display_name=${enteredDisplayName}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json;charset=UTF-8",
        Accept: "application/json",
      },
    }
  )
    .then((res) => {
      if (res.status === 200) {
        return false;
      }
    })
    .catch((err) => {
      if (err.response.status === 401) {
        alert("다시 확인하고 입력해주세요");
      }
    });

  return true;
};

export default isDuplicateDisplayName;
