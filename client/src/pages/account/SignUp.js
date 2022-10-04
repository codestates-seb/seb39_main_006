import React, { useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
import styled from "styled-components";
import imgBgr from "../../img/flower4.jpeg";
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
      }
    } else {
      setValidateDisplayNameText("❌ 닉네임을 2글자 이상 입력해 주세요 ");
      setValidateDisplayNameNoticeClassname("validate");
    }
  };

  const displaynameInputRef = useRef();
  const emailInputRef = useRef();
  const passwordInputRef = useRef();

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
        if (err.response.status === 401) {
          alert("다시 확인하고 입력해주세요");
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
      setValidatePasswordText("❌ 비밀번호 6글자 이상 입력해주세요");
      setValidatePasswordNoticeClassname("validate");
    } else {
      setValidatePasswordText("✅ 올바른 비밀번호 형식입니다.");
      setValidatePasswordNoticeClassname("HTH-green");
    }
  };

  return (
    <Wrap>
      <img
        id="bgr"
        src={imgBgr}
        alt="./flower4.jpeg"
        width="2000"
        height="2000"
      />
      <section>
        <SignUpPageContainer>
          <SignUpContainer>
            <InputWrapper>
              <SignUpText>회원가입</SignUpText>
              <form onSubmit={(e) => e.preventDefault()}>
                <label htmlFor="displayName">User Name</label>
                <div className="container">
                  <input
                    type="displayName"
                    id="displayName"
                    required
                    ref={displaynameInputRef}
                    onChange={onChangeInputDisplayName}
                  />
                </div>
                <Button onClick={onClickDuplicateDisplayName}>중복확인</Button>
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
                </div>
                <p className={validateEmailNoticeClassname}>
                  {validateEmailText}
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
                    onChange={onChangeInputPassword}
                  />
                </div>
                <p className={validatePasswordNoticeClassname}>
                  {validatePasswordText}
                </p>
              </form>
            </InputWrapper>
            <div>
              <Button onClick={usesubmitHandler}>Create Account</Button>
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
  #bgr {
    position: absolute;
    top: -600px;
    z-index: -995;
    opacity: 50%;
  }
`;
const SignUpPageContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 5px 0 5px;
  input:-webkit-autofill,
  input:-webkit-autofill:focus {
    transition: background-color 600000s 0s, color 600000s 0s;
  }
  @media screen and (max-width: 500px) {
    padding: 30px 25px 30px 25px;
    height: 600px;
  }
`;
const SignUpText = styled.div`
  font-size: 32px;
  line-height: 37px;
  color: #444444;
  font-weight: 700;
  margin-bottom: 33px;
  @media screen and (max-width: 600px) {
    font-size: 26px;
    margin-bottom: 25px;
  }
`;
const SignUpContainer = styled.div`
  margin: 150px 0 250px 0;
  padding: 40px 50px 40px 50px;
  display: flex;
  flex-direction: column;
  max-width: 468px;
  width: 100%;
  height: 650px;
  background: #fbfbfb;
  box-shadow: 0px 0px 11px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  font-family: Roboto;
  box-sizing: border-box;
  @media screen and (max-width: 500px) {
    padding: 30px 25px 30px 25px;
    height: 700px;
  }
`;
const InputWrapper = styled.div`
  /* align-items: center; */
  .validate {
    color: red;
    padding: 0.5rem;
  }
  .HTH-green {
    color: green;
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
    width: 20rem;
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
`;
