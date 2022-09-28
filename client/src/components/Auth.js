import React, { useState, useRef } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Button from "../components/ui/Button";
import styled from "styled-components";

// token 연결
const Auth = () => {
  const emailInputRef = useRef();
  const passwordInputRef = useRef();
  const displaynameInputRef = useRef();

  const navigate = useNavigate();

  const [isLogin, setIsLogin] = useState(true);

  const switchAuthModeHandler = () => {
    setIsLogin(!isLogin);
    if (isLogin) {
      emailInputRef.current.value = "";
      passwordInputRef.current.value = "";
    } else {
      emailInputRef.current.value = "";
      passwordInputRef.current.value = "";
      displaynameInputRef.current.value = "";
    }
  };

  const usesubmitHandler = (event) => {
    event.preventDefault();

    const enteredEmail = emailInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;

    if (isLogin) {
      // fetch(`${process.env.REACT_APP_URL}/api/members/login`, {
      //   method: "POST",
      //   headers: {
      //     "Content-Type": "application/json",
      //   },
      //   body: JSON.stringify({
      //     email: enteredEmail,
      //     password: enteredPassword,
      //   }),
      // }).then((res) => console.log(res.headers.Access_HH));
      axios(`${process.env.REACT_APP_URL}/api/members/login`, {
        method: "POST",
        data: {
          email: enteredEmail,
          password: enteredPassword,
        },
      })
        .then((res) => {
          if (res.status) navigate("/auth");
          sessionStorage.setItem("isLogin", true);
          sessionStorage.setItem("AccessToken", res.headers.access_hh);
          sessionStorage.setItem("RefreshToken", res.headers.refresh_hh);
          sessionStorage.setItem("userName", res.data.displayName);
          window.location.reload();
        })
        .catch((err) => {
          if (err.response.status === 401) {
            alert("다시 확인하고 입력해주세요");
            window.location.reload();
          }
        });
    } else {
      const enteredDisplayName = displaynameInputRef.current.value;
      axios(`https://seb-006.shop/api/members`, {
        method: "POST",
        data: {
          email: enteredEmail,
          password: enteredPassword,
          displayName: enteredDisplayName,
        },
      });
    }
  };
  return (
    <main>
      <section>
        <h1>{isLogin ? "로그인" : "회원가입"}</h1>
        <div>
          {isLogin ? (
            <InputWrapper>
              <div className="container">
                <label htmlFor="email">Email</label>
                <input
                  className="input-tag"
                  type="email"
                  id="email"
                  required
                  ref={emailInputRef}
                />
              </div>
              <div className="container">
                <label htmlFor="password">Password</label>
                <input
                  className="input-tag"
                  type="password"
                  id="password"
                  required
                  ref={passwordInputRef}
                />
              </div>
            </InputWrapper>
          ) : (
            <InputWrapper>
              <div className="container">
                <label htmlFor="displayName">User Name</label>
                <input
                  type="displayName"
                  id="displayName"
                  required
                  ref={displaynameInputRef}
                />
              </div>
              <div className="container">
                <label htmlFor="email">Email</label>
                <input
                  className="input-tag"
                  type="email"
                  id="email"
                  required
                  ref={emailInputRef}
                />
              </div>
              <div className="container">
                <label htmlFor="password">Password</label>
                <input
                  className="input-tag"
                  type="password"
                  id="password"
                  required
                  ref={passwordInputRef}
                />
              </div>
            </InputWrapper>
          )}
          <div>
            <Button onClick={usesubmitHandler}>
              {isLogin ? "Login" : "Create Account"}
            </Button>
            <Button type="button" onClick={switchAuthModeHandler}>
              {isLogin ? "Create new account" : "Login with existing account"}
            </Button>
          </div>
        </div>
      </section>
    </main>
  );
};

export default Auth;
const InputWrapper = styled.div`
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
