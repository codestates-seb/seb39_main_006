import React, { useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Button, { ValidateBtn, SignupBtn } from "../../components/ui/Button";
import styled from "styled-components";

import CheckDisplayName from "./CheckDisplayName";
// usestate 사용법
// 선언부  const [변수명, 함수이름] = useState(원하는 값 보통은 초기화)
// 사용부  함수이름(원하는값)

const Signup = () => {
  const [isDisabledInfo, setIsDisabledInfo] = useState(true);
  const [validateEmailText, setValidateEmailText] = useState("");
  const [validatePasswordText, setValidatePasswordText] = useState("");
  const [validateDisplayNameText, setValidateDisplayNameText] = useState("");
  const [
    validateDisplayNameNoticeClassname,
    setValidateDisplayNameNoticeClassname,
  ] = useState("validate");
  const [validateEmailNoticeClassname, setValidateEmailNoticeClassname] =
    useState("validate");
  const [validatePasswordNoticeClassname, setValidatePasswordNoticeClassname] =
    useState("validate");
  const [isEmailAuthorizing, setIsEmailAuthorizing] = useState(false);
  const [emailAuthCode, setEamilAuthCode] = useState("");
  const [validateAuthEmail, setValidateAuthEmail] = useState("validate");

  const onClickDuplicateDisplayName = async () => {
    const enteredDisplayName = displaynameInputRef.current.value;
    if (enteredDisplayName.length > 1) {
      const isDuplicateDisplayName = await CheckDisplayName(
        displaynameInputRef.current.value
      );

      setIsDisabledInfo(isDuplicateDisplayName);

      if (isDuplicateDisplayName) {
        emailInputRef.current.value = "";
        passwordInputRef.current.value = "";
        setValidateDisplayNameNoticeClassname("validate");
        setValidateDisplayNameText("아이디가 중복 됐습니다.");
      }
    } else {
      setValidateDisplayNameText("❌ 닉네임을 2글자 이상 입력해 주세요 ");
      setValidateDisplayNameNoticeClassname("validate");
    }
  };

  const onClickSendAuthCodeEmail = async () => {
    setIsEmailAuthorizing(true);
    await axios(
      `${process.env.REACT_APP_URL}/api/members/email?email=${emailInputRef.current.value}`
    )
      .then((res) => {
        if (res.status === 200) {
          setEamilAuthCode(res.data);
        }
      })
      .catch((err) => {
        setIsEmailAuthorizing(false);
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
          if (err.response.data.korMessage) {
            alert(err.response.data.korMessage);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        }
      });
  };

  const onClickAuthCodeEmail = () => {
    const realCode = emailAuthCode.code;
    const sdf = typeof onInputEmailCode.current.value;
    const asdf = typeof realCode;
    if (onInputEmailCode.current.value === realCode) {
      setValidateAuthEmail("success");
    } else {
      alert("인증 코드가 잘못 되었습니다.");
    }
  };

  const displaynameInputRef = useRef();
  const emailInputRef = useRef();
  const passwordInputRef = useRef();
  const onInputEmailCode = useRef();

  const navigate = useNavigate();

  const validateEmail = (email) => {
    return String(email)
      .toLowerCase()
      .match(
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      );
  };

  const loginHandler = () => {
    navigate("/");
  };

  const usesubmitHandler = (event) => {
    event.preventDefault();
    if (isDisabledInfo) {
      alert("회원가입을 할 수 없습니다 서식을 채워주세요");
      return;
    }

    const enteredEmail = emailInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;
    const enteredDisplayName = displaynameInputRef.current.value;

    axios(`${process.env.REACT_APP_URL}/api/members`, {
      method: "POST",
      data: {
        email: enteredEmail,
        password: enteredPassword,
        displayName: enteredDisplayName,
      },
    })
      .then((res) => {
        if (res.status) {
          alert("정상적으로 회원가입되었습니다. 로그인하여 진행해주세요.");
          navigate("/");
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
          if (err.response.data.korMessage) {
            alert(err.response.data.korMessage);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        }
      });
  };

  const onChangeInputDisplayName = (e) => {
    const enteredDisplayName = e.target.value;
    if (enteredDisplayName.length < 2) {
      setValidateDisplayNameText("❌ 닉네임을 2글자 이상 입력해 주세요 ");
      setValidateDisplayNameNoticeClassname("validate");
    } else {
      setValidateDisplayNameText("✅ 올바른 닉네임 형식입니다.");
      setValidateDisplayNameNoticeClassname("HTH-green");
    }
  };

  const onChangeInputEmail = (e) => {
    const enteredEmail = e.target.value;

    if (validateEmail(enteredEmail) === null) {
      setValidateEmailText("❌ 이메일 형식으로 입력해 주세요");
      setValidateEmailNoticeClassname("validate");
    } else {
      setValidateEmailText("✅ 올바른 이메일 형식입니다.");
      setValidateEmailNoticeClassname("HTH-green");
    }
  };

  const onChangeInputPassword = (e) => {
    const enteredPassword = e.target.value;
    if (enteredPassword.length < 6) {
      setValidatePasswordText(
        "❌숫자,문자로 구성된 비밀번호 6글자 이상 입력해주세요"
      );
      setValidatePasswordNoticeClassname("validate");
    } else {
      setValidatePasswordText("✅ 올바른 비밀번호 형식입니다.");
      setValidatePasswordNoticeClassname("HTH-green");
    }
  };

  return (
    <Wrap>
      <section>
        <SignUpPageContainer>
          <SignUpContainer>
            <InputWrapper>
              <SignUpText>SIGNUP</SignUpText>
              <form onSubmit={(e) => e.preventDefault()}>
                <label htmlFor="displayName">User Name</label>
                <div className="containerValidation">
                  <input
                    type="displayName"
                    id="displayName"
                    required
                    ref={displaynameInputRef}
                    onChange={onChangeInputDisplayName}
                  />

                  <ValidateBtn onClick={onClickDuplicateDisplayName}>
                    중복확인
                  </ValidateBtn>
                </div>
                <p className={validateDisplayNameNoticeClassname}>
                  {validateDisplayNameText}
                </p>
                <label htmlFor="email">Email</label>
                <div className="container">
                  <input
                    disabled={isDisabledInfo}
                    className="input-tag"
                    type="email"
                    id="email"
                    required
                    ref={emailInputRef}
                    onChange={onChangeInputEmail}
                  />
                  {validateEmailNoticeClassname === "validate" ? (
                    ""
                  ) : (
                    <div>
                      <Button onClick={onClickSendAuthCodeEmail}>
                        이메일 인증번호 받기
                      </Button>
                    </div>
                  )}{" "}
                </div>{" "}
                <p className={validateEmailNoticeClassname}>
                  {validateEmailText}
                </p>
                {isEmailAuthorizing ? (
                  <div>
                    <input
                      className="input-tag"
                      type="text"
                      id="emailAuth"
                      ref={onInputEmailCode}
                    />
                    <Button onClick={onClickAuthCodeEmail}>OK</Button>
                    {validateAuthEmail === "validate" ? (
                      ""
                    ) : (
                      <p className="HTH-green">인증 되었습니다.</p>
                    )}
                  </div>
                ) : (
                  ""
                )}
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
                    onChange={onChangeInputPassword}
                  />
                </div>
                <p className={validatePasswordNoticeClassname}>
                  {validatePasswordText}
                </p>
              </form>
            </InputWrapper>
            <div>
              <Button
                onClick={usesubmitHandler}
                disabled={validateAuthEmail === "validate" ? true : false}
              >
                Create Account
              </Button>
              <Button type="button" onClick={loginHandler}>
                Return to Login Page
              </Button>
            </div>
          </SignUpContainer>
        </SignUpPageContainer>
      </section>
    </Wrap>
  );
};
export default Signup;
const Wrap = styled.div`
 
`;
const SignUpPageContainer = styled.div`
 
`;
const SignUpText = styled.div`
 
`;
const SignUpContainer = styled.div`
 
`;
const InputWrapper = styled.div`
 
`;
