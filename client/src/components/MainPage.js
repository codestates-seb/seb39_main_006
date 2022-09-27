import React from "react";
import Posts from "./Posts";
import SideBar from "./SideBar";
import MainSubHeader from "./MainSubHeader";

const MainPage = () => {
  return (
    <>
      <MainSubHeader />
      <div>
        <SideBar />
        <Posts />
      </div>
    </>
  );
};

export default MainPage;
