import { useDispatch } from "react-redux";
import classes from "./Auth.module.css";
import { authActions } from "../store/auth";
import { useState, useRef } from "react";
import axios from "axios";
// axios,toolkit으로 통일
// islogin 로직
// token 연결
const Auth = () => {
  const displaynameInputRef = useRef();
  const emailInputRef = useRef();
  const passwordInputRef = useRef();

  const [isLogin, setIsLogin] = useState(true);
  const switchAuthModeHandler = () => {
    setIsLogin((prevState) => !prevState);
  };
  const usesubmitHandler = (event) => {
    event.preventDefault();

    const enteredEmail = emailInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;
    const enteredDisplayName = displaynameInputRef.current.value;

    if (isLogin) {
    } else {
      axios(`${process.env.REACT_APP_URL}/api/members`, {
        method: "POST",
        body: {
          email: enteredEmail,
          password: enteredPassword,
          displayName: enteredDisplayName,
        },
      });
    }
  };
  // dispatch(authActions.login());
  // const dispatch = useDispatch();
  return (
    <main className={classes.auth}>
      <section>
        <h1>{isLogin ? "로그인" : "회원가입"}</h1>
        <form onSubmit={usesubmitHandler}>
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
          <div className={classes.actions}>
            <button>{isLogin ? "Login" : "Create Account"}</button>
            <button
              type="button"
              className={classes.toggle}
              onClick={switchAuthModeHandler}
            >
              {isLogin ? "Create new account" : "Login with existing account"}
            </button>
          </div>
        </form>
      </section>
    </main>
  );
};

export default Auth;
