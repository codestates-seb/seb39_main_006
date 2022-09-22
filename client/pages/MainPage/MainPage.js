import { useDispatch } from "react-redux";
import classes from "./MainPage.module.css";
import { mainpageActions } from "../store/mainpage";

const MainPage = () => {
  const dispatch = useDispatch();

  const inputHandler = (event) => {
    event.preventDefault();

    dispatch(mainpageActions.mainpage());
  };

  return (
    <div className={classes.mainpage}>
      <div className={classes.container}>
        <h1>메인페이지</h1>
        <input onSubmit={inputHandler}>
          <div className={classes.container}>
            <button>submit</button>
          </div>
          <div className={classes.container}></div>
          <button>Login</button>
        </input>
      </div>
    </div>
  );
};

export default Auth;
