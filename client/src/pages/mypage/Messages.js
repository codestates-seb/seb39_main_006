import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SideBar from "../../components/SideBar";
import H1 from "../../components/ui/H1";
const Messages = () => {
  const [messages, setMessages] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`${process.env.REACT_APP_URL}/api/messages`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    }).then((res) => {
      setMessages(res.data.data);
    });
  }, []);
  return (
    <div>
      <SideBar />
      <H1>알림들</H1>
      {messages.map((el, idx) => (
        <div key={idx}>
          <span
            onClick={() => {
              navigate(`/${el.postId}`);
            }}
          >
            {el.body}
          </span>
          <span> {el.messageStatus === "READ" ? "읽음" : "안읽음"}</span>
        </div>
      ))}
    </div>
  );
};

export default Messages;
