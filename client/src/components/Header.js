import React, { useEffect, useState } from "react";
import imgLogo from "../img/realWave.gif";
import profileImg from "../img/bell.png";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import SockJs from "sockjs-client";
import StompJs from "stompjs";
import axios from "axios";

const Header = () => {
  const [msgs, setMsgs] = useState([]);
  const [msgIds, setMsgIds] = useState([]);
  const [showMsg, setShowMsg] = useState(false);
  const navigate = useNavigate();

  const logoutHandler = () => {
    axios(`${process.env.REACT_APP_URL}/api/members/logout`, {
      headers: {
        method: "Post",
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    });
    sessionStorage.clear();
    navigate(`/`);
    window.location.reload();
  };

  useEffect(() => {
    if (sessionStorage.getItem("isLogin")) {
      axios(`${process.env.REACT_APP_URL}/api/messages/not-read`, {
        headers: {
          access_hh: sessionStorage.getItem("AccessToken"),
        },
      })
        .then((res) => {
          setMsgs((msgs) => [...msgs, ...res.data.data]);
          res.data.data.map((el) => {
            return setMsgIds((msgIds) => [...msgIds, el.messageId]);
          });
        })
        .catch((err) => {
          if (err.response.status === 400) {
            if (err.response.data.fieldErrors) {
              alert(err.response.data.fieldErrors[0].reason);
            } else if (
              err.response.data.fieldErrors === null &&
              err.response.data.violationErrors
            ) {
              alert(err.response.data.violationErrors[0].reason);
            } else {
              alert(
                "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
              );
            }
          } else if (err.response.status === 0)
            alert(
              "서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
            );
          else {
            if (
              err.response.data.korMessage ===
              "만료된 토큰입니다. 다시 로그인 해주세요."
            ) {
              sessionStorage.clear();
              navigate(`/`);
              window.location.reload();
            } else if (err.response.data.korMessage) {
              alert(err.response.data.korMessage);
            } else {
              alert(
                "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
              );
            }
          }
          window.location.reload();
        });
    }
  }, []);

  useEffect(() => {
    if (sessionStorage.getItem("isLogin")) {
      const socket = new SockJs(`${process.env.REACT_APP_URL}/websocket`);
      const client = StompJs.over(socket);
      client.debug = null;
      client.connect(
        {
          access_hh: sessionStorage.getItem("AccessToken"),
        },
        () => {
          client.subscribe(
            "/topic/" + sessionStorage.getItem("memberId"),
            (msg) => {
              setMsgs((msgs) => [...msgs, JSON.parse(msg.body)]);
              setMsgIds((msgIds) => [
                ...msgIds,
                JSON.parse(msg.body).messageId,
              ]);
            }
          );
        }
      );
    }
  }, []);

  const msgClickHandler = (msgId, postId) => {
    axios(`${process.env.REACT_APP_URL}/api/messages/read?messageId=${msgId}`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    })
      .then(() => {
        navigate(`/${postId}`);
        window.location.reload();
      })
      .catch((err) => {
        if (err.response.status === 400) {
          if (err.response.data.fieldErrors) {
            alert(err.response.data.fieldErrors[0].reason);
          } else if (
            err.response.data.fieldErrors === null &&
            err.response.data.violationErrors
          ) {
            alert(err.response.data.violationErrors[0].reason);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        } else if (err.response.status === 0)
          alert(
            "서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
          );
        else {
          if (
            err.response.data.korMessage ===
            "만료된 토큰입니다. 다시 로그인 해주세요."
          ) {
            sessionStorage.clear();
            navigate(`/`);
            window.location.reload();
          } else if (err.response.data.korMessage) {
            alert(err.response.data.korMessage);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        }
        window.location.reload();
      });
  };

  const readAllMessage = () => {
    axios(
      `${process.env.REACT_APP_URL}/api/messages/read?messageId=${msgIds}`,
      {
        headers: {
          access_hh: sessionStorage.getItem("AccessToken"),
        },
      }
    )
      .then(() => {
        window.location.reload();
      })
      .catch((err) => {
        if (err.response.status === 400) {
          if (err.response.data.fieldErrors) {
            alert(err.response.data.fieldErrors[0].reason);
          } else if (
            err.response.data.fieldErrors === null &&
            err.response.data.violationErrors
          ) {
            alert(err.response.data.violationErrors[0].reason);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        } else if (err.response.status === 0)
          alert(
            "서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
          );
        else {
          if (
            err.response.data.korMessage ===
            "만료된 토큰입니다. 다시 로그인 해주세요."
          ) {
            sessionStorage.clear();
            navigate(`/`);
            window.location.reload();
          } else if (err.response.data.korMessage) {
            alert(err.response.data.korMessage);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        }
        window.location.reload();
      });
  };

  const toggleMsg = () => {
    setShowMsg(!showMsg);
  };

  return (
    <HeaderSection>
      <p
        onClick={() => {
          sessionStorage.getItem("isLogin") ? navigate(`/main`) : navigate(`/`);
        }}
        data-item="HITCH : HIKER"
      >
        HITCH : HIKER
      </p>

      {sessionStorage.getItem("isLogin") && (
        <Test>
          <nav>
            <ul className="menuItems">
              <li>
                <a href="/main" data-item="mainpage">
                  mainpage
                </a>
              </li>
              <li>
                <a href="/feeds" data-item="feeds">
                  FEEDS
                </a>
              </li>
              <li>
                <a href="/mypage" data-item="mypage">
                  mypage
                </a>
              </li>

              <details className="dropdown">
                <summary role="Button">
                  <div>{msgIds.length}</div>
                  <img
                    src={profileImg}
                    width="40"
                    height="40"
                    onClick={() => {
                      toggleMsg();
                    }}
                  />
                </summary>
                <ul>
                  {/* <li id="alarm">
                  <button
                      className="alarm"
                      onClick={() => {
                        readAllMessage();
                      }}
                    >
                      전체 읽음
                    </button>
                  </li> */}
                  {/* <li id="alarm">
                    <button
                      className="alarm"
                      onClick={() => {
                        toggleMsg();
                      }}
                    >
                      새알람 확인 {msgIds.length}
                    </button>
                  </li> */}
                </ul>
              </details>
              <li>
                <button className="logoutBtn" onClick={logoutHandler}>
                  Logout
                </button>
              </li>
            </ul>

            <img
              className="Button"
              src={imgLogo}
              alt="./newWave.gif"
              width="400"
              height="140"
            />
            <a className="banner"></a>
          </nav>
          {showMsg ? (
            <div className="mgs">
              {showMsg && (
                <button
                  className="alarm"
                  onClick={() => {
                    readAllMessage();
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
                        msgClickHandler(el.messageId, el.postId);
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
        </Test>
      )}
    </HeaderSection>
  );
};

export default Header;

const Test = styled.div`
  .mgs {
    box-sizing: border-box;
    position: relative;
    display: inline-block;
    margin: 40px;
    padding: 20px;
    border: 1px solid black;
    border-radius: 5px;
    box-shadow: 0px 0px 30px rgba(0, 0, 0, 0.2);
    background: rgba(255, 255, 255, 0.6);
    font-family: Roboto;
    width: fit-content;
    height: fit-content;
  }
  #alarm {
    margin: -1rem;
  }
  .alarm {
    display: inline-block;
    width: 8rem;
    padding: 0.1rem;
  }
  display: flex;
  .logoutBtn {
    margin-left: 1rem;
    padding: 10px;
    border-color: #16213b;
    background: #16213b;
    color: wheat;
    border-radius: 13px;
    &:hover {
      background-color: #304b61;
      border-color: #c2e3de;
    }
  }
`;

const HeaderSection = styled.div`
  img {
    margin-left: 4%;
  }
  display: grid;
  place-items: center;

  * {
    padding: 0;
    margin: 0;
    summary::marker {
      display: none;
      content: "";
    }
  }
  // Developed by http://grohit.com/

  .Button {
    /* margin-top: 1rem; */
    align-items: center;
    /* margin-left: 8rem; */
    list-style-type: none;
    border-radius: 10px;
    display: inline-block;
    display: flex;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.9);
    position: relative;
    height: 10rem;

    width: 31rem;
    opacity: 100%;
  }

  #logout-btn {
    li {
      margin: 50%;
      padding: 1rem;

      a {
        text-decoration: none;
        color: #efd5c8;
        font-size: 3rem;
        font-weight: 400;
        transition: all 0.5s ease-in-out;
        position: relative;
        text-transform: uppercase;

        &::before {
          content: attr(data-item);
          transition: 0.5s;
          color: #efd5c8;
          position: absolute;
          top: 0;
          bottom: 0;
          left: 0;
          right: 0;
          width: 0;
          overflow: hidden;
        }

        &:hover {
          &::before {
            width: 100%;
            transition: all 0.5s ease-in-out;
          }
        }
      }
    }
  }
  body {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    position: relative;
    min-height: 50vh;
    font-family: Hack, monospace;
  }
  button {
    font-size: 1rem;
    background-color: #dabbc9;
    width: fit-content;
    border: 1px solid #dabbc9;
    padding: 0.1rem 1rem;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
    color: #425049;
    &:hover {
      background-color: #efd5c8;
      border-color: #efd5c8;
    }
  }
  div {
    color: #727272;
    text-align: center;
  }

  p {
    line-height: 4rem;
    margin-top: 5rem;
    padding-bottom: 20px;
    font-size: 5rem;
    color: #003046;
    text-transform: uppercase;
    font-weight: 600;
    transition: all 1s ease-in-out;
    position: relative;

    &::before {
      content: attr(data-item);
      transition: all 1s ease-in-out;
      color: #e5e5e5;
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      width: 0;
      overflow: hidden;
    }

    &:hover {
      &::before {
        width: 100%;
      }
    }
  }

  nav {
    margin-bottom: 1rem;
    border-radius: 10px;
    box-shadow: 0 1px 4px rgba(1, 1, 0, 0.4);
    /* margin: 70px; */
    background: #d0e8f0;
    opacity: 90%;
    width: 35rem;
    padding: 0.5rem;

    .menuItems {
      list-style: none;
      display: flex;

      /* margin-left: 2rem; */
      ul {
        text-align: center;
      }
      li {
        margin: 0.5rem;
        margin-right: 3rem;
        display: inline-block;
        a {
          text-decoration: none;
          color: #8f8f8f;
          font-size: 24px;
          font-weight: 400;
          transition: all 0.5s ease-in-out;
          position: relative;
          text-transform: uppercase;

          &::before {
            content: attr(data-item);
            transition: 0.5s;
            color: #425049;
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            width: 0;
            overflow: hidden;
          }

          &:hover {
            &::before {
              width: 100%;
              transition: all 0.5s ease-in-out;
            }
          }
        }
      }
    }
  }
`;
