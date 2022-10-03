import React, { useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
import styled from "styled-components";
import Background from "../../components/ui/Background";
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
          sessionStorage.setItem("RefreshToken", res.headers.refresh_hh);
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
    <Background>
      <main>
        <section>
          <h1 align="center">로그인</h1>
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

            <div>
              <div></div> <Button onClick={usesubmitHandler}>Login</Button>
              <Button type="button" onClick={signupHandler}>
                Create new account
              </Button>
            </div>
            <div>
              <img
                className="Button"
                src={kakaoLogo}
                alt="./Kakao.png"
                width="200"
                height="45"
              ></img>
            </div>
          </div>
        </section>
      </main>
    </Background>
  );
};
export default Login;

const InputWrapper = styled.div`
  display: grid;
  place-items: center;
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
