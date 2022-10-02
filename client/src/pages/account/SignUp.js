import React, { useRef } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
import styled from "styled-components";

const Signup = () => {
  const emailInputRef = useRef();
  const passwordInputRef = useRef();
  const displaynameInputRef = useRef();

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

    const enteredEmail = emailInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;
    const enteredDisplayName = displaynameInputRef.current.value;

    if (enteredDisplayName.length < 2) {
      alert("닉네임을 2글자 이상 입력해 주세요 ");
      return;
    }

    if (validateEmail(enteredEmail) === null) {
      alert("이메일 형식으로 입력해 주세요");
      return;
    }

    if (enteredPassword.length < 6) {
      alert("비밀번호 6글자 이상 입력해주세요 ");
      return;
    }

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
          window.location.reload();
        }
      });
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
                />
              </div>
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
