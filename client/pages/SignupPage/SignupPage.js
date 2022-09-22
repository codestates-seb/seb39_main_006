import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import classes from "./SignupPage.module.css";
import axios from "axios";
import { useHistory } from "react-router";

function SignupPage() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");

  const [isSubmitValid, setIsSubmitValid] = useState(false);
  const history = useHistory();
  const isValidInput = {
    isEmail: null,
    isPassword: null,
    isPasswordConfirm: null,
  };

  const [isCheckEmail, SetIsCheckEmail] = useState({
    isSuccess: false,
    isFail: false,
  });
  const [isCheckInput, SetIsCheckInput] = useState(isValidInput);

  const isCheckWeather = {
    sunny: false,
    cloud: false,
    rain: false,
    snow: false,
  };

  const [isWeather, setIsWeather] = useState(isCheckWeather);
  const weatherCheckHandle = (e) => {
    let newWeather = { ...isWeather };
    if (e.target.id === "0") {
      newWeather.sunny = !newWeather.sunny;
      setIsWeather(newWeather);
    }
    if (e.target.id === "1") {
      newWeather.cloud = !newWeather.cloud;
      setIsWeather(newWeather);
    }
    if (e.target.id === "2") {
      newWeather.rain = !newWeather.rain;
      setIsWeather(newWeather);
    }
    if (e.target.id === "3") {
      newWeather.snow = !newWeather.snow;
      setIsWeather(newWeather);
    }
  };

  const inputValueHandle = (e) => {
    if (e.target.id === "name") {
      setName(e.target.value);
    } else if (e.target.id === "email") {
      setEmail(e.target.value);
    } else if (e.target.id === "password") {
      setPassword(e.target.value);
    } else if (e.target.id === "passwordConfirm") {
      setPasswordConfirm(e.target.value);
    }
  };

  useEffect(() => {
    if (
      name.length >= 2 &&
      isCheckInput.isEmail &&
      isCheckInput.isPassword &&
      isCheckInput.isPasswordConfirm
    ) {
      setIsSubmitValid(true);
    } else {
      setIsSubmitValid(false);
    }
  }, [isCheckInput, name]);

  const inputValidHandle = (e) => {
    let isNewCheckInput = { ...isCheckInput };
    if (e.target.id === "email") {
      isNewCheckInput.isEmail = emailHandle(e.target.value);
      SetIsCheckInput(isNewCheckInput);
    } else if (e.target.id === "password") {
      isNewCheckInput.isPassword = passwordHandle(e.target.value);
      SetIsCheckInput(isNewCheckInput);
    } else if (e.target.id === "passwordConfirm") {
      isNewCheckInput.isPasswordConfirm = isPasswordConfirmHandle(
        e.target.value
      );
      SetIsCheckInput(isNewCheckInput);
    }
  };

  const emailHandle = (email) => {
    if (!email.length) {
      return null;
    }
    let check = email.match(
      /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i
    );

    return email.indexOf(".") !== -1 && check ? true : false;
  };

  const passwordHandle = (password) => {
    if (!password.length) {
      return null;
    }

    let check = password.match(
      /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/
    );
    return password.length > 7 && password.length < 17 && check ? true : false;
  };

  const isPasswordConfirmHandle = (passwordConfirm) => {
    if (!passwordConfirm.length) {
      return null;
    }
    return password === passwordConfirm ? true : false;
  };

  const editInfohandler = (e) => {
    let arrEditWeather = [];
    if (isWeather.sunny) {
      arrEditWeather.push(0);
    }
    if (isWeather.cloud) {
      arrEditWeather.push(1);
    }
    if (isWeather.rain) {
      arrEditWeather.push(2);
    }
    if (isWeather.snow) {
      arrEditWeather.push(3);
    }
    if (password === passwordConfirm) {
    }
    handlesignin(e, arrEditWeather);
  };

  const handlesignin = (e, arrEditWeather) => {
    axios
      .post(
        `${process.env.REACT_APP_API_URL}/user/signup`,
        {
          name: name,
          email: email,
          password: password,
          weather: arrEditWeather,
        },
        { withCredentials: true }
      )

      .then((res) => {
        if (res.status === 201) {
          // 메인 페이지로 이동
          history.push("/login");
        }
      })
      .catch((error) => console.log("Error", error.message));
  };

  const handleConfirmEmail = (e) => {
    axios
      .get(`${process.env.REACT_APP_API_URL}/user/email?email=${email}`, {
        withCredentials: true,
      })
      .then((res) => {
        if (res.status === 200) {
          isCheckEmail.isSuccess = true;
          isCheckEmail.isFail = false;
          SetIsCheckEmail({ ...isCheckEmail });
        }
      })
      .catch((error) => {
        isCheckEmail.isSuccess = false;
        isCheckEmail.isFail = true;
        SetIsCheckEmail({ ...isCheckEmail });
        console.log("Error", error.message);
      });
  };

  return (
    <div className="signupage-container">
      <div className="signup-container">
        <h1 className="title">회원가입</h1>
        <div className="name-container">
          <span>이름</span>
          <input id="name" onChange={(e) => inputValueHandle(e)} />
        </div>
        <div className="emmail-container">
          <span>이메일</span>
          <div className="email-inputBox">
            <input
              id="email"
              onChange={(e) => inputValueHandle(e)}
              onKeyUp={(e) => inputValidHandle(e)}
            />

            <input
              onClick={(e) => handleConfirmEmail(e)}
              disabled={!isCheckInput.isEmail}
              isButtonValid={isCheckInput.isEmail}
            >
              중복 확인
            </input>
          </div>
          <input
            isSuccess={isCheckEmail.isSuccess}
            isFail={isCheckEmail.isFail}
            isEmail={isCheckInput.isEmail}
          >
            {isCheckEmail.isSuccess
              ? "사용 가능한 이메일입니다."
              : isCheckEmail.isFail
              ? "이미 가입 된 이메일입니다."
              : isCheckInput.isEmail === null
              ? null
              : isCheckInput.isEmail
              ? null
              : "올바른 이메일을 입력해주세요."}
          </input>
        </div>

        <div className="password-container">
          <span>비밀번호</span>
          <input
            type="password"
            id="password"
            onChange={(e) => inputValueHandle(e)}
            onKeyUp={(e) => inputValidHandle(e)}
          />
          <input isPassword={isCheckInput.isPassword}>
            {isCheckInput.isPassword === null
              ? null
              : isCheckInput.isPassword
              ? "사용 가능한 비밀번호입니다"
              : "8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요"}
          </input>
        </div>
        <input>
          <span>비밀번호 확인</span>
          <input
            type="password"
            id="passwordConfirm"
            onChange={(e) => inputValueHandle(e)}
            onKeyUp={(e) => inputValidHandle(e)}
          />
          <input isPasswordConfirm={isCheckInput.isPasswordConfirm}>
            {isCheckInput.isPasswordConfirm === null
              ? null
              : isCheckInput.isPasswordConfirm
              ? "비밀번호가 일치합니다"
              : "비밀번호를 다시 확인해 주세요"}
          </input>
        </input>
        <button
          onClick={(e) => editInfohandler(e)}
          disabled={!isSubmitValid}
          isButtonValid={isSubmitValid}
        >
          가입
        </button>
        <Link to="/login">
          가입하신 이메일이 있으신가요? <a>로그인 하러가기</a>
        </Link>
      </div>
    </div>
  );
}

export default SignupPage;
