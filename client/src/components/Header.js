import React from "react";
//react 에서 img import 하는법 https://velog.io/@ingdol2/React-image-%EA%B2%BD%EB%A1%9C-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0
import imgLogo from "../img/newWave.gif";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";

const Header = () => {
  const navigate = useNavigate();

  const logoutHandler = () => {
    sessionStorage.clear();
    navigate(`/`);
    window.location.reload();
  };
  return (
    <HeaderContainer>
      <h1
        onClick={() => {
          sessionStorage.getItem("isLogin") ? navigate(`/auth`) : navigate(`/`);
        }}
      >
        HITCH : HICKER
      </h1>
      {sessionStorage.getItem("isLogin") && (
        <nav>
          <HeaderFlex>
            <div align="center">
              <img src={imgLogo} alt="./newWave.gif" width="400" height="140" />
            </div>

            <li>
              <a href="/auth">MyPage</a>
            </li>
            <li>
              <button onClick={logoutHandler}>Logout</button>
            </li>
          </HeaderFlex>
        </nav>
      )}
    </HeaderContainer>
  );
};

export default Header;
const HeaderContainer = styled.div`
  display: flex;
`;
const HeaderFlex = styled.ul`
  display: flex;
`;
