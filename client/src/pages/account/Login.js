import React, { useEffect, useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { SubmitBtn } from "../../components/ui/Button";
import styled from "styled-components";

// import AccountInfo from "./AccountInfo";
import kakaoLogo from "../../img/kakao.png";
import googleLogo from "../../img/google.png"

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
              <div align="center">
                <a href="https://server.seb-006.shop/oauth2/authorization/google">
                  <img
                    className="Button"
                    src={googleLogo}
                    alt="./google.png"
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
  font-size: 1.4rem;
  margin: 15px 15px;
  font-weight: 700;
  color: #f2a25f;
  &:hover {
    color: #6d639e;
  }
`;
const Wrap = styled.div`
  /* overflow: hidden;
  /* width: 100vw;
  height: 100vh; */
  position: absolute;
  top: 170px;
  left: 50%;
  transform: translate(-50%, 0);
  box-sizing: border-box;
  #bgr {
    position: absolute;
    top: -600px;
    z-index: -995;
    opacity: 50%;
  }
`;
const LoginPageContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 5px 0 5px;

  @media screen and (max-width: 500px) {
    padding: 30px 25px 30px 25px;
    height: 455px;
  }
`;
const LoginText = styled.div`
  font-size: 32px;
  line-height: 37px;
  letter-spacing: 8px;
  margin-left: 8px;
  color: #444444;
  font-weight: 600;
  margin-bottom: 33px;
  @media screen and (max-width: 500px) {
    font-size: 26px;
    margin-bottom: 25px;
  }
`;
const LoginContainer = styled.div`
  /* margin: 150px 0 250px 0; */
  padding: 40px 50px 40px 50px;
  display: flex;
  flex-direction: column;
  max-width: 468px;
  width: 100%;
  height: 555px;
  background: rgba(255, 255, 255, 0.5);
  box-shadow: 0px 0px 30px rgba(0, 0, 0, 0.2);
  border-radius: 15px;
  font-family: Roboto;
  box-sizing: border-box;
  /* @media screen and (max-width: 500px) {
    padding: 30px 25px 30px 25px;
    height: 455px;
  } */
`;
const InputWrapper = styled.div`
  display: grid;
  /* place-items: center; */
  h1 {
    padding: 2rem;
  }

  .validate {
    color: red;
    padding: 0.5rem;
  }
  .container {
    display: grid;
    display: flex;
    place-items: center;
    flex-direction: column-reverse;
  }
  .input-tag {
    display: block;
    text-align: left;
    font-size: 1em;
  }
  label {
    font-weight: 600;
  }
  input {
    padding: 15px 10px;
    border-radius: 5px;
    outline: none;
    width: 20rem;
    display: inline-block;
    /* border: none; */
    border-radius: 5px;
    box-sizing: border-box;
    background-color: rgba(255, 255, 255, 0.7);
    /* margin-right: 10%; */
    margin: 5px auto;
    border: 2px solid #8fc9e04b;
    &:focus {
      background-color: white;
      border: 2px solid #5e98ae4b;
    }
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
