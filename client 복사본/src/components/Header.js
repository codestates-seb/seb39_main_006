import React, { useEffect, useState } from "react";

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
      <div className="container">
        {/* 고정헤더영역 */}
        <header className="header-area">
          <div className="palce-header">
            <h1
              className="title"
              onClick={() => {
                sessionStorage.getItem("isLogin")
                  ? navigate(`/main`)
                  : navigate(`/`);
              }}
              data-item="HITCH : HIKER"
            >
              HITCH : HIKER
            </h1>
          </div>
        </header>
        <div className="place-header">
          {sessionStorage.getItem("isLogin") && (
            <div>
              <div className="place-header-bottom">
                <div>{msgIds.length}</div>

                <div>
                  <img
                    src={profileImg}
                    width="30"
                    height="30"
                    onClick={() => {
                      toggleMsg();
                    }}
                  />
                </div>

                <button className="logoutBtn" onClick={logoutHandler}>
                  Logout
                </button>
              </div>

              {showMsg ? (
                <div className="place-header-bottom">
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
                      <div key={idx} className="place-header-bottom">
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
            </div>
          )}
          {/* //고정헤더영역 */}
        </div>
      </div>
    </HeaderSection>
  );
};

export default Header;
const HeaderSection = styled.div`
  .header-area {
    position: relative;
    display: block;
    top: 0;
    left: 0;
    background-color: #ca7575;
    color: #fff;
    z-index: 5000;
    width: 100%;
    .container {
      background-color: #fff;
      top: 0;
      left: 0;
      display: flex;
      flex: 1;
      justify-content: space-around;
    }
  }
  .place-header {
    display: flex;
    display: block;
    place-content: center;
    margin: 0 auto;
    height: 50px;
    width: 100%;
    text-align: center;
    height: 60px;
    line-height: 40px;
  }
  .place-header-bottom {
    display: flex;
    flex: 1;
    justify-content: space-around;
    width: 80%;
  }
`;
