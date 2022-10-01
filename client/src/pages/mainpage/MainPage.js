import React from "react";
import Posts from "../postpage/Posts";
import SideBar from "../../components/SideBar";
import MainSubHeader from "./MainSubHeader";
import styled from "styled-components";

const MainPage = () => {
  return (
    <>
      <MainSubHeader />
      <Flex>
        <SideBar />
        <Posts />
      </Flex>
    </>
  );
};

export default MainPage;

const Flex = styled.div`
  display: flex;
  height: 100vh;
`;
