import React from "react";
//react 에서 img import 하는법 https://velog.io/@ingdol2/React-image-%EA%B2%BD%EB%A1%9C-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0
import imgLogo from "../img/newWave.gif";
import loginBtn from "../img/darkLogo.png";
import { useNavigate } from "react-router-dom";

const Header = () => {
  const navigate = useNavigate();

  const logoutHandler = () => {
    sessionStorage.clear();
    navigate(`/`);
    window.location.reload();
  };
  return (
    <header>
      <h1
        onClick={() => {
          sessionStorage.getItem("isLogin") ? navigate(`/auth`) : navigate(`/`);
        }}
      >
        HITCH : HICKER
      </h1>
      {sessionStorage.getItem("isLogin") && (
        <nav>
          <ul>
            <div align="center">
              <img
                className="banner"
                src={imgLogo}
                alt="./newWave.gif"
                width="400"
                height="140"
              />
            </div>
            <li>
              <a href="/auth">
                <img
                  className="login"
                  src={loginBtn}
                  alt="./darkLogo.png"
                  width="50"
                  height="50"
                />
              </a>
            </li>
            <li>
              <a href="/auth">User BTN</a>
            </li>
            <li>
              <button onClick={logoutHandler}>Logout</button>
            </li>
          </ul>
        </nav>
      )}
    </header>
  );
};

export default Header;