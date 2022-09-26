import React from "react";
import styled from "styled-components";
const SideBar = () => {
  return (
    <StyledDiv>
      <div className="container">
        <div class="wrapper">
          <main>
            <h1>사이드바 </h1>
          </main>
        </div>
        <nav className="nav__cont">
          <ul class="nav">
            <li className="nav__items ">
              <a href="">Home</a>
            </li>
            <li className="nav__items ">
              <a href="">Search</a>
            </li>
            <li className="nav__items ">
              <a href="">Map</a>
            </li>
            <li className="nav__items ">
              <a href="">Planner</a>
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
    height: 100vh;
    border: 1px solid black;
    height: 100vh;

    .nav__cont {
      position: fixed;
      width: 60px;
      top: 150px;
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
      font-family: "roboto";
      a {
        position: relative;
        display: block;
        top: -35px;
        padding-left: 25px;
        padding-right: 15px;
        transition: all 0.3s ease;
        margin-left: 25px;
        margin-right: 10px;
        text-decoration: none;
        color: white;
        font-family: "roboto";
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
