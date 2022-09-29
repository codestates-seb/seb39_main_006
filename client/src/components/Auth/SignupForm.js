import classes from "./LoginForm.module.css";
import { useState, useRef } from "react";
import { useDispatch } from "react-redux";
import axios from "axios";

const AuthForm = () => {
  const emailInputRef = useRef();
  const passwordInputRef = useRef();
  const displaynameInputRef = useRef();
  const [isLogin, setIsLogin] = useState(true);

  // const switchAuthModeHandler = () => {
  //   setIsLogin((prevState) => !prevState);
  // };

  const submitHandler = (event) => {
    event.preventDefault();

    const enteredEmail = emailInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;
    const enteredDisplayName = displaynameInputRef.current.value;
    let url;
    url = `${process.env.REACT_APP_URL}/api/members`;

    axios(url, {
      method: "POST",
      data: JSON.stringify({
        email: enteredEmail,
        password: enteredPassword,
        displayName: enteredDisplayName,
      }),
      headers: {
        "Content-Type": "application/json;charset=UTF-8",
        Accept: "application / json",
        "Content-Length": 90,
        Host: "localhost:8080",
      },
    })
      .then((res) => {
        if (res.ok) {
          // isLogin = true;
          return res.json();
        } else {
          return res.json().then((data) => {
            let errorMessage = "Authentication failed!";
            if (data && data.error && data.error.message) {
              errorMessage = data.error.message;
            }
            throw new Error("Network response was not ok.");
          });
        }
      })
      .then((data) => {})
      .catch((err) => {
        alert(err.message);
      });
  };

  return (
    <main className={classes.auth}>
      <section>
        <h1>회원가입</h1>
        <form onSubmit={submitHandler}>
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
          <div className={classes.control}>
            <label htmlFor="displayName">displayName</label>
            <input
              type="displayName"
              id="displayName"
              required
              ref={displaynameInputRef}
            />
          </div>
          <div className={classes.actions}>
            <button> Signup </button>
          </div>
        </form>
      </section>
    </main>
  );
};

export default AuthForm;
