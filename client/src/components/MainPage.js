import React from "react";
import Posts from "./Posts";
import classes from "./MainPage.module.css";
import SideBar from "./SideBar";
import MainSubHeader from "./MainSubHeader";
import { useSelector } from "react-redux";

const MainPage = () => {
  const isAuth = useSelector((state) => state.auth.isAuthenticated);

  return (
    <>
      <MainSubHeader />
      <div className={classes.container}>
        <SideBar />
        <Posts />
      </div>
    </>
  );
};

export default MainPage;
