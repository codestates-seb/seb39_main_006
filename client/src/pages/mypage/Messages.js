import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SideBar from "../../components/SideBar";
import H1 from "../../components/ui/H1";
const Messages = () => {
  const [messages, setMessages] = useState([]);
  const [msgIds, setMsgIds] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`${process.env.REACT_APP_URL}/api/messages`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    }).then((res) => {
      setMessages(res.data.data);
      res.data.data
        .filter((el) => {
          return el.messageStatus === "READ";
        })
        .map((el) => {
          return setMsgIds((msgIds) => [...msgIds, el.messageId]);
        });
    });
  }, []);

  const msgClickHandler = (postId, msgId) => {
    axios(`${process.env.REACT_APP_URL}/api/messages/read?messageId=${msgId}`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    }).then(() => {
      navigate(`/${postId}`);
      window.location.reload();
    });
  };

  const readMsgAllDelete = () => {
    axios(
      `${process.env.REACT_APP_URL}/api/messages/delete?messageId=${msgIds}`,
      {
        method: "DELETE",
        headers: {
          access_hh: sessionStorage.getItem("AccessToken"),
        },
      }
    ).then(() => {
      window.location.reload();
    });
  };

  const msgDelete = (msgId) => {
    axios(
      `${process.env.REACT_APP_URL}/api/messages/delete?messageId=${msgId}`,
      {
        method: "DELETE",
        headers: {
          access_hh: sessionStorage.getItem("AccessToken"),
        },
      }
    ).then(() => {
      window.location.reload();
    });
  };

  return (
    <div>
      <SideBar />
      <H1>
        알림들{" "}
        <button
          onClick={() => {
            readMsgAllDelete();
          }}
        >
          읽은 메세지 삭제
        </button>
      </H1>
      {messages.map((el, idx) => (
        <div key={idx}>
          <span
            onClick={() => {
              msgClickHandler(el.postId, el.messageId);
            }}
          >
            {el.body}
          </span>
          <span> {el.messageStatus === "READ" ? "읽음" : "안읽음"}</span>
          <span>
            {el.messageStatus === "READ" ? (
              <button
                onClick={() => {
                  msgDelete(el.messageId);
                }}
              >
                알림 삭제
              </button>
            ) : null}
          </span>
        </div>
      ))}
    </div>
  );
};

export default Messages;
