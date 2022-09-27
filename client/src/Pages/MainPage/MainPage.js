import React from "react";
import Posts from "../PostPage/Posts";
import SideBar from "../../components/SideBar";
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
