import React, { useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
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
    <main>
      <section>
        <div>
          <InputWrapper>
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
        </div>
      </section>
    </main>
  );
};
export default Signup;

const InputWrapper = styled.div`
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
`;
