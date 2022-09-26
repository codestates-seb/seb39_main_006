import { useDispatch, useSelector } from "react-redux";
import classes from "./Header.module.css";
import { authActions } from "../store/auth";
//react 에서 img import 하는법 https://velog.io/@ingdol2/React-image-%EA%B2%BD%EB%A1%9C-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0
import imgLogo from "../img/newWave.gif";
import loginBtn from "../img/darkLogo.png";
import { useNavigate } from "react-router-dom";

const Header = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isAuth = useSelector((state) => state.auth.isAuthenticated);

  const logoutHandler = () => {
    dispatch(authActions.logout());
    navigate(`/`);
  };
  return (
    <header className={classes.header}>
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
              <a href="/">User BTN</a>
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
