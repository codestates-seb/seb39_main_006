import React, { useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
import styled from "styled-components";

// import AccountInfo from "./AccountInfo";
import kakaoLogo from "../../img/kakao.png";

const Login = () => {
  const [validateEmailText, setValidateEmailText] = useState("");
  const [validatePasswordText, setvalidatePasswordText] = useState("");

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
        if (err.response.status === 401) {
          alert("다시 확인하고 입력해주세요");
          window.location.reload();
        }
      });
  };
  return (
    <Wrap>
      <LoginPageContainer>
        <LoginContainer>
          <section align="center">
            <LoginText>로그인</LoginText>
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
                <div></div> <Button onClick={usesubmitHandler}>Login</Button>
                <Button type="button" onClick={signupHandler}>
                  Create new account
                </Button>
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
const Wrap = styled.div`
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
  color: #444444;
  font-weight: 700;
  margin-bottom: 33px;
  @media screen and (max-width: 500px) {
    font-size: 26px;
    margin-bottom: 25px;
  }
`;
const LoginContainer = styled.div`
  margin: 150px 0 250px 0;
  padding: 40px 50px 40px 50px;
  display: flex;
  flex-direction: column;
  max-width: 468px;
  width: 100%;
  height: 555px;
  background: #fbfbfb;
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
