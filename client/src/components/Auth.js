import { useDispatch, useSelector } from "react-redux";
import classes from "./Auth.module.css";
import { authActions } from "../store/auth";
import { useState, useRef } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
// axios,toolkit으로 통일
// islogin 로직
// token 연결
const Auth = () => {
  const emailInputRef = useRef();
  const passwordInputRef = useRef();
  const displaynameInputRef = useRef();

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const isAuth = useSelector((state) => state.auth.isAuthenticated);

  const [isLogin, setIsLogin] = useState(true);

  const switchAuthModeHandler = () => {
    setIsLogin(!isLogin);
  };

  const usesubmitHandler = (event) => {
    event.preventDefault();
    dispatch(authActions.login());
    if (isAuth) {
    }

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
        window.location.reload();
      });
    } else {
      const enteredDisplayName = displaynameInputRef.current.value;
      console.log(enteredPassword);
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
  // dispatch(authActions.login());
  // const dispatch = useDispatch();
  return (
    <main className={classes.auth}>
      <section>
        <h1>{isLogin ? "로그인" : "회원가입"}</h1>
        <div>
          {isLogin ? (
            <>
              <div className={classes.control}>
                <label htmlFor="email">Email</label>
                <input type="email" id="email" required ref={emailInputRef} />
              </div>
              <div className={classes.control}>
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
              <div className={classes.control}>
                <label htmlFor="displayName">User Name</label>
                <input
                  type="displayName"
                  id="displayName"
                  required
                  ref={displaynameInputRef}
                />
              </div>
              <div className={classes.control}>
                <label htmlFor="email">Email</label>
                <input type="email" id="email" required ref={emailInputRef} />
              </div>
              <div className={classes.control}>
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
          <div className={classes.actions}>
            <button onClick={usesubmitHandler}>
              {isLogin ? "Login" : "Create Account"}
            </button>
            <button
              type="button"
              className={classes.toggle}
              onClick={switchAuthModeHandler}
            >
              {isLogin ? "Create new account" : "Login with existing account"}
            </button>
          </div>
        </div>
      </section>
    </main>
  );
};

export default Auth;
