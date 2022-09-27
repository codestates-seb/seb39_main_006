import React from "react";
import styled from "styled-components";
const SideBar = () => {
  return (
    <StyledDiv>
      <div className="container">
        <div class="wrapper"></div>
        <nav className="nav__cont">
          <ul class="nav">
            <li className="nav__items ">
              <a href="">북마크</a>
            </li>
            <li className="nav__items ">
              <a href="">유저신고</a>
            </li>
            <li className="nav__items ">
              <a href="">차단유저관리</a>
            </li>
            <li className="nav__items ">
              <a href="">등등</a>
            </li>
          </ul>
        </nav>
      </div>
    </StyledDiv>
  );
};

export default SideBar;

const StyledDiv = styled.div`
  .container {
    width: 300px;
    opacity: 90%;
    height: 100vh;
    height: 100vh;
    font-family: "Gill Sans", "Gill Sans MT", Calibri, "Trebuchet MS",
      sans-serif;
    .nav__cont {
      position: fixed;
      width: 60px;
      top: 0px;
      height: 100vh;
      z-index: 100;
      background-image: url(../img/flower4.jpeg);
      overflow: hidden;
      transition: width 0.3s ease;
      cursor: pointer;
      box-shadow: 4px 7px 10px rgba(0, 0, 0, 0.4);
      &:hover {
        width: 200px;
      }
      @media screen and (min-width: 600px) {
        width: 80px;
      }
    }

    .nav {
      list-style-type: none;
      color: white;
      &:first-child {
        padding-top: 1.5rem;
      }
    }

    .nav__items {
      padding-bottom: 4rem;
      a {
        position: relative;
        display: block;
        top: -35px;
        margin-top: 2rem;
        font-size: large;
        font-weight: 700;
        padding-left: 25px;
        padding-right: 15px;
        transition: all 0.3s ease;
        margin-left: 25px;
        margin-right: 10px;
        text-decoration: none;
        color: white;
        font-weight: 100;
        font-size: 1.35em;
        &:after {
          content: "";
          width: 100%;
          height: 100%;
          position: absolute;
          top: 0;
          left: 0;
          border-radius: 2px;
          background: radial-gradient(
            circle at 94.02% 88.03%,
            #54a4ff,
            transparent 100%
          );
          opacity: 0;
          transition: all 0.5s ease;
          z-index: -10;
        }
      }
      &:hover a:after {
        opacity: 1;
      }
      svg {
        width: 26px;
        height: 26px;
        position: relative;
        left: -25px;
        cursor: pointer;
        @media screen and(min-width:600px) {
          width: 32px;
          height: 32px;
          left: -15px;
        }
      }
    }
  }
`;
