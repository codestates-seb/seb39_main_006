import React from "react";
import Posts from "./Posts";
import classes from "./MainPage.module.css";
import SideBar from "./SideBar";
import MainSubHeader from "./MainSubHeader";

const MainPage = () => {
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
