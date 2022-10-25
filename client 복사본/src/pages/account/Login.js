import React, { useEffect, useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { SubmitBtn } from "../../components/ui/Button";
import styled from "styled-components";

// import AccountInfo from "./AccountInfo";
import kakaoLogo from "../../img/kakao.png";

const Login = () => {
  const [validateEmailText, setValidateEmailText] = useState("");
  const [validatePasswordText, setvalidatePasswordText] = useState("");

  const emailInputRef = useRef();
  const passwordInputRef = useRef();

  const navigate = useNavigate();

  useEffect(() => {
    if (sessionStorage.getItem("isLogin")) {
      navigate(`/main`);
    }
  }, []);
  const validateEmail = (email) => {
    return String(email)
      .toLowerCase()
      .match(
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      );
  };
  const signupHandler = () => {
    navigate("/signup");
  };
  const usesubmitHandler = async (event) => {
    event.preventDefault();

    const enteredEmail = emailInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;

    if (validateEmail(enteredEmail) === null) {
      setValidateEmailText("이메일 형식을 입력해 주세요");
      return;
    }

    if (enteredPassword.length < 6) {
      setvalidatePasswordText("비밀번호 6글자 이상 입력해주세요");
      return;
    }
    await axios(`${process.env.REACT_APP_URL}/api/members/login`, {
      method: "POST",
      headers: {
        Accept: "application/json",
      },
      data: {
        email: enteredEmail,
        password: enteredPassword,
      },
    })
      .then((res) => {
        if (res.status) {
          navigate("/main");
          sessionStorage.setItem("isLogin", true);
          sessionStorage.setItem("AccessToken", res.headers.access_hh);
          sessionStorage.setItem("userName", res.data.displayName);
          sessionStorage.setItem("memberId", res.data.memberId);
          window.location.reload();
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
  return (
    <Wrap>
      <LoginPageContainer>
        <LoginContainer>
          <section align="center">
            <LoginText>LOGIN</LoginText>
            <div>
              <InputWrapper>
                <form onSubmit={(e) => e.preventDefault()}>
                  <label htmlFor="email">Email</label>
                  <div className="container">
                    <input
                      className="input-tag possible"
                      type="email"
                      id="email"
                      required
                      ref={emailInputRef}
                    />
                  </div>
                  <p className="validate">{validateEmailText}</p>

                  <label htmlFor="password">Password</label>
                  <div className="container">
                    <input
                      className="input-tag"
                      type="password"
                      id="password"
                      required
                      ref={passwordInputRef}
                      name="password"
                      autoComplete="off"
                    />
                  </div>
                  <p className="validate">{validatePasswordText}</p>
                </form>
              </InputWrapper>

              <div align="center">
                <SubmitBtn onClick={usesubmitHandler}>Login</SubmitBtn>
                <NewBTN type="button" onClick={signupHandler}>
                  Create new account
                </NewBTN>
              </div>
              <div align="center">
                <a href="https://server.seb-006.shop/oauth2/authorization/kakao">
                  <img
                    className="Button"
                    src={kakaoLogo}
                    alt="./Kakao.png"
                    width="200"
                    height="45"
                  ></img>
                </a>
              </div>
            </div>
          </section>
        </LoginContainer>
      </LoginPageContainer>
    </Wrap>
  );
};
export default Login;
const NewBTN = styled.div`
  
`;
const Wrap = styled.div`
  
  
`;
const LoginPageContainer = styled.div`
  
`;
const LoginText = styled.div`
  
`;
const LoginContainer = styled.div`
  
`;
const InputWrapper = styled.div`
  
  
`;
