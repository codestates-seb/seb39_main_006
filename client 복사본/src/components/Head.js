/*eslint-disable*/
import styled from "styled-components";
import profileImg from "../img/bell.png";
import { useNavigate } from "react-router-dom";
import {
  useLogoutHandler,
  useMsgClickHandler,
  useReadAllMessage,
  useToggleMsg,
} from "../components/socket/SocketStomp.js";

const Header = () => {
  const navigate = useNavigate();
  return (
    <>
      <HeaderSection>
        <div className="container">
          <header className="header-area">
            <div className="palce-header">
              <h1
                className="title"
                onClick={() => {
                  sessionStorage.getItem("isLogin")
                    ? navigate(`/main`)
                    : navigate(`/`);
                }}
              >
                {" "}
                HITCH : HIKER
              </h1>
            </div>
          </header>
          <div className="header-area">
            <div className="container">
              <div className="place-header-bottom">
                <button className="nav">
                  <img
                    src={profileImg}
                    width="30"
                    height="30"
                    onClick={() => {
                      useToggleMsg();
                    }}
                  />
                </button>

                <button className="nav" onClick={useLogoutHandler}>
                  Logout
                </button>

                {showMsg ? (
                  <div className="mgs">
                    {showMsg && (
                      <button
                        className="alarm"
                        onClick={() => {
                          useReadAllMessage();
                        }}
                      >
                        전체 읽음
                      </button>
                    )}
                    {showMsg &&
                      msgs.map((el, idx) => (
                        <div key={idx}>
                          <div
                            onClick={() => {
                              useMsgClickHandler(el.messageId, el.postId);
                            }}
                          >
                            {el.body}
                          </div>
                        </div>
                      ))}
                  </div>
                ) : (
                  ""
                )}
                {/* //고정헤더영역 */}
              </div>
            </div>
          </div>
        </div>
      </HeaderSection>
    </>
  );
};
export default Header;

const HeaderSection = styled.div`
  box-sizing: border-box;
  .header-area {
    position: relative;
    top: 0;
    left: 0;
    background-color: #ca7575;
    color: #fff;
    z-index: 5000;
  }
  .container {
    background-color: #fff;
    top: 0;
    left: 0;
    .place-header {
      display: flex;
      place-content: center;
      margin: 0 auto;
      height: 50px;
      width: 100%;
      text-align: center;
    }
    .place-header-bottom {
      display: flex;
      flex: 1;
      justify-content: space-around;
    }
  }
`;
