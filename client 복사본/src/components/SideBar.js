import React from "react";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
const SideBar = () => {
  const navigate = useNavigate();

  const mypageHandler = () => {
    navigate("/mypage");
  };
  const mybookmarkHandler = () => {
    navigate("/mybookmark");
  };
  const mypostkHandler = () => {
    navigate("/mypost");
  };
  const mymatchinfokHandler = () => {
    navigate("/mymatchinfo");
  };
  const messagesHandler = () => {
    navigate("/messages");
  };
  return (
    <SideBars>
      <nav>
        <ul>
          <button onClick={mypageHandler}>
            <li id="home">
              <div className="home-icon">
                <div className="roof">
                  <div className="roof-edge"></div>
                </div>
                <div className="front"></div>
              </div>
            </li>
          </button>
          <button onClick={mybookmarkHandler}>
            <li id="about">
              <div className="about-icon">
                <div className="head">
                  <div className="eyes"></div>
                  <div className="beard"></div>
                </div>
              </div>
            </li>
          </button>
          <button onClick={mypostkHandler}>
            <li id="work">
              <div className="work-icon">
                <div className="paper"></div>
                <div className="lines"></div>
                <div className="lines"></div>
                <div className="lines"></div>
              </div>
            </li>
          </button>
          <button onClick={mymatchinfokHandler}>
            <li id="mail">
              <div className="mail-icon">
                <div className="mail-base">
                  <div className="mail-top"></div>
                </div>
              </div>
            </li>
          </button>
          <button onClick={messagesHandler}>
            <li id="newmail">
              <div className="newmail-icon">
                <div className="newmail-base">
                  <div className="newmail-top"></div>
                </div>
              </div>
            </li>
          </button>
        </ul>
      </nav>
    </SideBars>
  );
};

export default SideBar;

const SideBars = styled.div`
  
`;
