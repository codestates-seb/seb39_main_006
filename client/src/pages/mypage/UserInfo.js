import React, { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import styled from "styled-components";
import CheckDisplayName from "../account/CheckDisplayName";
import H1 from "../../components/ui/H1";

const Userinfo = () => {
  const [isDisabledInfo, setIsDisabledInfo] = useState(false);
  const [imageURL, setImageURL] = useState("");
  const [userInfo, setUserInfo] = useState("");

  const displaynameInputRef = useRef();
  const passwordInputRef = useRef();
  const phoneInputRef = useRef();
  const contentInputRef = useRef();
  const profileuploadInputRef = useRef();

  // displaynameInputRef.current.value = accountInfo.displayName;
  //#region Show validate text
  const [validateDisplayNameText, setValidateDisplayNameText] = useState("");
  const [validatePasswordText, setValidatePasswordText] = useState("");
  const [validatePhoneNumberText, setValidatePhoneNumberText] = useState("");
  //#endregion

  //#region Change validate className
  const [
    validateDisplayNameNoticeClassname,
    setValidateDisplayNameNoticeClassname,
  ] = useState("validate");
  const [validatePasswordNoticeClassname, setValidatePasswordNoticeClassname] =
    useState("validate");
  const [
    validatePhoneNumberNoticeClassname,
    setValidatePhoneNumberNoticeClassname,
  ] = useState("validate");
  //#endregion

  //#region Check validate&Duplicate functions
  const validatePhoneNumber = (phoneNumber) => {
    return String(phoneNumber).match(/^\d{3}-\d{3,4}-\d{4}$/);
  };

  const validatePassword = (password) => {
    return String(password).match(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/);
  };

  const onClickDuplicateDisplayName = async () => {
    const enteredDisplayName = displaynameInputRef.current.value;
    if (enteredDisplayName.length > 1) {
      const isDuplicateDisplayName = await CheckDisplayName(
        displaynameInputRef.current.value
      );

      setIsDisabledInfo(isDuplicateDisplayName);

      if (isDuplicateDisplayName) {
      }
    } else {
      setValidateDisplayNameText("❌ 닉네임을 2글자 이상 입력해 주세요 ");
      setValidateDisplayNameNoticeClassname("validate");
    }
  };
  //#endregion

  //#region OnChanged event for validate
  const onChangedDisplayName = (e) => {
    if (e.target.value.length < 2) {
      setValidateDisplayNameText("❌ 닉네임을 2글자 이상 입력해 주세요 ");
      setValidateDisplayNameNoticeClassname("validate");
    } else {
      setValidateDisplayNameText("✅ 올바른 닉네임 형식입니다.");
      setValidateDisplayNameNoticeClassname("HTH-green");
    }
  };

  const onChangedPassword = (e) => {
    const enteredPassword = e.target.value;
    if (validatePassword(enteredPassword) === null) {
      setValidatePasswordText(
        "❌ 숫자,문자로 구성된 비밀번호 6글자 이상 입력해주세요"
      );
      setValidatePasswordNoticeClassname("validate");
    } else {
      setValidatePasswordText("✅ 올바른 비밀번호 형식입니다.");
      setValidatePasswordNoticeClassname("HTH-green");
    }
  };

  const onChangedPhoneNumber = (e) => {
    if (validatePhoneNumber(e.target.value) === null) {
      setValidatePhoneNumberText("❌ 010-0000-0000 형식으로 입력해 주세요");
      setValidatePhoneNumberNoticeClassname("validate");
    } else {
      setValidatePhoneNumberText("✅ 올바른 번호 형식입니다.");
      setValidatePhoneNumberNoticeClassname("HTH-green");
    }
  };

  const onChangedContent = (e) => {
    // setContentText(e.target.value);
  };
  //#endregion

  // if (accountInfo.phone !== null) {
  //   // setPhoneNumberText(accountInfo.phone);
  // }
  // if (accountInfo.content !== null) {
  //   // setContentText(accountInfo.content);
  // }

  const postImg = (e) => {
    const formData = new FormData();
    formData.append("image", e.target.files[0]);
    axios(`${process.env.REACT_APP_URL}/api/images/upload`, {
      method: "POST",
      headers: {
        "Content-Type": "multipart/form-data",
        access_hh: sessionStorage.getItem("AccessToken"),
      },
      data: formData,
    })
      .then((res) => {
        // 기홍님의 잔재.....
        // let testid = res.data.imageId;
        // setImageId(testid);
        // mount.current = true;
        setImageURL(res.data.imageUrl);
      })
      .catch((err) => {
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
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        } else if (err.response.status === 0)
          alert(
            "서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
          );
        else {
          if (
            err.response.data.korMessage ===
            "만료된 토큰입니다. 다시 로그인 해주세요."
          ) {
            sessionStorage.clear();
            navigate(`/`);
            window.location.reload();
          } else if (err.response.data.korMessage) {
            alert(err.response.data.korMessage);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        }
      });
  };

  const usesubmitHandler = (event) => {
    event.preventDefault();

    const enteredDisplayName = displaynameInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;
    const enteredPhone = phoneInputRef.current.value;
    const enteredContent = contentInputRef.current.value;

    if (
      validatePasswordNoticeClassname === "validate" ||
      validatePhoneNumberNoticeClassname === "validate"
    ) {
      alert("서식을 확인해 주세요");
      return;
    }

    axios(`${process.env.REACT_APP_URL}/api/members?`, {
      method: "PATCH",
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
      data: {
        displayName: enteredDisplayName,
        password: enteredPassword,
        phone: enteredPhone,
        content: enteredContent,
        //TODO useState 로 이미지 url을 받아온다
        profileImage: imageURL,
      },
    })
      .then((res) => {
        if (res.status) {
          alert("정상적으로 수정되었습니다");
          navigate("/mypage");
        }
      })
      .catch((err) => {
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
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        } else if (err.response.status === 0)
          alert(
            "서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
          );
        else {
          if (
            err.response.data.korMessage ===
            "만료된 토큰입니다. 다시 로그인 해주세요."
          ) {
            sessionStorage.clear();
            navigate(`/`);
            window.location.reload();
          } else if (err.response.data.korMessage) {
            alert(err.response.data.korMessage);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        }
      });
  };
  const navigate = useNavigate();

  const memberId = sessionStorage.getItem("memberId");

  useEffect(() => {
    axios
      .get(`${process.env.REACT_APP_URL}/api/members/${memberId}`, {
        headers: {
          access_hh: sessionStorage.getItem("AccessToken"),
        },
      })
      .then((res) => {
        setValidatePhoneNumberNoticeClassname("HTH-green");
        setUserInfo(res.data);
      })
      .catch((err) => {
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
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        } else if (err.response.status === 0)
          alert(
            "서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
          );
        else {
          if (
            err.response.data.korMessage ===
            "만료된 토큰입니다. 다시 로그인 해주세요."
          ) {
            sessionStorage.clear();
            navigate(`/`);
            window.location.reload();
          } else if (err.response.data.korMessage) {
            alert(err.response.data.korMessage);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        }
        window.location.reload();
      });
  }, []);

  return (
    <>
      <H1>userinfo Edit</H1>

      <main>
        <section>
          <PageContainer>
            <ContainerWrap>
              <InputWrapper>
                <form onSubmit={(e) => e.preventDefault()}>
                  <div className="label-wrapper">
                    <label htmlFor="displayName">User Name</label>
                    <div className="container">
                      <input
                        type="displayName"
                        id="displayName"
                        required
                        ref={displaynameInputRef}
                        onChange={onChangedDisplayName}
                        defaultValue={
                          userInfo !== null ? userInfo.displayName : ""
                        }
                      />
                    </div>
                    <button onClick={onClickDuplicateDisplayName}>
                      중복확인
                    </button>
                    <p className={validateDisplayNameNoticeClassname}>
                      {validateDisplayNameText}
                    </p>

                    <label htmlFor="password">Password</label>
                    <div className="container">
                      <input
                        disabled={isDisabledInfo}
                        className="input-tag"
                        type="password"
                        id="password"
                        required
                        ref={passwordInputRef}
                        name="password"
                        autoComplete="off"
                        onChange={onChangedPassword}
                      />
                    </div>
                    <p className={validatePasswordNoticeClassname}>
                      {validatePasswordText}
                    </p>

                    <label htmlFor="phone">Phone Number</label>
                    <div className="container">
                      <input
                        disabled={isDisabledInfo}
                        className="input-tag"
                        type="phone"
                        id="phone"
                        required
                        ref={phoneInputRef}
                        defaultValue={userInfo !== null ? userInfo.phone : ""}
                        onChange={onChangedPhoneNumber}
                        // value={phoneNumberText}
                      />
                    </div>
                    <p className={validatePhoneNumberNoticeClassname}>
                      {validatePhoneNumberText}
                    </p>

                    <label htmlFor="content">자기소개</label>
                    <div className="container">
                      <input
                        disabled={isDisabledInfo}
                        className="input-tag"
                        type="textarea"
                        id="content"
                        required
                        ref={contentInputRef}
                        defaultValue={userInfo !== null ? userInfo.content : ""}
                        onChange={onChangedContent}
                      />
                    </div>

                    <label htmlFor="content">프로필 사진 업로드</label>
                    <div className="container">
                      <input
                        className="input-tag"
                        type="file"
                        id="profile-upload"
                        accept="image/*"
                        required
                        multiple="multiple"
                        onChange={postImg}
                        ref={profileuploadInputRef}
                      />
                    </div>
                    <button onClick={usesubmitHandler}>수정하기</button>
                    <button onClick={() => navigate("/mypage")}>취소</button>
                  </div>
                </form>
              </InputWrapper>
            </ContainerWrap>
          </PageContainer>
        </section>
      </main>
    </>
  );
};

export default Userinfo;
const PageContainer = styled.div`
  h2 {
    font-size: 2rem;
    font-weight: 500;
    color: #444;
  }
  width: 70%;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 5px 0 5px;

  @media screen and (max-width: 500px) {
    padding: 30px 25px 30px 25px;
    height: 700px;
  }
`;
const ContainerWrap = styled.div`
  #author {
    color: darkblue;
    font-weight: 600;
    font-size: 1.3rem;
  }
  .author {
    font-size: 1.25rem;
  }
  button {
    place-items: center;
    font-size: 1.25rem;
    background-color: #dabbc9;
    max-width: 1000px;
    width: fit-content;
    border: 1px solid #dabbc9;
    padding: 0.5rem 1rem;
    margin: 0.5rem;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
    color: #425049;
    &:hover {
      background-color: #efd5c8;
      border-color: #efd5c8;
    }
  }
  .contents {
    padding-left: 1rem;
  }
  margin: 150px 0 250px 0;
  padding: 40px 50px 40px 50px;
  display: flex;
  flex-direction: column;
  max-width: 1650px;
  width: 170%;
  height: 900px;
  background-color: beige;

  box-shadow: 0px 0px 11px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  font-family: Roboto;
  box-sizing: border-box;
  @media screen and (max-width: 500px) {
    padding: 30px 25px 30px 25px;
    height: 455px;
  }
`;
const InputWrapper = styled.div`
  .in-line {
    width: 350px;
    height: 40px;
  }
  input {
    margin: 0;
  }
  input[type="text"] {
    width: 70%;
    height: 100%;
    border: none;
    font-size: 1em;
    padding-left: 5px;
    font-style: oblique;
    display: inline;
    outline: none;
    box-sizing: border-box;
    color: black;
  }
  input[type="button"] {
    width: 30%;
    height: 100%;
    background-color: lightgray;
    border: none;
    background-color: white;
    font-size: 1em;
    color: #042aac;
    outline: none;
    display: inline;
    margin-left: -10px;
    box-sizing: border-box;
  }
  input[type="button"]:hover {
    background-color: lightgray;
  }

  button {
    display: grid;
    grid: auto / repeat(auto-fit, minmax(100px, auto));
    place-items: center;
    font-size: 1.25rem;
    background-color: #dabbc9;
    width: fit-content;
    border: 1px solid #dabbc9;
    padding: 0.5rem 1rem;
    margin: 0.5rem;
    margin-left: 60%;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
    color: #425049;
    &:hover {
      background-color: #efd5c8;
      border-color: #efd5c8;
    }
  }
  align-items: center;
  margin-left: 18%;
  label {
    margin: 1rem;
    font-weight: 400;
  }
  input {
    margin-left: 3rem;
  }
  .validate {
    color: red;
    padding: 0.5rem;
  }
  .container {
    display: flex;
    flex-direction: column-reverse;
  }
  .input-tag {
    display: block;
    text-align: left;
    font-size: 1em;
  }
  input {
    padding: 20px 10px;
    border-radius: 5px;
    outline: none;
    width: 30rem;
    display: inline-block;
    border: none;
    border-radius: 5px;
    margin-right: 10%;
    border: 1.5px solid #a19f9f;
  }
  label {
    font-size: large;
    line-height: normal;
    padding: 0.5rem;
  }
  #none + label {
    color: #a19f9f;
  }
  #hover:hover {
    border: 1.5px solid black;
  }
  #hover:hover + label {
    color: black;
  }
  #focus:focus + label {
    color: #2962ff;
  }
  #focus:focus {
    border: 1.5px solid #2962ff;
  }
  .content {
  }
`;
