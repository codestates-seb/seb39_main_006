import { useDispatch, useSelector } from "react-redux";
import { authActions } from "../store/auth";
//react 에서 img import 하는법 https://velog.io/@ingdol2/React-image-%EA%B2%BD%EB%A1%9C-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0
import imgLogo from "../img/newWave.gif";
import loginBtn from "../img/darkLogo.png";
import singupBtn from "../img/logo.png";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
const Header = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isAuth = useSelector((state) => state.auth.isAuthenticated);

  const logoutHandler = () => {
    dispatch(authActions.logout());
    navigate(`/`);
  };
  return (
    <StyledHeader>
      <header className="header">
        <h1
          onClick={() => {
            navigate(`/`);
          }}
        >
          HITCH : HICKER
        </h1>
        {isAuth && (
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
                <a href="/">
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
                <a href="/">
                  <img
                    className="signup"
                    src={singupBtn}
                    alt="./logo.png"
                    width="50"
                    height="50"
                  />
                </a>
              </li>
              <li>
                <a href="/">User BTN</a>
              </li>
              <li>
                <button onClick={logoutHandler}>Logout</button>
              </li>
            </ul>
          </nav>
        )}
      </header>
    </StyledHeader>
  );
};

export default Header;
const StyledHeader = styled.div`
  .header {
    width: 100%;
    height: 150px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: yellowgreen;
    color: white;
    padding: 0 10%;
    background-image: url(../img/flower4.jpeg);
    opacity: 80%;
    background-repeat: repeat-x;
    ul {
      display: flex;
      list-style: none;
      margin: 0;
      padding: 0;
      align-items: center;
    }
    li {
      margin: 0 1rem;
    }
    a {
      text-decoration: none;
      color: white;
      font-size: 1.25rem;
      &:hover {
        color: #b864da;
      }
    }

    button {
      font-size: 1.25rem;
      background-color: #ffbb00;
      width: fit-content;
      border: 1px solid #ffbb00;
      padding: 0.5rem 1rem;
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
      color: #2c2922;
      &:hover {
        background-color: #ffa600;
        border-color: #ffa600;
      }
    }

    img {
      border-radius: 10px;
    }
  }
`;
