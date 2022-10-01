import React, { useRef } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
import styled from "styled-components";

const Login = () => {
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
  const usesubmitHandler = (event) => {
    event.preventDefault();

    const enteredEmail = emailInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;

    if (validateEmail(enteredEmail) === null) {
      alert("이메일 형식으로 입력해 주세요");
      return;
    }

    if (enteredPassword.length < 6) {
      alert("비밀번호 6글자 이상 입력해주세요 ");
      return;
    }
    axios(`${process.env.REACT_APP_URL}/api/members/login`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Length": 61,
      },
      data: {
        email: enteredEmail,
        password: enteredPassword,
      },
    })
      .then((res) => {
        if (res.status) {
          navigate("/login");
          sessionStorage.setItem("isLogin", true);
          sessionStorage.setItem("AccessToken", res.headers.access_hh);
          sessionStorage.setItem("RefreshToken", res.headers.refresh_hh);
          sessionStorage.setItem("userName", res.data.displayName);
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
    <main>
      <section>
        <h1>로그인</h1>
        <div>
          <InputWrapper>
            <form onSubmit={(e) => e.preventDefault()}>
              <label htmlFor="email">Email</label>
              <div className="container">
                <input
                  className="input-tag"
                  type="email"
                  id="email"
                  required
                  ref={emailInputRef}
                />
              </div>
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
            </form>
          </InputWrapper>

          <div>
            <Button onClick={usesubmitHandler}>Login</Button>
            <Button type="button" onClick={signupHandler}>
              "Create new account"
            </Button>
          </div>
        </div>
      </section>
    </main>
  );
};
export default Login;

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
