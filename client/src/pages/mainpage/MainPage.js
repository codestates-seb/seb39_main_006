import React from "react";
import Posts from "../postpage/Posts";
import SideBar from "../../components/SideBar";
import MainSubHeader from "./MainSubHeader";
import styled from "styled-components";

const MainPage = () => {
  return (
    <>
      <Section>
        <section>
          <h1>Main Page</h1>
          <h2>여행동행자 모집합니다</h2>
        </section>
      </Section>
      <MainSubHeader />
      <Flex>
        <Posts />
      </Flex>
    </>
  );
};

export default MainPage;

const Flex = styled.div`
  /* align-items: center; */
  margin-left: 10%;
  display: flex;
  height: 2000px;
`;
const Section = styled.div`
  section {
    display: flex;
    position: relative;
    left: 100px;
    list-style: none;
  }

  h1 {
    font-size: 52px;
    display: block;
    align-items: center;
    font-family: "Montserrat", sans-serif;
    text-transform: uppercase;
    text-shadow: 1px 1px 0px #dabbc9, 2px 2px 0px #dabbc9, 3px 3px 0px #dabbc9,
      4px 4px 0px #dabbc9;
  }
  h2 {
    padding: 1rem;
    font-size: 15px;
    display: block;
    align-items: center;
    font-family: "Montserrat", sans-serif;
    text-transform: uppercase;
    text-shadow: 1px 1px 0px #dabbc9, 1px 1px 0px #dabbc9, 2px 2px 0px #dabbc9,
      3px 3px 0px #dabbc9;
  }
`;
