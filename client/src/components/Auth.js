import React, { useState, useRef } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
// axios,toolkit으로 통일
// islogin 로직
// token 연결
const Auth = () => {
  const emailInputRef = useRef();
  const passwordInputRef = useRef();
  const displaynameInputRef = useRef();

  const navigate = useNavigate();

  const [isLogin, setIsLogin] = useState(true);

  const switchAuthModeHandler = () => {
    setIsLogin(!isLogin);
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
      }).then((res) => {
        if (res.status) navigate("/auth");
        sessionStorage.setItem("isLogin", true);
        console.log(res.headers);
        sessionStorage.setItem("AccesToken", res.headers.access_hh);
        sessionStorage.setItem("RefreshToken", res.headers.refresh_hh);
        window.location.reload();
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
      }).then((res) => {
        console.log(res);
      });
    }
  };
  return (
    <main>
      <section>
        <h1>{isLogin ? "로그인" : "회원가입"}</h1>
        <div>
          {isLogin ? (
            <>
              <div>
                <label htmlFor="email">Email</label>
                <input type="email" id="email" required ref={emailInputRef} />
              </div>
              <div>
                <label htmlFor="password">Password</label>
                <input
                  type="password"
                  id="password"
                  required
                  ref={passwordInputRef}
                />
              </div>
            </>
          ) : (
            <>
              <div>
                <label htmlFor="displayName">User Name</label>
                <input
                  type="displayName"
                  id="displayName"
                  required
                  ref={displaynameInputRef}
                />
              </div>
              <div>
                <label htmlFor="email">Email</label>
                <input type="email" id="email" required ref={emailInputRef} />
              </div>
              <div>
                <label htmlFor="password">Password</label>
                <input
                  type="password"
                  id="password"
                  required
                  ref={passwordInputRef}
                />
              </div>
            </>
          )}
          <div>
            <button onClick={usesubmitHandler}>
              {isLogin ? "Login" : "Create Account"}
            </button>
            <button type="button" onClick={switchAuthModeHandler}>
              {isLogin ? "Create new account" : "Login with existing account"}
            </button>
          </div>
        </div>
      </section>
    </main>
  );
};

export default Auth;
